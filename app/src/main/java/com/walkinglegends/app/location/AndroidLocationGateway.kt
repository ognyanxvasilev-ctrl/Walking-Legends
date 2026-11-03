package com.walkinglegends.app.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.walkinglegends.app.domain.LocationFix
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AndroidLocationGateway(context: Context) {
    private val client = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission") // The activity checks foreground permission before calling this gateway.
    suspend fun currentHighAccuracyLocation(): Result<LocationFix> = suspendCoroutine { continuation ->
        val token = CancellationTokenSource()
        client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, token.token)
            .addOnSuccessListener { location ->
                if (location == null) {
                    continuation.resume(Result.failure(IllegalStateException("No current location is available.")))
                } else {
                    continuation.resume(
                        Result.success(
                            LocationFix(
                                latitude = location.latitude,
                                longitude = location.longitude,
                                accuracyMetres = location.accuracy.toDouble(),
                            ),
                        ),
                    )
                }
            }
            .addOnFailureListener { error -> continuation.resume(Result.failure(error)) }
    }
}
