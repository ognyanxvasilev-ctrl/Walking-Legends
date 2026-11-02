import { DISCOVERY_RADIUS_METRES } from "./gps.js";

function assertPlace(place) {
  if (!place || !Number.isFinite(place.lat) || !Number.isFinite(place.lon)) {
    throw new TypeError("A map place must include finite lat and lon values.");
  }
}

/**
 * Translates world content into a renderer-neutral map model. Leaflet uses this
 * today; the Android app can use the same shape without duplicating location
 * and discovery-zone rules.
 */
export function createExplorationMapModel(world, { discoveryRadiusMetres = DISCOVERY_RADIUS_METRES } = {}) {
  if (!world || !Array.isArray(world.places) || world.places.length === 0) {
    throw new TypeError("A playable world with at least one place is required for a map.");
  }
  if (!Number.isFinite(discoveryRadiusMetres) || discoveryRadiusMetres <= 0) {
    throw new RangeError("discoveryRadiusMetres must be a positive number.");
  }

  const markers = world.places.map((place) => {
    assertPlace(place);
    return {
      id: place.id,
      name: place.name,
      subtitle: place.subtitle,
      icon: place.icon,
      lat: place.lat,
      lon: place.lon,
      discoveryRadiusMetres,
      accessibleLabel: `${place.name}. Discovery radius ${discoveryRadiusMetres} metres.`,
    };
  });

  const center = {
    lat: markers.reduce((total, marker) => total + marker.lat, 0) / markers.length,
    lon: markers.reduce((total, marker) => total + marker.lon, 0) / markers.length,
  };

  return { center, markers, defaultZoom: 15 };
}
