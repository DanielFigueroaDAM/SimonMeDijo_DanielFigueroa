package com.dam.simonmedijo.controller.room



// Entidad que representa la tabla en la base de datos(Podría ponerse por separado)

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "records")
data class RecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val record: Int,
    val fecha: Long
)
