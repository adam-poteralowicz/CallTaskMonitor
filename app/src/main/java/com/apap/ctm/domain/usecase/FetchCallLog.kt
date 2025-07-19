package com.apap.ctm.domain.usecase

import android.database.Cursor
import android.provider.CallLog
import android.util.Log
import com.apap.ctm.domain.model.CallLogItem
import com.apap.ctm.domain.model.CallType
import java.util.Date
import javax.inject.Inject

class FetchCallLog @Inject constructor() {

    operator fun invoke(cursor: Cursor) : List<CallLogItem> {
        val results = mutableListOf<CallLogItem>()
        with(cursor) {
            val dateColumnIndex = getColumnIndex(CallLog.Calls.DATE)
            val durationColumnIndex = getColumnIndex(CallLog.Calls.DURATION)
            val numberColumnIndex = getColumnIndex(CallLog.Calls.NUMBER)
            val typeColumnIndex = getColumnIndex(CallLog.Calls.TYPE)
            val nameColumnIndex = getColumnIndex(CallLog.Calls.CACHED_NAME)
            while(moveToNext()) {
                val itemName = getString(nameColumnIndex)
                val itemDate = Date(getLong(dateColumnIndex))
                val itemDuration = getString(durationColumnIndex)
                val itemNumber = getString(numberColumnIndex)
                val itemType = getString(typeColumnIndex)

                val callType = when(Integer.parseInt(itemType)) {
                    CallLog.Calls.OUTGOING_TYPE -> CallType.OUTGOING
                    CallLog.Calls.INCOMING_TYPE -> CallType.INCOMING
                    CallLog.Calls.MISSED_TYPE -> CallType.MISSED
                    else -> null
                }

                callType?.let {
                    results += CallLogItem(itemName, itemDate, itemDuration, itemNumber, it)
                }
                Log.d("Call Log item", "Name: $itemName, date: $itemDate, type: $callType, number: $itemNumber, duration: $itemDuration")
            }
            close()
        }
        return results
    }
}