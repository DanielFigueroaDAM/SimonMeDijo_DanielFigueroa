package com.dam.simonmedijo.controller

import android.content.Context
import com.dam.simonmedijo.model.Record
import java.util.Date

/*
 Interfaz que implementa las funciones de conexión.
 */
interface Conexion {
    suspend fun obtenerRecord(context: Context): Record
    suspend fun actualizarRecord(nuevoRecord: Int, fecha: Date, context: Context): Record
}