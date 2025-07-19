package com.apap.ctm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _isServerOnline = MutableStateFlow(false)
    val isServerOnline = _isServerOnline.asStateFlow()

    private val _toggleServerFlow = MutableSharedFlow<Boolean>()
    val toggleServerFlow = _toggleServerFlow.asSharedFlow()

    fun onServerOn() = viewModelScope.launch {
        _toggleServerFlow.emit(true)
        _isServerOnline.emit(true)
    }

    fun onServerOff() = viewModelScope.launch {
        _toggleServerFlow.emit(false)
        _isServerOnline.emit(false)
    }
}