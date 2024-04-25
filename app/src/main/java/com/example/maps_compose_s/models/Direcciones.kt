package com.example.maps_compose_s.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "direcciones")
data class Direcciones(
    @PrimaryKey(autoGenerate = true)
    val id: Long=0,
    @ColumnInfo(name = "latitud")
    val latitud:Double,
    @ColumnInfo(name="longitud")
    val longitud :Double,
    @ColumnInfo(name="direccion")
    val direccion:String
)
