package com.apap.ctm.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apap.ctm.data.model.MonitorStatusEntity

@Dao
interface MonitorStatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(status: MonitorStatusEntity)

    @Query("UPDATE monitorStatusEntity SET ongoing=:ongoing WHERE number=:number")
    suspend fun update(number: String, ongoing: Boolean)

    @Query("DELETE FROM monitorStatusEntity")
    suspend fun delete()

    @Query("SELECT * FROM monitorStatusEntity ORDER BY id DESC LIMIT 1")
    fun getStatus(): MonitorStatusEntity?
}