package com.dam.simonmedijo

import android.content.Context
import java.util.Date
/*
 Interfaz que implementa las funciones de conexión.
 */
interface Conexion {
    fun obtenerRecord(context: Context): Record
    fun actualizarRecord(nuevoRecord: Int, fecha: Date, context: Context): Record
}