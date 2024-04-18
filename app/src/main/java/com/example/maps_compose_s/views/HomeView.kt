package com.example.maps_compose_s.views

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
import com.example.maps_compose_s.componentes.LocationPermissions

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import viewModel.SearchViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeView(navController: NavController , serchVm : SearchViewModel){
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    serchVm.polylineOptions.points.clear()
    serchVm.dibujarLinea=false


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
            var search  by remember { mutableStateOf("Morelia") }
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
                    Button(onClick = {
                        navController.navigate("MapSearchView/${serchVm.lat}/${serchVm.long}/${serchVm.addreess}")
                        serchVm.show=false
                    },
                        modifier = Modifier.padding(start=10.dp)

                    ) {
                        Text(text = "Enviar")

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