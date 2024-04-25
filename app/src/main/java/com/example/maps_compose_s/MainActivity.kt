package com.example.maps_compose_s

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import com.example.maps_compose_s.navigation.NavManager
import com.example.maps_compose_s.ui.theme.Maps_compose_sTheme
import com.example.maps_compose_s.viewModel.SearchViewModel
import com.example.maps_compose_s.views.HomeView
import com.example.maps_compose_s.views.MapsView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel: SearchViewModel by viewModels()


        setContent {
            Maps_compose_sTheme {
               Surface(modifier = Modifier.fillMaxSize()) {
                  //MapsView()
                  NavManager(serchVM = viewModel)

               }

                }
            }
        }
    }


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Maps_compose_sTheme {
        Greeting("Android")
    }
}