package com.apap.ctm.presentation.viewmodel

import com.apap.ctm.domain.model.MonitorLogEntry

data class MainViewState(
    val isServerStarted: Boolean = false,
    val showPermissionDialog: List<MissingPermission> = emptyList(),
    val entries: List<MonitorLogEntry> = emptyList()
)