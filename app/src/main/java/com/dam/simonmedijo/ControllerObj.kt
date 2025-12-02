package com.dam.simonmedijo

import java.util.Date

object ControllerObj : Conexion {
    override fun obtenerRecord(): Record {
        return Record
    }

    override fun actualizarRecord(nuevoRecord: Int, fecha: Date): Record {
        Record.record.value = nuevoRecord
        Record.fecha.value = fecha
        return Record
    }

}