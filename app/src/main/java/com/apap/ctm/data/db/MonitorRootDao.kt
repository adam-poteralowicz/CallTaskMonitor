package com.apap.ctm.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apap.ctm.data.model.MonitorRootEntity

@Dao
interface MonitorRootDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(root: MonitorRootEntity)

    @Query("DELETE FROM monitorRootEntity")
    suspend fun delete()

    @Query("SELECT * FROM monitorRootEntity ORDER BY id DESC LIMIT 1")
    suspend fun getRoot(): MonitorRootEntity?
}