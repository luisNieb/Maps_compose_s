package com.example.maps_compose_s.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun getRetrofit():Retrofit{
    return Retrofit.Builder()
        .baseUrl("https://api.openrouteservice.org")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}