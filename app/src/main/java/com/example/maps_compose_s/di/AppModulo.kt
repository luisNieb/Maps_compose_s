package com.example.maps_compose_s.di

import android.content.Context
import androidx.room.Room
import com.example.maps_compose_s.room.DireccionBaseDatosDao
import com.example.maps_compose_s.room.DireccionDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Singleton
    @Provides
    fun providesDireccionesDao(direccionDataBase: DireccionDataBase):DireccionBaseDatosDao{
        return direccionDataBase.direccionesDao()
    }

    //instancia de la creacion de la base de datos
    @Singleton
    @Provides
    fun providesDireccionesDataBase(@ApplicationContext context: Context):DireccionDataBase{
        return Room.databaseBuilder(
            context,
            DireccionDataBase::class.java,
            "direcciones_db"
        ).fallbackToDestructiveMigration().build()
    }

}
