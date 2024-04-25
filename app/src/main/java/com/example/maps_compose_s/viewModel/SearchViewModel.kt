package com.example.maps_compose_s.viewModel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maps_compose_s.constantes.API_KEY_MAPS
import com.example.maps_compose_s.constantes.API_MAPS_TRAZAR_RUTA
import com.example.maps_compose_s.models.Direcciones

import com.example.maps_compose_s.models.GoogleGeoResults
import com.example.maps_compose_s.repositorio.DireccionRepositorio
import com.example.maps_compose_s.retrofit.ApiService
import com.example.maps_compose_s.retrofit.RouteResponse
import com.example.maps_compose_s.retrofit.getRetrofit
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repositorio: DireccionRepositorio):ViewModel() {
    //para las direcciones que esten en Room *************************************
    private val _direccionesList= MutableStateFlow<List<Direcciones>>(emptyList())
    val listaDirecciones= _direccionesList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repositorio.getAllDirecciones().collect{item->
                if(item.isEmpty()){
                    _direccionesList.value= emptyList()
                }else{
                    _direccionesList.value=item
                }
            }
        }
    }


    //*********************************************************************************

    //para la latitud
    var lat by mutableDoubleStateOf(0.0)
    private set

    var latUser by mutableDoubleStateOf(0.0)
        set

    //para la longitud
    var long by mutableDoubleStateOf(0.0)
        private set

    var longUser by mutableDoubleStateOf(0.0)
         set

    // la direccin a buscar
    var addreess by mutableStateOf("")
        private set

    var show by mutableStateOf(false)
      set

    private var _isNewLocationSelected= MutableLiveData(false)
      var isNewLocationSelected: MutableLiveData<Boolean> = _isNewLocationSelected

    //ubicacon del usuario
    var dibujarLinea by mutableStateOf(false)

    var inicioRuta by mutableStateOf("")
    var finRuta by mutableStateOf("")

    // para guardar las cordenadas de la polilinea
    val polylineOptions by mutableStateOf<PolylineOptions>(PolylineOptions())

    //permisos concedios?
    var lationPermision by mutableStateOf(false)


    fun getLocation(search:String){
        viewModelScope.launch {
            val  apiKey= API_KEY_MAPS
            val url="https://maps.googleapis.com/maps/api/geocode/json?address=$search&key=$apiKey"
            val response= withContext(Dispatchers.IO){
                 URL(url).readText()
            }
            val results= Gson().fromJson(response, GoogleGeoResults::class.java)

            if (results.results.isNotEmpty()){
                show=true
                lat= results.results[0].geometry.location.lat
                long= results.results[0].geometry.location.lng
                addreess=results.results[0].formatted_address
            }else{
                Log.d("FAIL","error")
            }
        }
    }

    //funcion para crear  la ruta hace un llamado a la aqupi que de ser successes retorna las cordenadas
    fun createRoute(latitud:Double,logitud:Double){
        CoroutineScope(Dispatchers.IO).launch{


            if( lat!=0.0 && long!= 0.0 && latUser != 0.0 && longUser!= 0.0) {

                inicioRuta="${longUser},${latUser}"
                finRuta =  "${logitud} ,${latitud}"

                val call =
                    getRetrofit().create(ApiService::class.java)
                        .getRoute(API_MAPS_TRAZAR_RUTA, inicioRuta, finRuta)

                if(call.isSuccessful){
                    dibujarRuta(call.body())
                    dibujarLinea=true
                    Log.i("RUTAS","OK")
                }else{
                    Log.e("RUTAS","KO")
                }
            }

        }
    }
    fun dibujarRuta(routeResponse: RouteResponse?){
        // Borramos las coordenadas existentes para evitar duplicados
        polylineOptions.points.clear()

        //creamos un polilinea
        //val polylineOptions = PolylineOptions()
        routeResponse?.features?.first()?.geometry?.coordinates?.forEach{

            polylineOptions.add(LatLng(it[1],it[0]))
        }

    }

    fun LocalizacionActual(context: Context){
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    // Got last known location. In some rare situations this can be null.
                    latUser = location.latitude
                    longUser = location.longitude
                }
            }
    }

    fun agregarDireccion(direcciones: Direcciones)=viewModelScope.launch { repositorio.addDirecion(direcciones) }
    fun eliminarDireccion(direcciones: Direcciones)= viewModelScope.launch { repositorio.deleteDireccion(direcciones) }

}