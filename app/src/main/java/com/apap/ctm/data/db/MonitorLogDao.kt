package com.apap.ctm.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apap.ctm.domain.model.MonitorLog
import kotlinx.coroutines.flow.Flow

@Dao
interface MonitorLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: MonitorLog)

    @Delete
    suspend fun delete(log: MonitorLog)

    @Query("SELECT * FROM monitorLog")
    fun getAll(): Flow<List<MonitorLog>>
}