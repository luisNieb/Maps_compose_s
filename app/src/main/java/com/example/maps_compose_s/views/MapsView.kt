package com.example.maps_compose_s.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapsView(){


    val newYork= LatLng(40.758896,-73.985130)
    //agregar un marcador
    val markerstate= rememberMarkerState(position = newYork)
    val camaraPosition= CameraPosition.fromLatLngZoom(newYork, 10f)
    val camaraState= rememberCameraPositionState{ position= camaraPosition }
    //agregamos un spinner de carga
    var mapLoading by remember{ mutableStateOf(true) }


    Box(modifier=Modifier.fillMaxSize()){
        GoogleMap(
            modifier= Modifier.matchParentSize(),
            cameraPositionState = camaraState,
            onMapLoaded = {
                mapLoading=false
            }
        ){
            //agragamos el marcador
            Marker(
                state=markerstate,
                title = "New York"
            )
        }
        if(mapLoading){
            AnimatedVisibility(
                visible = mapLoading,
                modifier= Modifier.matchParentSize(),
                exit= fadeOut()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.wrapContentSize()
                )
            }
        }

    }


}