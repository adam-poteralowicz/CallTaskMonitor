package com.apap.ctm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apap.ctm.data.repository.MonitorLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val logRepository: MonitorLogRepository) : ViewModel() {

    private val _state = MutableStateFlow(MainViewState())
    val state = _state.asStateFlow()

    private val _toggleServerFlow = MutableSharedFlow<Boolean>()
    val toggleServerFlow = _toggleServerFlow.asSharedFlow()

    fun onServerToggled(shouldStart: Boolean) = viewModelScope.launch {
        _toggleServerFlow.emit(shouldStart)
    }

    fun onServerStarted() = viewModelScope.launch {
        _state.emit(_state.value.copy(isServerStarted = true))
    }

    fun onServerStopped() = viewModelScope.launch {
        _state.emit(_state.value.copy(isServerStarted = false))
    }

    fun onPermissionsNotGranted(permissions: List<String>) = viewModelScope.launch {
        if (permissions.isNotEmpty()) {
            val permission = when (permissions.distinct().first()) {
                "android.permission.READ_CALL_LOG" -> "READ CALL LOG"
                "android.permission.READ_CONTACTS" -> "READ CONTACTS"
                "android.permission.READ_PHONE_STATE" -> "READ PHONE STATE"
                else -> ""
            }
            _state.emit(_state.value.copy(showPermissionDialog = listOf(permission)))
        } else {
            _state.emit(_state.value.copy(showPermissionDialog = emptyList()))
        }
    }

    fun refreshLogs() = viewModelScope.launch {
        _state.emit(_state.value.copy(entries = logRepository.getAllEntries()))
    }
}