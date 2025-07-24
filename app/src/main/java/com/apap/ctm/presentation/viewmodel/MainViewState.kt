package com.apap.ctm.presentation.viewmodel

import com.apap.ctm.domain.model.MonitorLogEntry

data class MainViewState(
    val isServerStarted: Boolean = false,
    val showPermissionDialog: List<String> = emptyList(),
    val entries: List<MonitorLogEntry> = emptyList()
)