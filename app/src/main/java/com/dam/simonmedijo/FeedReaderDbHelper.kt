package com.dam.simonmedijo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
// Sentencia SQL para borrar la tabla si existe.
// Se utiliza en onUpgrade() para eliminar la versión antigua de la tabla.
private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${miTabla.TABLE_NAME}"

// Sentencia SQL para crear la tabla de la base de datos.
// Incluye:
// - _ID: clave primaria de tipo INTEGER
// - PUNTUACION: entero con la puntuación guardada
// - FECHA: campo de tipo DATETIME para almacenar la fecha
private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${miTabla.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${miTabla.PUNTUACION} INTEGER," +
            "${miTabla.FECHA} DATETIME )"

// Clase que gestiona la creación, actualización y apertura de la base de datos.
// Extiende SQLiteOpenHelper, que facilita manejar bases de datos SQLite en Android.
class FeedReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    // Se ejecuta la primera vez que se crea la base de datos.
    // Aquí se ejecuta la sentencia SQL que crea la tabla.
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    // Se ejecuta cuando cambia la versión de la base de datos (DATABASE_VERSION).
    // En este caso, como la base solo almacena datos temporales o simples,
    // se borra la tabla antigua y se vuelve a crear desde cero.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    // Si la versión baja (algo poco común), se aplica la misma política:
    // borrar y volver a crear la base de datos.
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // Zona estática de la clase (similar a "static" en Java).
        // Permite usar estas constantes sin crear un objeto de la clase.

        // Versión del esquema de la base de datos.
        // Si cambias la estructura, sube este número.
        const val DATABASE_VERSION = 1

        // Nombre del archivo donde se guarda la base de datos.
        const val DATABASE_NAME = "miBaseDeDatos.db"
    }

}
