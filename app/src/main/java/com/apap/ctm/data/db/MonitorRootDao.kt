package com.apap.ctm.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apap.ctm.domain.model.MonitorRoot

@Dao
interface MonitorRootDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(root: MonitorRoot)

    @Query("DELETE FROM monitorRoot")
    suspend fun delete()

    @Query("SELECT * FROM monitorRoot ORDER BY id DESC LIMIT 1")
    suspend fun getRoot(): MonitorRoot?
}