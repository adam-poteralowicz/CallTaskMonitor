package com.apap.ctm.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apap.ctm.domain.model.MonitorLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MonitorLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: MonitorLogEntity)

    @Query("DELETE FROM monitorLogEntity")
    suspend fun delete()

    @Query("SELECT * FROM monitorLogEntity")
    suspend fun getLog(): MonitorLogEntity

    @Query("SELECT * FROM monitorlogEntity")
    fun getLogFlow(): Flow<MonitorLogEntity?>
}