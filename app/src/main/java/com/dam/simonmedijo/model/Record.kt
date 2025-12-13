package com.dam.simonmedijo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

// Clase donde se guardan los datos del record(record,fecha)
data class Record( val record: Int = 0, val fecha: Date = Date())


// Entidad que representa la tabla en la base de datos(Podría ponerse por separado)
@Entity(tableName = "records")
data class RecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val record: Int,
    val fecha: Long
)
