package com.apap.ctm.domain.usecase

import android.database.Cursor
import android.provider.ContactsContract
import javax.inject.Inject

class GetNameFromContacts @Inject constructor() {

    private companion object {
        const val UNKNOWN = "<UNKNOWN>"
    }

    operator fun invoke(cursor: Cursor) : String {
        val nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        return if (cursor.moveToFirst()) {
            cursor.getString(nameColumnIndex)
        } else UNKNOWN
    }
}