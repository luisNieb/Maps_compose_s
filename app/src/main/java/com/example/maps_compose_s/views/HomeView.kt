package com.example.maps_compose_s.views

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
import com.example.maps_compose_s.R
import com.example.maps_compose_s.componentes.LocationPermissions
import com.example.maps_compose_s.models.Direcciones
import com.example.maps_compose_s.viewModel.SearchViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeView(navController: NavController , serchVm : SearchViewModel){
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)



    serchVm.polylineOptions.points.clear()
    serchVm.dibujarLinea=false


    serchVm.LocalizacionActual(LocalContext.current)
    if(serchVm.latUser!=0.0 && serchVm.longUser !=0.0){
        var local= "${serchVm.latUser} ${serchVm.longUser}"
        serchVm.getLocation(local)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Buscar lugar") })
        }
    ) { pad->
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
            var search  by remember { mutableStateOf(serchVm.addreess) }
            OutlinedTextField(
                value =  search ,
                onValueChange ={ search=it} ,
                label = { Text(text = "Buscar")}
            )
            OutlinedButton(onClick = { if(search !="") serchVm.getLocation(search) }) {
                Text(text = "Buscar")
                
            }
            if(serchVm.show){
                Card(
                    modifier = Modifier
                        .height(300.dp)
                        .padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.LocationOn,
                        contentDescription = "",
                        modifier = Modifier.size(40.dp)
                    )

                    Text(text = "Latitud: ${serchVm.lat}", modifier = Modifier.padding(all = 10.dp))
                    Text(text = "Longitud : ${serchVm.long}", modifier = Modifier.padding(all = 10.dp))
                    Text(text = "Direccion : ${serchVm.addreess}", modifier = Modifier.padding(all = 10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Button(onClick = {
                            navController.navigate("MapSearchView/${serchVm.lat}/${serchVm.long}/${serchVm.addreess}")
                            serchVm.show=false
                        },
                            modifier = Modifier.padding(start=10.dp, end = 15.dp)

                        ) {
                            Text(text = "Ir a direccion")

                        }

                        Button(onClick = {
                            //guardar los datos en room
                            serchVm.agregarDireccion(
                                Direcciones(
                                    latitud = serchVm.lat,
                                    longitud = serchVm.long,
                                    direccion = serchVm.addreess
                                )
                            )


                        }) {
                            Text(text = "Guardar tu Direccion")
                        }

                    }

                    
                }



            } //lista de direcciones  guardadas
            val direccionesLista by serchVm.listaDirecciones.collectAsState()
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(direccionesLista){direccion->

                    val eliminar= SwipeAction(
                        icon= rememberVectorPainter(image = Icons.Default.Delete),
                        background = Color.Red,
                        onSwipe = { serchVm.eliminarDireccion(direccion)}
                    )

                    SwipeableActionsBox (endActions = listOf(eliminar), swipeThreshold = 100.dp){
                        SaveDirecionCard(
                            lat = direccion.latitud,
                            long = direccion.longitud,
                            direccion =direccion.direccion ,
                            navController = navController,
                            viewModel = serchVm
                        )
                    }

                }
            }

            LocationPermissions(
                text = "TU LOCALIZACION",
                rationale = "We need access to your location for some awesome features!",
                locationState = locationPermissionState,
                serchVm
            )




        }
        
    }

}

@Composable
fun SaveDirecionCard(lat:Double, long:Double ,direccion:String ,navController: NavController, viewModel: SearchViewModel){
    Card(
        modifier = Modifier
            .height(300.dp)
            .padding(16.dp)
    ) {
        Icon(imageVector = Icons.Default.Home,
            contentDescription = "",
            modifier = Modifier.size(40.dp)
        )

        Text(text = "Latitud: ${lat}", modifier = Modifier.padding(all = 10.dp))
        Text(text = "Longitud : ${long}", modifier = Modifier.padding(all = 10.dp))
        Text(text = "Direccion : ${direccion}", modifier = Modifier.padding(all = 10.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Button(onClick = {
                navController.navigate("MapSearchView/${lat}/${long}/${direccion}")

            },
                modifier = Modifier.padding(start=10.dp, end = 15.dp)

            ) {
                Text(text = "Ir a direccion")

            }


        }


    }
}


