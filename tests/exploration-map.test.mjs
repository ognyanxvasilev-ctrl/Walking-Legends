import test from "node:test";
import assert from "node:assert/strict";

import { DISCOVERY_RADIUS_METRES } from "../js/gps.js";
import { createExplorationMapModel } from "../js/exploration-map.js";
import { ANCIENT_GREECE } from "../js/worlds.js";

test("the Athens map exposes every landmark with the same 120-metre discovery zone", () => {
  const map = createExplorationMapModel(ANCIENT_GREECE);

  assert.equal(map.markers.length, ANCIENT_GREECE.places.length);
  assert.equal(map.defaultZoom, 15);
  assert.deepEqual(
    map.markers.map((marker) => marker.id),
    ANCIENT_GREECE.places.map((place) => place.id),
  );
  assert.ok(map.markers.every((marker) => marker.discoveryRadiusMetres === DISCOVERY_RADIUS_METRES));
  assert.match(map.markers[0].accessibleLabel, /120 metres/);
});

test("map model rejects a world without usable places", () => {
  assert.throws(() => createExplorationMapModel({ places: [] }), /playable world/);
  assert.throws(
    () => createExplorationMapModel({ places: [{ lat: "bad", lon: 23.7 }] }),
    /finite lat and lon/,
  );
});
