package viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.example.maps_compose_s.models.GoogleGeoResults
import com.example.maps_compose_s.retrofit.ApiService
import com.example.maps_compose_s.retrofit.RouteResponse
import com.example.maps_compose_s.retrofit.getRetrofit


import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL


class SearchViewModel:ViewModel() {
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


    val polylineOptions by mutableStateOf<PolylineOptions>(PolylineOptions())


    //permisos concedios?
    var lationPermision by mutableStateOf(false)




    fun currentUserGeoCord(latLng: LatLng){
        latUser=latLng.latitude
        longUser=latLng.longitude
    }
    fun actualizarLocalizacionSeleccionada(status:Boolean){
        isNewLocationSelected.value=status
    }


    fun permisosGrand(setGrand:Boolean){
        lationPermision=setGrand
    }


    fun getLocation(search:String){
        viewModelScope.launch {
            val  apiKey="AIzaSyD9LhRibGkiCAuEtXTwtSQM6I90R3roDDE"
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


    //funcion para crear  la ruta hace un llamado a la api que de ser successes retorna las cordenadas
    fun createRoute(){
        CoroutineScope(Dispatchers.IO).launch{




            if( lat!=0.0 && long!= 0.0 && latUser != 0.0 && longUser!= 0.0) {


                inicioRuta="${longUser},${latUser}"
                finRuta =  "${long} ,${lat}"


                val call =
                    getRetrofit().create(ApiService::class.java)
                        .getRoute("5b3ce3597851110001cf624880ba9f39b183459c94792e8955141ba2", inicioRuta, finRuta)


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




}
