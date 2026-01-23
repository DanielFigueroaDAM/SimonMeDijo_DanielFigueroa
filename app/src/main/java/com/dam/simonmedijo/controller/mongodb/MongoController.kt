package com.dam.simonmedijo.controller.mongodb

import android.content.Context
import com.dam.simonmedijo.controller.Conexion
import com.dam.simonmedijo.model.Record
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Document
import java.util.Date

object MongoController: Conexion {
    override fun obtenerRecord(context: Context): Record {



    }

    override fun actualizarRecord(
        nuevoRecord: Int,
        fecha: Date,
        context: Context
    ): Record {
        val nuevoRecord = Record(nuevoRecord, fecha)

        runBlocking {
            val mongodb = setupConnection()
            val collection = mongodb.getCollection<Record>("records")
            val doc = nuevoRecord
            collection.insertOne(doc)
        }
        return nuevoRecord
    }

    suspend fun setupConnection(
        database: String? = null
    ) : MongoDatabase {
         val dotenv = dotenv {
             ignoreIfMissing = true
         }
        val client = MongoClient.create(dotenv["MONGO_URI"])
        val mongodb = client.getDatabase(database ?: dotenv["MONGO_DATABASE"])
        return mongodb
    }


}