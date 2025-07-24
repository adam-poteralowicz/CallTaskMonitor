package com.apap.ctm.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.apap.ctm.data.db.BaseListConverter

class MonitorLogEntryConverter : BaseListConverter<MonitorLogEntryEntity>(MonitorLogEntryEntity::class.java)

@Entity
data class MonitorLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val entries: List<MonitorLogEntryEntity>? = emptyList()
)

@Entity
data class MonitorLogEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "beginning") val beginning: String?,
    @ColumnInfo(name = "duration") val duration: Int = 0,
    @ColumnInfo(name = "number") val number: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "timesQueried") val timesQueried: Int?
) {
    override fun toString(): String = "$beginning::$duration::$number::$name::$timesQueried"
}