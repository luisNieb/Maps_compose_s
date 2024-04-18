package com.example.maps_compose_s.views

import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.AdvancedMarker

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PinConfig
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable

import com.google.maps.android.compose.Marker

import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.streetview.StreetView
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

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
            },

        ){

            LaunchedEffect(Unit) {
                repeat(10) {
                    delay(5.seconds)
                    val old = markerstate.position
                    markerstate.position = LatLng(old.latitude + 1.0, old.longitude + 2.0)
                }
            }


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



//boton que controla el zoom de la camara
@Composable
fun ButtonZoomCamera(lugar:LatLng ,distacia:Float, cameraPosition: CameraPositionState) {

    Button(onClick = {
        cameraPosition.move(CameraUpdateFactory.zoomIn())
    }) {
        Text(text = "Zoom")

    }

}

