package com.dam.simonmedijo.controller.mongodb

import android.content.Context
import android.util.Log
import com.dam.simonmedijo.controller.Conexion
import com.dam.simonmedijo.model.Record
import com.mongodb.client.model.Sorts
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull

import java.util.Date

object MongoController : Conexion {

    private val connectionString = "mongodb+srv://dfigueroavidal_db_user:Abc123.@cluster0.i4q2u86.mongodb.net/?appName=Cluster0"
    private val dbName = "simondice"

    private const val TAG = "MongoController"

    // Variables de estado
    private var dotenv: Map<String, String>? = null
    private var mongoClient: MongoClient? = null
    private var mongoDatabase: MongoDatabase? = null

    /**
     * Función auxiliar para asegurar que todo está cargado antes de operar.
     * Usa el patrón "Lazy Init": si ya tenemos BD, la devuelve. Si no, carga env y conecta.
     */
    private fun getDatabase(context: Context): MongoDatabase {
        // 1. Si ya tenemos base de datos, devolverla inmediatamente (Rápido)
        if (mongoDatabase != null) {
            return mongoDatabase!!
        }

        // 2. Si no tenemos variables de entorno cargadas, cargarlas
        if (dotenv == null) {
            loadEnv(context)
        }

        // 3. Establecer conexión
        return setupConnection()
    }

    private fun loadEnv(context: Context) {
        try {
            val inputStream = context.assets.open(".env")
            val envContent = inputStream.bufferedReader().use { it.readText() }

            dotenv = envContent.lines()
                .filter { it.isNotBlank() && !it.startsWith("#") && it.contains("=") }
                .associate { line ->
                    val (key, value) = line.split("=", limit = 2).map { it.trim() }
                    key to value
                }

            Log.d(TAG, "✓ Variables cargadas: ${dotenv?.keys}")
        } catch (e: Exception) {
            Log.e(TAG, "✗ Error cargando .env: ${e.message}", e)
            dotenv = emptyMap() // Evitamos que siga siendo null para no reintentar infinitamente si falla
        }
    }

    private fun setupConnection(): MongoDatabase {
        try {
            Log.d("MongoController", "Iniciando conexión a MongoDB...")

            // Creamos el cliente usando la cadena LARGA (esto evita el error de JNDI/DNS)
            val client = MongoClient.create(connectionString)

            // Obtenemos la base de datos
            val database = client.getDatabase(dbName)

            Log.d("MongoController", "Conexión creada exitosamente")
            return database

        } catch (e: Exception) {
            Log.e("MongoController", "ERROR FATAL al conectar: ${e.message}")
            e.printStackTrace()
            throw e // Re-anzamos la excepción para que se vea en el log si falla
        }
    }

    // --- MÉTODOS DE LA INTERFAZ CONEXION ---

    override suspend fun obtenerRecord(context: Context): Record {
        return try {
            // Aquí pasamos el context para inicializar si hiciera falta
            val database = getDatabase(context)
            val collection = database.getCollection<Record>("records")

            // Obtiene el último record (fecha descendente)
            val result = collection.find()
                .sort(Sorts.descending("fecha"))
                .limit(1)
                .firstOrNull()

            result ?: Record(0, Date())
        } catch (e: Exception) {
            Log.e(TAG, "✗ Error obteniendo record: ${e.message}", e)
            Record(0, Date())
        }
    }

    override suspend fun actualizarRecord(
        nuevoRecord: Int,
        fecha: Date,
        context: Context
    ): Record {
        val recordObj = Record(nuevoRecord, fecha)

        return try {
            val database = getDatabase(context)
            val collection = database.getCollection<Record>("records")

            collection.insertOne(recordObj)
            Log.d(TAG, "✓ Record guardado: $nuevoRecord en $fecha")
            recordObj
        } catch (e: Exception) {
            Log.e(TAG, "✗ Error actualizando record: ${e.message}", e)
            recordObj
        }
    }

    // Opcional: Para cerrar conexión al cerrar la app
    fun close() {
        mongoClient?.close()
        mongoClient = null
        mongoDatabase = null
    }
}