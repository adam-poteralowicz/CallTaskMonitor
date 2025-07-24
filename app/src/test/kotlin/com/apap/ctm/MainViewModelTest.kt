package com.apap.ctm

import android.Manifest
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.apap.ctm.data.repository.MonitorLogRepository
import com.apap.ctm.presentation.viewmodel.MainViewModel
import com.apap.ctm.presentation.viewmodel.MissingPermission
import com.apap.ctm.testUtils.MainDispatcherRule
import com.apap.ctm.testUtils.randomLogEntry
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule val mainDispatcherRule = MainDispatcherRule()
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK private lateinit var logRepository: MonitorLogRepository

    private lateinit var subject: MainViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = MainViewModel(logRepository)
    }

    @Test
    fun `should emit toggleServerFlow true on server button toggled ON`() = runTest {
        subject.toggleServerFlow.test {
            subject.onServerToggled(true)
            assertThat(expectMostRecentItem()).isTrue
            expectNoEvents()
        }
    }

    @Test
    fun `should show permission request dialog on missing READ_CALL_LOG permission`() = runTest {
        subject.state.test {
            subject.onServerToggled(true)
            val expectedMissionPermission = MissingPermission.READ_CALL_LOG
            subject.onPermissionsNotGranted(listOf(Manifest.permission.READ_CALL_LOG))

            assertThat(expectMostRecentItem().showPermissionDialog).isEqualTo(listOf(expectedMissionPermission))
        }
    }

    @Test
    fun `should show permission request dialog on missing READ_CONTACTS permission`() = runTest {
        subject.state.test {
            subject.onServerToggled(true)
            val expectedMissionPermission = MissingPermission.READ_CONTACTS
            subject.onPermissionsNotGranted(listOf(Manifest.permission.READ_CONTACTS))

            assertThat(expectMostRecentItem().showPermissionDialog).isEqualTo(listOf(expectedMissionPermission))
        }
    }

    @Test
    fun `should show permission request dialog on missing READ_PHONE_STATE permission`() = runTest {
        subject.state.test {
            subject.onServerToggled(true)
            val expectedMissionPermission = MissingPermission.READ_PHONE_STATE
            subject.onPermissionsNotGranted(listOf(Manifest.permission.READ_PHONE_STATE))

            assertThat(expectMostRecentItem().showPermissionDialog).isEqualTo(listOf(expectedMissionPermission))
        }
    }

    @Test
    fun `should retrieve log entries on logs refresh requested`() = runTest {
        val testLogEntry = randomLogEntry(0)
        coEvery { logRepository.getAllEntries() } returns listOf(testLogEntry)
        subject.state.test {
            assertThat(expectMostRecentItem().entries).isEmpty()
            subject.refreshLogs()
            assertThat(expectMostRecentItem().entries).containsExactly(testLogEntry)
        }
    }
}