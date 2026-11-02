import test from "node:test";
import assert from "node:assert/strict";

import {
  DISCOVERY_RADIUS_METRES,
  distanceBetweenMetres,
  evaluateDiscovery,
  formatDistance,
  locationFromGeolocation,
} from "../js/gps.js";
import { ANCIENT_GREECE, EUROPE_WORLDS } from "../js/worlds.js";

test("GPS distance is zero for matching coordinates", () => {
  assert.equal(distanceBetweenMetres({ lat: 37.9715, lon: 23.7257 }, { lat: 37.9715, lon: 23.7257 }), 0);
});

test("a precise reading inside the 120 metre radius can unlock a discovery", () => {
  const landmark = { lat: 37.9715, lon: 23.7257 };
  const result = evaluateDiscovery({ lat: 37.9723, lon: 23.7257, accuracyMetres: 8 }, landmark);

  assert.equal(result.eligible, true);
  assert.equal(result.reason, "within-radius");
  assert.ok(result.distanceMetres < DISCOVERY_RADIUS_METRES);
});

test("a reading outside the 120 metre radius cannot unlock a discovery", () => {
  const landmark = { lat: 37.9715, lon: 23.7257 };
  const result = evaluateDiscovery({ lat: 37.973, lon: 23.7257, accuracyMetres: 8 }, landmark);

  assert.equal(result.eligible, false);
  assert.equal(result.reason, "too-far");
  assert.ok(result.distanceMetres > DISCOVERY_RADIUS_METRES);
});

test("an imprecise reading is rejected even when its reported point is at a landmark", () => {
  const landmark = { lat: 37.9715, lon: 23.7257 };
  const result = evaluateDiscovery({ ...landmark, accuracyMetres: 121 }, landmark);

  assert.deepEqual(result, {
    eligible: false,
    reason: "accuracy-too-low",
    distanceMetres: 0,
    accuracyMetres: 121,
  });
});

test("browser geolocation is normalised into the portable location shape", () => {
  assert.deepEqual(
    locationFromGeolocation({ coords: { latitude: 51.5, longitude: -0.12, accuracy: 6 } }),
    { lat: 51.5, lon: -0.12, accuracyMetres: 6 },
  );
});

test("distance labels remain concise and usable", () => {
  assert.equal(formatDistance(119.6), "120 m away");
  assert.equal(formatDistance(1_240), "1.2 km away");
  assert.equal(formatDistance(-1), "Distance unavailable");
});

test("Europe-first world content has one playable Greek world and unique identifiers", () => {
  assert.equal(ANCIENT_GREECE.status, "playable");
  assert.equal(ANCIENT_GREECE.continent, "Europe");
  assert.equal(ANCIENT_GREECE.places.length, 3);
  assert.equal(new Set(EUROPE_WORLDS.map((world) => world.id)).size, EUROPE_WORLDS.length);
});

