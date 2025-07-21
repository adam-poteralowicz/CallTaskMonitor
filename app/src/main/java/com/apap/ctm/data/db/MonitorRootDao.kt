package com.apap.ctm.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apap.ctm.domain.model.MonitorRoot
import kotlinx.coroutines.flow.Flow

@Dao
interface MonitorRootDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(root: MonitorRoot)

    @Delete
    suspend fun delete(root: MonitorRoot)

    @Query("SELECT * FROM monitorRoot LIMIT 1")
    fun getRoot(): Flow<MonitorRoot>
}