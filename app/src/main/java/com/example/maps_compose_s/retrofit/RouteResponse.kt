package com.example.maps_compose_s.retrofit

import com.example.maps_compose_s.models.Geometry
import com.google.gson.annotations.SerializedName

data class RouteResponse (@SerializedName("features") val features:List<Feature>)
data class Feature(@SerializedName("geometry") val geometry:GeometryRute)
data class GeometryRute(@SerializedName("coordinates") val coordinates:List<List<Double>>)