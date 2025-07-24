package com.apap.ctm.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apap.ctm.data.model.MonitorLogEntryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Dao
interface MonitorLogEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: MonitorLogEntryEntity)

    @Query("DELETE FROM monitorLogEntryEntity")
    suspend fun delete()

    @Query("SELECT * FROM monitorLogEntryEntity")
    suspend fun getAll(): List<MonitorLogEntryEntity>

    @Query("SELECT * FROM monitorLogEntryEntity ORDER BY beginning DESC")
    fun entriesFlow(): Flow<List<MonitorLogEntryEntity>> = emptyFlow()
}