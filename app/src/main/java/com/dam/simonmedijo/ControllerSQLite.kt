package com.dam.simonmedijo

import android.content.Context
import java.util.Date

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