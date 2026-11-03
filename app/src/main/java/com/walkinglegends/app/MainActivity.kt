package com.walkinglegends.app

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.walkinglegends.app.domain.DiscoveryEvaluation
import com.walkinglegends.app.domain.DiscoveryReason
import com.walkinglegends.app.domain.Landmark
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(colorScheme = walkingLegendsColorScheme()) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WalkingLegendsApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WalkingLegendsApp(viewModel: ExplorationViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { grants ->
        if (grants[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            viewModel.refreshLocation()
        } else {
            viewModel.onLocationPermissionDenied()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Walking Legends") }) },
    ) { padding ->
        val refreshLocation = {
            if (context.hasForegroundLocationPermission()) {
                viewModel.refreshLocation()
            } else {
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                    ),
                )
            }
        }
        ExplorerScreen(
            state = state,
            worldTitle = viewModel.selectedWorld().title,
            region = viewModel.selectedWorld().region,
            landmarks = viewModel.selectedWorld().landmarks,
            evaluationFor = viewModel::evaluationFor,
            onRefreshLocation = refreshLocation,
            onDiscover = viewModel::discover,
            contentPadding = padding,
        )
    }
}

@Composable
private fun ExplorerScreen(
    state: ExplorerUiState,
    worldTitle: String,
    region: String,
    landmarks: List<Landmark>,
    evaluationFor: (Landmark) -> DiscoveryEvaluation?,
    onRefreshLocation: () -> Unit,
    onDiscover: (Landmark) -> Unit,
    contentPadding: PaddingValues,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = contentPadding.calculateTopPadding() + 16.dp,
            bottom = contentPadding.calculateBottomPadding() + 24.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF244B3B))) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("EUROPE · FIRST EXPEDITION", color = Color(0xFFE4B75A), style = MaterialTheme.typography.labelLarge)
                    Text(worldTitle, color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text(region, color = Color(0xFFE1E8E2))
                    Text("${state.xp} XP · ${state.foundLandmarkIds.size}/${landmarks.size} artefacts", color = Color.White)
                }
            }
        }
        item {
            LocationCard(state.locationStatus, onRefreshLocation)
        }
        item {
            Text("Athens landmarks", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("A discovery needs a current reading within 120 metres, accurate to 120 metres or better.")
        }
        items(landmarks, key = { it.id }) { landmark ->
            LandmarkCard(
                landmark = landmark,
                found = landmark.id in state.foundLandmarkIds,
                evaluation = evaluationFor(landmark),
                onDiscover = { onDiscover(landmark) },
            )
        }
    }
}

@Composable
private fun LocationCard(status: LocationStatus, onRefresh: () -> Unit) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("Your location", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                when (status) {
                    LocationStatus.NotChecked -> "Check your location when you are ready to explore."
                    LocationStatus.Loading -> "Getting a fresh high-accuracy location…"
                    is LocationStatus.Ready -> "Accuracy ±${status.fix.accuracyMetres.roundToInt()} m"
                    is LocationStatus.Error -> status.message
                    LocationStatus.PermissionDenied -> "Location permission is needed for real-world discoveries."
                },
            )
            Button(onClick = onRefresh, enabled = status !is LocationStatus.Loading) {
                Text(if (status is LocationStatus.Loading) "Refreshing…" else "Check / refresh location")
            }
        }
    }
}

@Composable
private fun LandmarkCard(
    landmark: Landmark,
    found: Boolean,
    evaluation: DiscoveryEvaluation?,
    onDiscover: () -> Unit,
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(landmark.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(landmark.subtitle)
            Text(if (found) "Artefact found: ${landmark.artefact}" else locationHint(evaluation))
            if (found) {
                Text(landmark.story)
            } else {
                Button(onClick = onDiscover, enabled = evaluation?.eligible == true) {
                    Text("Discover with GPS")
                }
            }
        }
    }
}

private fun locationHint(evaluation: DiscoveryEvaluation?): String = when {
    evaluation == null -> "Check your location · 120 m discovery radius"
    evaluation.reason == DiscoveryReason.ACCURACY_TOO_LOW ->
        "GPS accuracy ±${evaluation.accuracyMetres.roundToInt()} m · improve to 120 m or better"
    evaluation.eligible -> "Within discovery range · ${evaluation.distanceMetres.roundToInt()} m away"
    else -> "Outside 120 m radius · ${evaluation.distanceMetres.roundToInt()} m away"
}

private fun Context.hasForegroundLocationPermission(): Boolean =
    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

private fun walkingLegendsColorScheme() = lightColorScheme(
    primary = Color(0xFF244B3B),
    secondary = Color(0xFFE4B75A),
)
