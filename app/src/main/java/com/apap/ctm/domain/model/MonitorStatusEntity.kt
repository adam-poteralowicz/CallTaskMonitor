package com.apap.ctm.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MonitorStatusEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "start") val start: String,
    @ColumnInfo(name = "stop") val stop: String,
    @ColumnInfo(name = "duration") val duration: Int = 0,
    @ColumnInfo(name = "ongoing") val ongoing: Boolean?,
    @ColumnInfo(name = "number") val number: String?,
    @ColumnInfo(name = "name") val name: String?
)