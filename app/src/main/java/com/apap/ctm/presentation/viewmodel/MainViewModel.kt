package com.apap.ctm.presentation.viewmodel

import android.database.Cursor
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

    private val _isServerOnline = MutableStateFlow(false)
    val isServerOnline = _isServerOnline.asStateFlow()

    private val _toggleServerFlow = MutableSharedFlow<Boolean>()
    val toggleServerFlow = _toggleServerFlow.asSharedFlow()

    fun onServerToggled(shouldStart: Boolean) = viewModelScope.launch {
        _toggleServerFlow.emit(shouldStart)
        _isServerOnline.emit(shouldStart)
    }

    fun onCallLogFetched(cursor: Cursor?) {
        cursor ?: return
        fetchCallLog.invoke(cursor)
    }
}