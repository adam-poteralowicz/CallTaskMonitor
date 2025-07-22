package com.apap.ctm.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apap.ctm.domain.model.MonitorStatus

@Dao
interface MonitorStatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(status: MonitorStatus)

    @Query("UPDATE monitorStatus SET ongoing=:ongoing WHERE number=:number")
    suspend fun update(number: String, ongoing: Boolean)

    @Query("DELETE FROM monitorStatus")
    suspend fun delete()

    @Query("SELECT * FROM monitorStatus ORDER BY id DESC LIMIT 1")
    fun getStatus(): MonitorStatus
}