package room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.maps_compose_s.models.Direcciones
import kotlinx.coroutines.flow.Flow



@Dao
interface DireccionBaseDatosDao{

    @Query("SELECT * FROM direcciones")
    fun getDireccion(): Flow<List<Direcciones>>

    @Query("SELECT * From direcciones WHERE id=:id")
    fun getDirecionesByID(id:Long):Flow<Direcciones>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(direcciones: Direcciones)


    @Delete
    suspend fun delete(direcciones: Direcciones)


}