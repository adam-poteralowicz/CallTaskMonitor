package com.apap.ctm.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.apap.ctm.data.db.BaseListConverter
import com.apap.ctm.data.db.DateTimeConverter
import org.joda.time.DateTime

class MonitorLogEntryConverter : BaseListConverter<MonitorLogEntry>(MonitorLogEntry::class.java)

@Entity
data class MonitorLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val entries: List<MonitorLogEntry>? = emptyList()
)

@Entity
data class MonitorLogEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "beginning") @TypeConverters(DateTimeConverter::class) val beginning: DateTime?,
    @ColumnInfo(name = "duration") val duration: String?,
    @ColumnInfo(name = "number") val number: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "timesQueried") val timesQueried: Int?
) {
    override fun toString(): String = "$beginning::$duration::$number::$name::$timesQueried"
}