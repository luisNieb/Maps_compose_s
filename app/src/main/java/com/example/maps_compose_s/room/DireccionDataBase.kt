package com.example.maps_compose_s.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.maps_compose_s.models.Direcciones


@Database(entities = [Direcciones::class], version = 1 , exportSchema = false)
abstract class DireccionDataBase:RoomDatabase(){
    abstract fun direccionesDao():DireccionBaseDatosDao
}