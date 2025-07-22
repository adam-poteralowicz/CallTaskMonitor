package com.apap.ctm.presentation.viewmodel

data class MainViewState(
    val isServerStarted: Boolean = false,
    val showPermissionDialog: List<String> = emptyList()
)