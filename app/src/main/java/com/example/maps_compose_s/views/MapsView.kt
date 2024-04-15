package com.example.maps_compose_s.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapsView(){

    val newYork= LatLng(40.758896,-73.985130)
    val camaraPosition= CameraPosition.fromLatLngZoom(newYork, 10f)
    val camaraState= rememberCameraPositionState{ position= camaraPosition }

    Box(modifier=Modifier.fillMaxSize()){
        GoogleMap(
            modifier= Modifier.matchParentSize(),
            cameraPositionState = camaraState
        ){

        }

    }


}