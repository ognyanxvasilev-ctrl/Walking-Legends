package com.walkinglegends.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.walkinglegends.app.domain.DiscoveryEvaluation
import com.walkinglegends.app.domain.DiscoveryRule
import com.walkinglegends.app.domain.EuropeWorlds
import com.walkinglegends.app.domain.Landmark
import com.walkinglegends.app.domain.LocationFix
import com.walkinglegends.app.location.AndroidLocationGateway
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface LocationStatus {
    data object NotChecked : LocationStatus
    data object Loading : LocationStatus
    data class Ready(val fix: LocationFix) : LocationStatus
    data class Error(val message: String) : LocationStatus
    data object PermissionDenied : LocationStatus
}

data class ExplorerUiState(
    val locationStatus: LocationStatus = LocationStatus.NotChecked,
    val xp: Int = 0,
    val foundLandmarkIds: Set<String> = emptySet(),
)

class ExplorationViewModel(application: Application) : AndroidViewModel(application) {
    private val locationGateway = AndroidLocationGateway(application.applicationContext)
    private val _state = MutableStateFlow(ExplorerUiState())
    val state: StateFlow<ExplorerUiState> = _state.asStateFlow()

    fun refreshLocation() {
        if (_state.value.locationStatus is LocationStatus.Loading) return
        _state.update { it.copy(locationStatus = LocationStatus.Loading) }
        viewModelScope.launch {
            locationGateway.currentHighAccuracyLocation().fold(
                onSuccess = { fix -> _state.update { it.copy(locationStatus = LocationStatus.Ready(fix)) } },
                onFailure = { error ->
                    _state.update {
                        it.copy(locationStatus = LocationStatus.Error(error.message ?: "Could not get a current location."))
                    }
                },
            )
        }
    }

    fun onLocationPermissionDenied() {
        _state.update { it.copy(locationStatus = LocationStatus.PermissionDenied) }
    }

    fun evaluationFor(landmark: Landmark): DiscoveryEvaluation? {
        val fix = (_state.value.locationStatus as? LocationStatus.Ready)?.fix ?: return null
        return DiscoveryRule.evaluate(fix, landmark.point)
    }

    fun discover(landmark: Landmark) {
        val evaluation = evaluationFor(landmark) ?: return
        if (!evaluation.eligible || landmark.id in _state.value.foundLandmarkIds) return
        _state.update {
            it.copy(
                xp = it.xp + 100,
                foundLandmarkIds = it.foundLandmarkIds + landmark.id,
            )
        }
    }

    fun selectedWorld() = EuropeWorlds.ancientGreece
}
