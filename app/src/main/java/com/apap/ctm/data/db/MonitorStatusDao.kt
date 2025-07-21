package com.apap.ctm.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apap.ctm.domain.model.MonitorStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MonitorStatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(status: MonitorStatus)

    @Delete
    suspend fun delete(status: MonitorStatus)

    @Query("SELECT * FROM monitorStatus LIMIT 1")
    fun getStatus(): Flow<MonitorStatus>
}