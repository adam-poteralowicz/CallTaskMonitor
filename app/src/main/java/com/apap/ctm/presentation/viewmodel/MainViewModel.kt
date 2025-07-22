package com.apap.ctm.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apap.ctm.data.repository.MonitorLogRepository
import com.apap.ctm.domain.model.MonitorLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(logRepository: MonitorLogRepository) : ViewModel() {

    private val _state = MutableStateFlow(MainViewState())
    val state = _state.asStateFlow()

    private val _toggleServerFlow = MutableSharedFlow<Boolean>()
    val toggleServerFlow = _toggleServerFlow.asSharedFlow()

    val log: StateFlow<MonitorLog?> = logRepository.getLogFlow()
        .catch { exception -> exception.localizedMessage?.let { Log.e(javaClass.simpleName, it) } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, MonitorLog())

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
}