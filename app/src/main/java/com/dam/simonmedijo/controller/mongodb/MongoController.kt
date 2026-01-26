package com.dam.simonmedijo.controller.mongodb

import android.content.Context
import com.dam.simonmedijo.controller.Conexion
import com.dam.simonmedijo.model.Record
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.runBlocking
import java.util.Date
import com.mongodb.client.model.Sorts
import kotlinx.coroutines.flow.toList


object MongoController: Conexion {
    private var dotenv: Map<String, String>? = null

    private fun loadEnv(context: Context) {
        if (dotenv == null) {
            try {
                val inputStream = context.assets.open(".env")
                val envContent = inputStream.bufferedReader().use { it.readText() }
                dotenv = envContent.lines()
                    .filter { it.isNotBlank() && !it.startsWith("#") }
                    .associate { line ->
                        val (key, value) = line.split("=", limit = 2)
                        key.trim() to value.trim()
                    }
            } catch (e: Exception) {
                android.util.Log.e("MongoController", "Error loading .env file", e)
                dotenv = emptyMap()
            }
        }
    }

    override fun obtenerRecord(context: Context): Record {
        var record = Record(0, Date())
        loadEnv(context)
        runBlocking {
            try {
                val mongodb = setupConnection()
                val collection = mongodb.getCollection<Record>("records")
                val results = collection.find().sort(Sorts.descending("record")).limit(1).toList()
                record = results.firstOrNull() ?: record
            } catch (e: Exception) {
                android.util.Log.e("MongoController", "Error obtaining record", e)
            }
        }
        return record
    }

    suspend fun obtenerRecordSuspend(context: Context): Record {
        var record = Record(0, Date())
        loadEnv(context)
        return try {
            val mongodb = setupConnection()
            val collection = mongodb.getCollection<Record>("records")
            val results = collection.find().sort(Sorts.descending("record")).limit(1).toList()
            results.firstOrNull() ?: record
        } catch (e: Exception) {
            android.util.Log.e("MongoController", "Error obtaining record", e)
            record
        }
    }

    override fun actualizarRecord(
        nuevoRecord: Int,
        fecha: Date,
        context: Context
    ): Record {
        val recordObj = Record(nuevoRecord, fecha)
        loadEnv(context)

        runBlocking {
            try {
                val mongodb = setupConnection()
                val collection = mongodb.getCollection<Record>("records")
                collection.insertOne(recordObj)
            } catch (e: Exception) {
                android.util.Log.e("MongoController", "Error updating record", e)
            }
        }
        return recordObj
    }

    suspend fun actualizarRecordSuspend(
        nuevoRecord: Int,
        fecha: Date,
        context: Context
    ): Record {
        val recordObj = Record(nuevoRecord, fecha)
        loadEnv(context)

        return try {
            val mongodb = setupConnection()
            val collection = mongodb.getCollection<Record>("records")
            collection.insertOne(recordObj)
            recordObj
        } catch (e: Exception) {
            android.util.Log.e("MongoController", "Error updating record", e)
            recordObj
        }
    }

    suspend fun setupConnection(
        database: String? = null
    ) : MongoDatabase {
        val mongoUri = dotenv?.get("MONGO_URI")
            ?: throw IllegalStateException("MONGO_URI not found in .env file")
        val mongoDatabase = database ?: dotenv?.get("MONGO_DATABASE")
            ?: throw IllegalStateException("MONGO_DATABASE not found in .env file")

        val client = MongoClient.create(mongoUri)
        return client.getDatabase(mongoDatabase)
    }


}