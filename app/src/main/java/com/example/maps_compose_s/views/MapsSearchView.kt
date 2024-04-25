package com.example.maps_compose_s.views

import android.Manifest
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController
import com.example.maps_compose_s.R
import com.example.maps_compose_s.componentes.LocationPermissions

import com.example.maps_compose_s.viewModel.SearchViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi

import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapSearchView(lat:Double , long:Double , address: String, viewModel: SearchViewModel,navController: NavController){



      val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
      val place =LatLng(lat,long)
      val markerState= rememberMarkerState(position = place)
      val cameraPosition=CameraPosition.fromLatLngZoom(place, 10f)
      val cameraState = rememberCameraPositionState{position= cameraPosition}

      val usuarioUbicacion= LatLng(viewModel.latUser ,viewModel.longUser)

      val markerStateUsuario= rememberMarkerState(position = usuarioUbicacion)
      //agregamos un spinner de carga
      var mapLoading by remember{ mutableStateOf(true) }



      Box(modifier = Modifier.fillMaxSize()){

            GoogleMap(
                  modifier= Modifier.matchParentSize(),
                  cameraPositionState =cameraState,
                  onMapLoaded = {
                        mapLoading=false
                  },
                  onMapClick = {
                        viewModel.createRoute(lat,long)
                  }
            ){

                  if(viewModel.dibujarLinea) {
                        viewModel.polylineOptions.let {
                              Polyline(
                                    points = it.points,
                                    color = Color.Red,
                                    width = 5f
                              )
                        }
                  }

                  MarkerInfoWindow(state = markerState) {
                        CarMarker(address = address)

                  }

                  Marker(
                        state=markerStateUsuario,
                        title = "Tu ubicacion",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                        snippet = "Esta es tu ubicacion"
                  )

            }

            Column (
                  modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                  verticalArrangement = Arrangement.Bottom,
                  horizontalAlignment =Alignment.CenterHorizontally

            ){
                  Text(text = "Latitid =${viewModel.latUser} , Longitud:=${viewModel.longUser}",
                        color = Color.Black
                  )

                  Button(onClick = {
                        viewModel.createRoute(lat,long)

                  }) {
                        Text(text = "Crear una ruta")
                  }



            }
            LocationPermissions(
                  text = "TU LOCALIZACION",
                  rationale = "We need access to your location for some awesome features!",
                  locationState = locationPermissionState,
                  viewModel
            )


      }
      

}

@Composable
fun CarMarker(address: String ){

      Card(modifier = Modifier
            .padding(16.dp)
            .height(200.dp),


      ) {
         Column(modifier = Modifier
               .padding(16.dp)
               .fillMaxSize(),
               horizontalAlignment =Alignment.CenterHorizontally) {
               Icon(imageVector = Icons.Default.LocationOn,
                     contentDescription = "",
                     modifier = Modifier.size(40.dp)
               )
               Text(
                     text = address,
                     fontWeight = FontWeight.Bold, 
                     fontSize = 20.sp,
                     modifier = Modifier.padding(all = 15.dp)
               )

         }
      }


}