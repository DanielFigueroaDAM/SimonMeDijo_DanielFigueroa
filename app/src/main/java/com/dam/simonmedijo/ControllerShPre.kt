package com.dam.simonmedijo

import android.content.Context
import androidx.core.content.edit
import java.util.Date


object ControllerShPre : Conexion {


    private const val PREFS_NAME = "preferencias_app"


    private const val KEY_RECORD = "record"

    override fun obtenerRecord(context: Context): Record {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit{
            putInt(KEY_RECORD, 0)
        }

    }

    override fun actualizarRecord(nuevoRecord: Int, fecha: Date, context: Context): Record {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        nuevoRecord = sharedPreferences.getInt(KEY_RECORD, 0)
        //falta la fecha
        val record = Record(nuevoRecord, fecha)

        return record
    }

}