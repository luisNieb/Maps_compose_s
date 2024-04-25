package com.example.maps_compose_s.componentes

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCbrt
import androidx.core.app.ActivityCompat
import com.example.maps_compose_s.viewModel.SearchViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


/**
 * Simple screen that manages the location permission state
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissions(text: String, rationale: String, locationState: PermissionState ,viewModel: SearchViewModel) {
    LocationPermissions(
        text = text,
        rationale = rationale,
        locationState = rememberMultiplePermissionsState(
            permissions = listOf(
                locationState.permission
            )
        ),
        viewModel
    )
}

/**
 * Simple screen that manages the location permission state
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissions(text: String, rationale: String, locationState: MultiplePermissionsState, viewModel: SearchViewModel) {
    var showRationale by remember(locationState) {
        mutableStateOf(false)
    }
    if (showRationale) {
        PermissionRationaleDialog(rationaleState = RationaleState(
            title = "Location Permission Access",
            rationale = rationale,
            onRationaleReply = { proceed ->
                if (proceed) {
                    locationState.launchMultiplePermissionRequest()
                }
                showRationale = false
            }
        ),
            viewModel
        )
    }
    Box(modifier = Modifier.fillMaxSize().padding(15.dp), contentAlignment = Alignment.BottomCenter) {


        PermissionRequestButton(isGranted = locationState.allPermissionsGranted , title = text ,viewModel) {
            if (locationState.shouldShowRationale) {
                showRationale = true
            } else {
                locationState.launchMultiplePermissionRequest()
            }
        }
    }
}

/**
 * A button that shows the title or the request permission action.
 */
@SuppressLint("MissingPermission")
@Composable
fun PermissionRequestButton(isGranted: Boolean, title: String, viewModel: SearchViewModel, onClick: () -> Unit) {

    if (isGranted) {
    Log.e("ISGRANTED",viewModel.lationPermision.toString())
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LocalContext.current)

        val context = LocalContext.current

            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment =Alignment.CenterHorizontally
            ) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            // Got last known location. In some rare situations this can be null.
                            viewModel.latUser = location.latitude
                            viewModel.longUser = location.longitude

                        }
                    }
            }

        } else {
            Button(onClick = onClick) {
                Text("Acceder a ${title} ")
            }
        }
    }


    /**
     * Simple AlertDialog that displays the given rational state
     */
    @Composable
    fun PermissionRationaleDialog(rationaleState: RationaleState,viewModel: SearchViewModel) {
        AlertDialog(onDismissRequest = {
            rationaleState.onRationaleReply(false) }, title = {
            Text(text = rationaleState.title)
        }, text = {
            Text(text = rationaleState.rationale)
        }, confirmButton = {
            TextButton(onClick = {

                rationaleState.onRationaleReply(true)

            }) {
                Text("Continue")
            }
        }, dismissButton = {
            TextButton(onClick = {

                rationaleState.onRationaleReply(false)
            }) {
                Text("Dismiss")
            }
        })
    }



data class RationaleState(
    val title: String,
    val rationale: String,
    val onRationaleReply: (proceed: Boolean) -> Unit,
)

