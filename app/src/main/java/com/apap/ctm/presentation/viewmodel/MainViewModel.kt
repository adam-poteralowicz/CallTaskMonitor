package com.apap.ctm.presentation.viewmodel

import android.database.Cursor
import android.provider.CallLog
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apap.ctm.R
import com.apap.ctm.util.ResourcesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val resourcesProvider: ResourcesProvider
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
        with(cursor) {
            val dateColumnIndex = getColumnIndex(CallLog.Calls.DATE)
            val durationColumnIndex = getColumnIndex(CallLog.Calls.DURATION)
            val numberColumnIndex = getColumnIndex(CallLog.Calls.NUMBER)
            val typeColumnIndex = getColumnIndex(CallLog.Calls.TYPE)
            while(moveToNext()) {
                val itemDate = Date(getLong(dateColumnIndex))
                val itemDuration = getString(durationColumnIndex)
                val itemNumber = getString(numberColumnIndex)
                val itemType = getString(typeColumnIndex)

                val callType = when(Integer.parseInt(itemType)) {
                    CallLog.Calls.OUTGOING_TYPE -> resourcesProvider.getString(R.string.type_outgoing)
                    CallLog.Calls.INCOMING_TYPE -> resourcesProvider.getString(R.string.type_incoming)
                    CallLog.Calls.MISSED_TYPE -> resourcesProvider.getString(R.string.type_missed)
                    else -> null
                }
                Log.d("Call Log item", "Date: $itemDate, type: $callType, number: $itemNumber, duration: $itemDuration")
            }
            close()
        }
    }
}