package com.dam.simonmedijo

import java.util.Date

interface Conexion {
    fun obtenerRecord(): Record
    fun actualizarRecord(nuevoRecord: Int, fecha: Date): Record
}