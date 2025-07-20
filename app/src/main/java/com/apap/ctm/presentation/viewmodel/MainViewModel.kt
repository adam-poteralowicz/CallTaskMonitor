package com.apap.ctm.presentation.viewmodel

import android.database.Cursor
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apap.ctm.domain.usecase.FetchCallLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchCallLog: FetchCallLog
) : ViewModel() {

    private val _serverStartedFlow = MutableStateFlow(false)
    val serverStartedFlow = _serverStartedFlow.asStateFlow()

    private val _toggleServerFlow = MutableSharedFlow<Boolean>()
    val toggleServerFlow = _toggleServerFlow.asSharedFlow()

    private val _showDialogFlow = MutableStateFlow<List<String>>(emptyList())
    val showDialogFlow = _showDialogFlow.asStateFlow()

    fun onServerToggled(shouldStart: Boolean) = viewModelScope.launch {
        _toggleServerFlow.emit(shouldStart)
    }

    fun onServerStarted() = viewModelScope.launch {
        _serverStartedFlow.emit(true)
    }

    fun onServerStopped() = viewModelScope.launch {
        _serverStartedFlow.emit(false)
    }

    fun onPermissionsNotGranted(permissions: List<String>) = viewModelScope.launch {
        if (permissions.isNotEmpty()) {
            permissions.onEach { Log.e("Permissions", it) }
            Log.e("permissions", permissions.toString())
            val permission = when (permissions.distinct().first()) {
                "android.permission.READ_CALL_LOG" -> "READ CALL LOG"
                "android.permission.READ_CONTACTS" -> "READ CONTACTS"
                "android.permission.READ_PHONE_STATE" -> "READ PHONE STATE"
                else -> ""
            }
            _showDialogFlow.emit(listOf(permission))
        } else {
            _showDialogFlow.emit(emptyList())
        }
    }

    fun onCallLogFetched(cursor: Cursor?) {
        cursor ?: return
        fetchCallLog.invoke(cursor)
    }
}