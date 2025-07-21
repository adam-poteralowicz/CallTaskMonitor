package com.apap.ctm.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.apap.ctm.data.db.DateTimeConverter
import org.joda.time.DateTime

@Entity
data class MonitorStatus(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "start") @TypeConverters(DateTimeConverter::class) val start: DateTime,
    @ColumnInfo(name = "stop") @TypeConverters(DateTimeConverter::class) val stop: DateTime,
    @ColumnInfo(name = "duration") val duration: Int = 0,
    @ColumnInfo(name = "ongoing") val ongoing: Boolean?,
    @ColumnInfo(name = "number") val number: String?,
    @ColumnInfo(name = "name") val name: String?
)