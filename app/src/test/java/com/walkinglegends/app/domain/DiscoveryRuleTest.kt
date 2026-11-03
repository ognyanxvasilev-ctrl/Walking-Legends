package com.walkinglegends.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DiscoveryRuleTest {
    private val acropolis = GeoPoint(37.9715, 23.7257)

    @Test
    fun `precise location inside 120 metres is eligible`() {
        val result = DiscoveryRule.evaluate(
            LocationFix(latitude = 37.9723, longitude = 23.7257, accuracyMetres = 8.0),
            acropolis,
        )

        assertTrue(result.eligible)
        assertEquals(DiscoveryReason.WITHIN_RADIUS, result.reason)
        assertTrue(result.distanceMetres < DISCOVERY_RADIUS_METRES)
    }

    @Test
    fun `imprecise location is rejected at a landmark`() {
        val result = DiscoveryRule.evaluate(
            LocationFix(latitude = acropolis.latitude, longitude = acropolis.longitude, accuracyMetres = 121.0),
            acropolis,
        )

        assertFalse(result.eligible)
        assertEquals(DiscoveryReason.ACCURACY_TOO_LOW, result.reason)
    }

    @Test
    fun `precise location outside radius is rejected`() {
        val result = DiscoveryRule.evaluate(
            LocationFix(latitude = 37.973, longitude = 23.7257, accuracyMetres = 8.0),
            acropolis,
        )

        assertFalse(result.eligible)
        assertEquals(DiscoveryReason.TOO_FAR, result.reason)
        assertTrue(result.distanceMetres > DISCOVERY_RADIUS_METRES)
    }

    @Test
    fun `europe content keeps only Athens playable`() {
        assertEquals(WorldStatus.PLAYABLE, EuropeWorlds.ancientGreece.status)
        assertEquals(3, EuropeWorlds.ancientGreece.landmarks.size)
        assertTrue(EuropeWorlds.catalogue.drop(1).all { it.status == WorldStatus.PLANNED && it.landmarks.isEmpty() })
    }
}
