package com.example.maps_compose_s.repositorio

import com.example.maps_compose_s.models.Direcciones
import com.example.maps_compose_s.room.DireccionBaseDatosDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DireccionRepositorio@Inject constructor(private val direccionBaseDatosDao: DireccionBaseDatosDao) {

    suspend fun addDirecion(direccion:Direcciones) =direccionBaseDatosDao.insert(direccion)
    suspend fun deleteDireccion(direccion: Direcciones)=direccionBaseDatosDao.delete(direccion)
    fun getAllDirecciones(): Flow<List<Direcciones>> = direccionBaseDatosDao.getDireccion().flowOn(Dispatchers.IO).conflate()
    fun getDireccioByID(id:Long):Flow<Direcciones> = direccionBaseDatosDao.getDirecionesByID(id).flowOn(Dispatchers.IO).conflate()

}