package com.dam.simonmedijo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

// Clase donde se guardan los datos del record(record,fecha)
data class Record( val record: Int = 0, val fecha: Date = Date())


