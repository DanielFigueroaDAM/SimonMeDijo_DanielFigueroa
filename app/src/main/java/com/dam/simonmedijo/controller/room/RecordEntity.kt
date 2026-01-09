package com.dam.simonmedijo.controller.room





import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Entidad que representa la tabla en la base de datos
@Entity(tableName = "records")
data class RecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Clave primaria autogenerada
    val record: Int, // El valor del récord
    val fecha: Long // Fecha del récord
)
