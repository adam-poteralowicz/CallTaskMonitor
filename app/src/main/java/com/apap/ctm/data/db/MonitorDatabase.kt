package com.apap.ctm.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.apap.ctm.data.model.MonitorLogEntity
import com.apap.ctm.data.model.MonitorLogEntryConverter
import com.apap.ctm.data.model.MonitorLogEntryEntity
import com.apap.ctm.data.model.MonitorRootEntity
import com.apap.ctm.data.model.MonitorServiceConverter
import com.apap.ctm.data.model.MonitorStatusEntity

@Database(
    entities = [
        MonitorLogEntity::class,
        MonitorLogEntryEntity::class,
        MonitorRootEntity::class,
        MonitorStatusEntity::class,
    ],
    version = 5,
    exportSchema = true
)
@TypeConverters(MonitorLogEntryConverter::class, MonitorServiceConverter::class)
abstract class MonitorDatabase : RoomDatabase() {

    companion object {
        private lateinit var instance: MonitorDatabase

        fun getInstance(context: Context) : MonitorDatabase {
            if (!::instance.isInitialized) {
                synchronized(this) { instance = createDatabase(context) }
            }
            return instance
        }

        private fun createDatabase(context: Context) : MonitorDatabase {
            val databaseBuilder = Room.databaseBuilder(context, MonitorDatabase::class.java, "monitor.db")
                .fallbackToDestructiveMigration(dropAllTables = true)
            return databaseBuilder.build()
        }
    }

    abstract fun logDao(): MonitorLogDao
    abstract fun rootDao(): MonitorRootDao
    abstract fun statusDao(): MonitorStatusDao
    abstract fun entriesDao(): MonitorLogEntryDao
}