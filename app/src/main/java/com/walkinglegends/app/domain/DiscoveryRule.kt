package com.walkinglegends.app.domain

import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

const val DISCOVERY_RADIUS_METRES = 120.0
const val MAXIMUM_ACCEPTED_ACCURACY_METRES = 120.0

data class GeoPoint(
    val latitude: Double,
    val longitude: Double,
)

data class LocationFix(
    val latitude: Double,
    val longitude: Double,
    val accuracyMetres: Double,
) {
    fun asGeoPoint() = GeoPoint(latitude, longitude)
}

data class DiscoveryEvaluation(
    val eligible: Boolean,
    val reason: DiscoveryReason,
    val distanceMetres: Double,
    val accuracyMetres: Double,
)

enum class DiscoveryReason {
    WITHIN_RADIUS,
    TOO_FAR,
    ACCURACY_TOO_LOW,
}

/**
 * Portable discovery rules shared in behaviour with the browser prototype.
 * The Android UI only awards an artefact after this rule returns eligible.
 */
object DiscoveryRule {
    private const val EarthRadiusMetres = 6_371_008.8

    fun distanceBetweenMetres(from: GeoPoint, to: GeoPoint): Double {
        requireValidCoordinate(from)
        requireValidCoordinate(to)
        val latitudeDelta = Math.toRadians(to.latitude - from.latitude)
        val longitudeDelta = Math.toRadians(to.longitude - from.longitude)
        val fromLatitude = Math.toRadians(from.latitude)
        val toLatitude = Math.toRadians(to.latitude)
        val halfChord = sin(latitudeDelta / 2) * sin(latitudeDelta / 2) +
            cos(fromLatitude) * cos(toLatitude) * sin(longitudeDelta / 2) * sin(longitudeDelta / 2)
        return 2 * EarthRadiusMetres * asin(min(1.0, sqrt(halfChord)))
    }

    fun evaluate(location: LocationFix, landmark: GeoPoint): DiscoveryEvaluation {
        require(location.accuracyMetres >= 0.0 && location.accuracyMetres.isFinite())
        val distance = distanceBetweenMetres(location.asGeoPoint(), landmark)
        return when {
            location.accuracyMetres > MAXIMUM_ACCEPTED_ACCURACY_METRES -> DiscoveryEvaluation(
                eligible = false,
                reason = DiscoveryReason.ACCURACY_TOO_LOW,
                distanceMetres = distance,
                accuracyMetres = location.accuracyMetres,
            )
            distance > DISCOVERY_RADIUS_METRES -> DiscoveryEvaluation(
                eligible = false,
                reason = DiscoveryReason.TOO_FAR,
                distanceMetres = distance,
                accuracyMetres = location.accuracyMetres,
            )
            else -> DiscoveryEvaluation(
                eligible = true,
                reason = DiscoveryReason.WITHIN_RADIUS,
                distanceMetres = distance,
                accuracyMetres = location.accuracyMetres,
            )
        }
    }

    private fun requireValidCoordinate(point: GeoPoint) {
        require(point.latitude.isFinite() && point.longitude.isFinite())
        require(point.latitude in -90.0..90.0 && point.longitude in -180.0..180.0)
    }
}
