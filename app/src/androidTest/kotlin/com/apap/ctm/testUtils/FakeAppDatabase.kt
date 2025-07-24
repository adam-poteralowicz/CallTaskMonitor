package com.apap.ctm.testUtils

import androidx.room.InvalidationTracker
import com.apap.ctm.data.db.MonitorDatabase
import com.apap.ctm.data.db.MonitorLogDao
import com.apap.ctm.data.db.MonitorLogDao_Impl
import com.apap.ctm.data.db.MonitorLogEntryDao
import com.apap.ctm.data.db.MonitorLogEntryDao_Impl
import com.apap.ctm.data.db.MonitorRootDao
import com.apap.ctm.data.db.MonitorRootDao_Impl
import com.apap.ctm.data.db.MonitorStatusDao
import com.apap.ctm.data.db.MonitorStatusDao_Impl

class FakeAppDatabase : MonitorDatabase() {

    private val fakeRoomDatabase = FakeRoomDatabase()

    override fun clearAllTables() {
        return
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        return InvalidationTracker(this)
    }

    override fun logDao(): MonitorLogDao = MonitorLogDao_Impl(fakeRoomDatabase)
    override fun rootDao(): MonitorRootDao = MonitorRootDao_Impl(fakeRoomDatabase)
    override fun statusDao(): MonitorStatusDao = MonitorStatusDao_Impl(fakeRoomDatabase)
    override fun entriesDao(): MonitorLogEntryDao = MonitorLogEntryDao_Impl(fakeRoomDatabase)

}