package com.kotlin.mvvm.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kotlin.mvvm.repository.model.TodoModel

/**
 * Developed by Waheed on 20,June,2021
 *
 * App Database
 * Define all entities and access doa's here/ Each entity is a table.
 */
@Database(
    entities = [TodoModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao
}