package com.dam.simonmedijo

import android.content.Context
import androidx.core.content.edit
import java.text.SimpleDateFormat // Importado
import java.util.Date
import java.util.Locale             // Importado

/**
 * En esta clase se implementa la conexión a las preferencias compartidas.
 * Documentación: https://developer.android.com/training/data-storage/shared-preferences?hl=es-419
 * @author Daniel Figueroa Vidal
 */
object ControllerShPre : Conexion {
    // Nombre del archivo donde se guardaran la información
    private const val PREFS_NAME = "preferencias_app"
    //Formate en el que se guardara la fecha
    private val FORMATO_FECHA = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

    //El nombre de los elementos XML que se guardarán
    private const val KEY_RECORD = "record"
    private const val KEY_FECHA = "fecha"

    /**
     * Obtiene el Record de las preferencias compartidas
     * @return Record(record,fecha)
     * @author Daniel Figueroa Vidal
     * @param context Contexto de la aplicación
     */
    override fun obtenerRecord(context: Context): Record {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val recordValue: Int = sharedPreferences.getInt(KEY_RECORD, 0)
        val fechaString: String? = sharedPreferences.getString(KEY_FECHA, null)

        val fechaFormateada: Date = if (fechaString != null) {
            try {
                // AHORA SÍ FUNCIONA: Usamos el método .parse() de nuestro formateador
                FORMATO_FECHA.parse(fechaString) ?: Date()
            } catch (e: Exception) {
                Date() // Si hay error, devuelve la fecha actual
            }
        } else {
            Date() // Si no hay fecha, devuelve la actual
        }

        return Record(recordValue, fechaFormateada)
    }

    /**
     * Actualiza el Record de las preferencias compartidas
     * @return Record(record,fecha)
     * @author Daniel Figueroa Vidal
     * @param nuevoRecord Int
     * @param fecha Date
     * @param context Contexto de la aplicación
     */
    override fun actualizarRecord(nuevoRecord: Int, fecha: Date, context: Context): Record {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val stringData = FORMATO_FECHA.format(fecha)

        sharedPreferences.edit {
            putInt(KEY_RECORD, nuevoRecord)
            putString(KEY_FECHA, stringData)
        }
        return Record(nuevoRecord, fecha)
    }
}
