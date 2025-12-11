package com.dam.simonmedijo

import android.content.Context
import android.provider.BaseColumns
import java.util.Date

object miTabla : BaseColumns {
    const val TABLE_NAME = "RECORD"
    const val PUNTUACION = "PUNTUACIÓN"
    const val FECHA = "FECHA"
}


object ControllerSQLite : Conexion {


    override fun obtenerRecord(context: Context): Record {

    }

    override fun actualizarRecord(
        nuevoRecord: Int,
        fecha: Date,
        context: Context
    ): Record {
        TODO("Not yet implemented")
    }
}