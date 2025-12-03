package com.dam.simonmedijo

import android.content.Context

import androidx.core.content.edit
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object ControllerShPre : Conexion {

    // preferencias_app es el nombre del archivo de preferencias
    private const val PREFS_NAME = "preferencias_app"

    private const val KEY_RECORD = "record"
    private const val KEY_FECHA = "fecha"

    private val FORMATO_FECHA = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

    override fun obtenerRecord(context: Context): Record {
        // Obtiene las preferencias del contexto
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        // Obtenemos el valor del record y la fecha de la preferencia
        val recordValue: Int = sharedPreferences.getInt(KEY_RECORD, 0)
        val fechaString: String? = sharedPreferences.getString(KEY_FECHA, null)

        val fechaFormateada: Date = if (fechaString != null) { // Si hay una fecha guardada en las preferencias, la usamos.
            try {
                // Intenta "parsear" (analizar) el String usando el formato.
                FORMATO_FECHA.parse(fechaString) ?: Date()
            } catch (e: Exception) {
                // Si hay un error (ej: el string está corrupto), usa la fecha actual.
                Date()
            }
        } else {
            // Si no hay ninguna fecha guardada, usa la fecha actual.
            Date()
        }

        return Record(recordValue, fechaFormateada)
    }

    override fun actualizarRecord(nuevoRecord: Int, fecha: Date, context: Context): Record {
        // Obtiene las preferencias del contexto
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val record = Record(nuevoRecord, fecha)
        val stringData = record.fecha.toString()
        // actualizamos el record y la fecha en las preferencias
        sharedPreferences.edit{
            putInt(KEY_RECORD, 0)
            putString(KEY_FECHA, stringData)
        }
        return record
    }

}