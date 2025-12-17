package com.dam.simonmedijo.controller.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dam.simonmedijo.model.Record

@Database(entities = [RecordEntity::class], version = 1) //Marcamos la clase como una base de datos de Room
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
}
