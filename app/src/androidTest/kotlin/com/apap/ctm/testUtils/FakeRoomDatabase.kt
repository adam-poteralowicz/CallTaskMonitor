package com.apap.ctm.testUtils

import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase

class FakeRoomDatabase() : RoomDatabase() {

    override fun clearAllTables() {
        return
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        return InvalidationTracker(this)
    }

}