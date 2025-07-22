package com.apap.ctm.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apap.ctm.domain.model.MonitorLog
import kotlinx.coroutines.flow.Flow

@Dao
interface MonitorLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: MonitorLog)

    @Query("DELETE FROM monitorlog")
    suspend fun delete()

    @Query("SELECT * FROM monitorLog")
    suspend fun getLog(): MonitorLog

    @Query("SELECT * FROM monitorlog")
    fun getLogFlow(): Flow<MonitorLog?>
}