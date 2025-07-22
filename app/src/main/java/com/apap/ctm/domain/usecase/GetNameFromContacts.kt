package com.apap.ctm.domain.usecase

import android.database.Cursor
import android.provider.ContactsContract
import javax.inject.Inject

class GetNameFromContacts @Inject constructor() {

    operator fun invoke(cursor: Cursor) : String {

        with(cursor) {
            val nameColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)
            return if (moveToFirst() && nameColumnIndex >= 0) {
                val contactName = cursor.getString(nameColumnIndex)
                contactName
            } else {
                ""
            }
        }
    }
}