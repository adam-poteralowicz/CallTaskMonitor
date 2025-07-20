package com.apap.ctm.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MonitorStatus(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "ongoing") val ongoing: String?,
    @ColumnInfo(name = "number") val number: String?,
    @ColumnInfo(name = "name") val name: String?
)