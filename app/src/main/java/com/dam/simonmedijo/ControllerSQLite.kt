package com.dam.simonmedijo

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date

object miTabla : BaseColumns {
    const val TABLE_NAME = "RECORD"
    const val PUNTUACION = "PUNTUACIÓN"
    const val FECHA = "FECHA"
}


object ControllerSQLite : Conexion {

    /**
     * Obtiene el ultimo Record de la base de datos SQLite.
     * @param context El contexto de la aplicación, necesario para acceder a la base de datos.
     */
    override fun obtenerRecord(context: Context): Record {
        val dbHelper = FeedReaderDbHelper(context)
        val db = dbHelper.readableDatabase

        // 1. Define las columnas que quieres obtener
        val projection = arrayOf(miTabla.PUNTUACION, miTabla.FECHA)

        // 2. Ordena por puntuacion en orden DESCENDENTE (el más nuevo aparecerá primero)
        val sortOrder = "${miTabla.PUNTUACION} DESC"


        // 3. Añade el LÍMITE de 1 para que la base de datos solo devuelva un resultado
        val limit = "1"

        val cursor = db.query(
            miTabla.TABLE_NAME,   // La tabla
            projection,           // Las columnas a devolver
            null,                 // Cláusula WHERE (ninguna)
            null,                 // Argumentos del WHERE (ninguno)
            null,                 // No agrupar las filas
            null,                 // No filtrar por grupos
            sortOrder,            // Ordenar por puntuacion, más nuevo primero
            limit                 // Límite de 1 resultado
        )

        var recordValue = 0
        var fechaValue = Date() // Usa un nombre de variable diferente para claridad

        // 4. Usa 'if (moveToFirst())' para leer el único registro que hemos pedido
        with(cursor) {
            if (moveToFirst()) { // Si la consulta devolvió al menos un resultado...
                recordValue = getInt(getColumnIndexOrThrow(miTabla.PUNTUACION))
                // Leemos el Long de la base de datos y lo convertimos a un objeto Date
                val fechaLong = getLong(getColumnIndexOrThrow(miTabla.FECHA))
                fechaValue = Date(fechaLong)
            }
        }

        // 5. Cierra siempre el cursor y la base de datos para liberar recursos.
        cursor.close()
        db.close()

        // 6. Devuelve el resultado. Si no se encontró nada, serán los valores por defecto (0 y la fecha actual)
        return Record(recordValue, fechaValue)
    }


    /**
    * Inserta un nuevo registro de puntuación en la base de datos.
    *
    * @param nuevoRecord La puntuación obtenida en la partida.
    * @param fecha La fecha y hora exactas en que se consiguió el récord.
    * @param context El contexto de la aplicación, necesario para acceder a la base de datos.
    * @return Devuelve el objeto Record que se acaba de guardar.
    */
    override fun actualizarRecord(
        nuevoRecord: Int,
        fecha: Date,
        context: Context
    ): Record {
        // 1. Obtiene una instancia del ayudante de la base de datos (DbHelper).
        //    El DbHelper gestiona la creación y actualización de la base de datos.
        val dbHelper = FeedReaderDbHelper(context)
        // 2. Prepara los datos que se van a insertar usando un objeto ContentValues.
        //    ContentValues funciona como un mapa (clave-valor) donde la clave es el nombre
        //    de la columna y el valor es el dato que se quiere guardar.
        val values = ContentValues().apply {
            // Añade la puntuación a la columna PUNTUACION.
            put(miTabla.PUNTUACION, nuevoRecord)
            // Añade la fecha a la columna FECHA.
            // Es CRÍTICO usar 'fecha.time' para convertir el objeto Date a un Long (milisegundos),
            // ya que SQLite no puede almacenar objetos Date directamente, pero sí números largos.
            put(miTabla.FECHA, fecha.time)
        }

        // 3. Obtiene la base de datos en modo escritura e inserta los nuevos valores.
        //    El método .insert() ejecuta la operación en la base de datos.
        //    - miTabla.TABLE_NAME: La tabla donde se insertarán los datos.
        //    - null: Argumento 'nullColumnHack'. Se usa para permitir insertar filas vacías (no es nuestro caso).
        //    - values: El objeto ContentValues con los datos a insertar.
        //    El método devuelve el ID de la nueva fila insertada, o -1 si hubo un error.
        val newRowId = dbHelper.writableDatabase.insert(miTabla.TABLE_NAME, null, values)

        // 4. (Opcional pero muy útil para depurar) Imprime un log para confirmar que la inserción fue exitosa.
        //    Podrás ver este mensaje en el Logcat de Android Studio si filtras por la etiqueta "newRowId".
        Log.d("newRowId", "Nueva fila insertada con ID: $newRowId")

        // 5. Cierra la conexión a la base de datos para liberar recursos y evitar fugas de memoria.
        //    Es una buena práctica cerrar la base de datos después de cada operación.
        dbHelper.close()

        // 6. Devuelve el objeto Record con los datos que se acaban de guardar.
        return Record(nuevoRecord, fecha)
    }

    // a esta funcion se le llama desde el view model, está se encargará a partir de ahora a  hacer las inserciones
    fun manejadorRecords(context: Context, nuevoRecord: Int, fecha: Date){
        val lista = leerBaseDatos(context)
        if(comprobarInsercion(context, nuevoRecord, fecha)){
            if(lista.size > 9){
                borrarUltimoRecord(context)
            }
            actualizarRecord(nuevoRecord,fecha,context)

        }
    }
    // Este metodo se encarga de borrar el metodo mas pequeño, que sería el adecuado
    fun conseguirRecordMásPequeño(context: Context):Int{
        val lista = leerBaseDatos(context)
        var recordMasPequeño = lista[0].record
        for(i in lista){
            if(i.record < recordMasPequeño){
                recordMasPequeño = i.record
            }
        }
        return recordMasPequeño
    }


    //Borra el ultimo record, osea el mas pequeño
    fun borrarUltimoRecord(context: Context){
        val recordMasPequeño = conseguirRecordMásPequeño(context)
        val dbHelper = FeedReaderDbHelper(context)
        val selection = "${miTabla.PUNTUACION} LIKE ?"
        val selectionArgs = arrayOf(recordMasPequeño.toString())
        val deletedRows = dbHelper.writableDatabase.delete(miTabla.TABLE_NAME, selection, selectionArgs)
        dbHelper.close()

    }



    //comprobamos si se puede insertar el record a partir de la lista de records de la base de datos
    fun comprobarInsercion(context: Context, nuevoRecord: Int, fecha: Date): Boolean{
        val lista = leerBaseDatos(context)

        if(lista.size >= 10 && nuevoRecord < lista[lista.size-1].record){
            // No hay que insertarlo
            return false
        }else{
            for( i in lista){
                if(i.record == nuevoRecord){
                    return false
                }
            }
        }
        return true
    }


    /**
     * Esta funcion se encarga de devolver el tamaño de la funcion y de leer toda la base mediante logs
     */
    fun leerBaseDatos(context: Context): MutableList<Record> {
        val dbHelper = FeedReaderDbHelper(context)
        val db = dbHelper.readableDatabase

        // 1. Define las columnas que quieres obtener
        val projection = arrayOf(miTabla.PUNTUACION, miTabla.FECHA)

        // 2. Ordena por fecha en orden DESCENDENTE (el más nuevo aparecerá primero)
        val sortOrder = "${miTabla.FECHA} DESC"


        val cursor = db.query(
            miTabla.TABLE_NAME,   // La tabla
            projection,           // Las columnas a devolver
            null,                 // Cláusula WHERE (ninguna)
            null,                 // Argumentos del WHERE (ninguno)
            null,                 // No agrupar las filas
            null,                 // No filtrar por grupos
            sortOrder,            // Ordenar por fecha, más nuevo primero
            null
        )

        var recordValue = 0
        var fechaValue = Date() // Usa un nombre de variable diferente para claridad
        var lista = mutableListOf<Record>()
        // 4. Usa 'if (moveToFirst())' para leer el único registro que hemos pedido
        with(cursor) {
            for(i in 0..10) {
                if (moveToPosition(i)) { // Si la consulta devolvió al menos un resultado...
                    recordValue = getInt(getColumnIndexOrThrow(miTabla.PUNTUACION))
                    // Leemos el Long de la base de datos y lo convertimos a un objeto Date
                    val fechaLong = getLong(getColumnIndexOrThrow(miTabla.FECHA))
                    fechaValue = Date(fechaLong)
                    lista.add(Record(recordValue, fechaValue))

                }
            }
        }
        Log.d("LOG",lista.toString())
        Log.d("LOG",lista.size.toString())
        // 5. Cierra siempre el cursor y la base de datos para liberar recursos.
        cursor.close()
        db.close()
        // 6. Devuelve el resultado. Si no se encontró nada, serán los valores por defecto (0 y la fecha actual)
        return lista
    }
}