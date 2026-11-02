/**
 * GPS rules shared by the browser prototype today and the Android app later.
 * A discovery is deliberately conservative: an imprecise reading may never
 * unlock a landmark, even if its reported point lands inside the radius.
 */
export const DISCOVERY_RADIUS_METRES = 120;
export const MAXIMUM_ACCEPTED_ACCURACY_METRES = 120;

const EARTH_RADIUS_METRES = 6_371_008.8;

function assertCoordinate(location, label) {
  if (!location || !Number.isFinite(location.lat) || !Number.isFinite(location.lon)) {
    throw new TypeError(`${label} must include finite lat and lon values.`);
  }
  if (location.lat < -90 || location.lat > 90 || location.lon < -180 || location.lon > 180) {
    throw new RangeError(`${label} contains an invalid latitude or longitude.`);
  }
}

function toRadians(degrees) {
  return (degrees * Math.PI) / 180;
}

/** Returns the great-circle distance between two WGS84 coordinates in metres. */
export function distanceBetweenMetres(from, to) {
  assertCoordinate(from, "from");
  assertCoordinate(to, "to");

  const deltaLatitude = toRadians(to.lat - from.lat);
  const deltaLongitude = toRadians(to.lon - from.lon);
  const fromLatitude = toRadians(from.lat);
  const toLatitude = toRadians(to.lat);
  const halfChord =
    Math.sin(deltaLatitude / 2) ** 2 +
    Math.cos(fromLatitude) * Math.cos(toLatitude) * Math.sin(deltaLongitude / 2) ** 2;

  return 2 * EARTH_RADIUS_METRES * Math.asin(Math.min(1, Math.sqrt(halfChord)));
}

/** Converts a browser GeolocationPosition into the app's portable location shape. */
export function locationFromGeolocation(position) {
  const { latitude, longitude, accuracy } = position?.coords ?? {};
  if (![latitude, longitude, accuracy].every(Number.isFinite) || accuracy < 0) {
    throw new TypeError("The GPS reading is incomplete or invalid.");
  }
  return { lat: latitude, lon: longitude, accuracyMetres: accuracy };
}

/**
 * Checks the 120 m discovery rule without mutating game state.
 * Consumers should persist a discovery only when `eligible` is true.
 */
export function evaluateDiscovery(
  location,
  landmark,
  {
    radiusMetres = DISCOVERY_RADIUS_METRES,
    maximumAccuracyMetres = MAXIMUM_ACCEPTED_ACCURACY_METRES,
  } = {},
) {
  assertCoordinate(location, "location");
  assertCoordinate(landmark, "landmark");
  if (!Number.isFinite(location.accuracyMetres) || location.accuracyMetres < 0) {
    throw new TypeError("location must include a non-negative accuracyMetres value.");
  }
  if (!Number.isFinite(radiusMetres) || radiusMetres <= 0 || !Number.isFinite(maximumAccuracyMetres) || maximumAccuracyMetres <= 0) {
    throw new RangeError("Discovery radius and maximum accuracy must be positive numbers.");
  }

  const distanceMetres = distanceBetweenMetres(location, landmark);
  if (location.accuracyMetres > maximumAccuracyMetres) {
    return {
      eligible: false,
      reason: "accuracy-too-low",
      distanceMetres,
      accuracyMetres: location.accuracyMetres,
    };
  }
  if (distanceMetres > radiusMetres) {
    return {
      eligible: false,
      reason: "too-far",
      distanceMetres,
      accuracyMetres: location.accuracyMetres,
    };
  }
  return {
    eligible: true,
    reason: "within-radius",
    distanceMetres,
    accuracyMetres: location.accuracyMetres,
  };
}

export function formatDistance(distanceMetres) {
  if (!Number.isFinite(distanceMetres) || distanceMetres < 0) {
    return "Distance unavailable";
  }
  if (distanceMetres < 1_000) {
    return `${Math.round(distanceMetres)} m away`;
  }
  return `${(distanceMetres / 1_000).toFixed(1)} km away`;
}

