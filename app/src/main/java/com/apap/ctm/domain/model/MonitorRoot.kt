package com.apap.ctm.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.apap.ctm.data.db.BaseListConverter
import com.apap.ctm.data.db.DateTimeConverter
import org.joda.time.DateTime

class MonitorServiceConverter : BaseListConverter<MonitorService>(MonitorService::class.java)

@Entity
data class MonitorRoot(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("start") @TypeConverters(DateTimeConverter::class) val start: DateTime?,
    @ColumnInfo("services") val services: List<MonitorService>? = emptyList()
)

@Entity
data class MonitorService(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "uri") val uri: String?
)