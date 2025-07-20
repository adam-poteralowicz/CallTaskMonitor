package com.apap.ctm.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.apap.ctm.domain.model.MonitorLog
import com.apap.ctm.domain.model.MonitorLogEntryConverter
import com.apap.ctm.domain.model.MonitorRoot
import com.apap.ctm.domain.model.MonitorServiceConverter
import com.apap.ctm.domain.model.MonitorStatus

@Database(
    entities = [
        MonitorLog::class,
        MonitorRoot::class,
        MonitorStatus::class
    ],
    version = 1,
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
            return databaseBuilder.build()
        }
    }

    abstract fun logDao(): MonitorLogDao
    abstract fun rootDao(): MonitorRootDao
    abstract fun statusDao(): MonitorStatusDao
}