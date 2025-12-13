package com.dam.simonmedijo.controller.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dam.simonmedijo.model.RecordEntity

/**
 * Esta interfaz se encarga de acceder a la tabla de records de la base de datos
 * y realizar operaciones CRUD en ella.
 * getRecord se encarga de seleccionar la ultima insertada
 * insert se encarga de insertar un nuevo registro
 */
@Dao
interface RecordDao {
    @Query("SELECT * FROM records ORDER BY id DESC LIMIT 1")
    fun getRecord(): RecordEntity?
    @Insert
    fun insert(record: RecordEntity)

}