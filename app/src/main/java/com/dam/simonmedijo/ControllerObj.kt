package com.dam.simonmedijo

import java.util.Date

object ControllerObj : Conexion {
    override fun obtenerRecord(): Record {
        return Record
    }

    override fun actualizarRecord(nuevoRecord: Int, fecha: Date): Record {
        Record.record = nuevoRecord
        Record.fecha = fecha
        return Record
    }

}