package com.apap.ctm

import android.database.Cursor
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.apap.ctm.data.network.CallTaskController
import com.apap.ctm.data.repository.MonitorLogRepository
import com.apap.ctm.data.repository.MonitorRootRepository
import com.apap.ctm.data.repository.MonitorStatusRepository
import com.apap.ctm.domain.model.MonitorLogEntry
import com.apap.ctm.domain.model.MonitorStatus
import com.apap.ctm.domain.usecase.GetNameFromContacts
import com.apap.ctm.testUtils.MainDispatcherRule
import com.apap.ctm.testUtils.randomLog
import com.apap.ctm.testUtils.randomLogEntry
import com.apap.ctm.testUtils.randomRoot
import com.apap.ctm.testUtils.randomService
import com.apap.ctm.testUtils.randomStatus
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CallTaskControllerTest {

    @get:Rule val mainDispatcherRule = MainDispatcherRule()
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK private lateinit var logRepository: MonitorLogRepository
    @MockK private lateinit var statusRepository: MonitorStatusRepository
    @MockK private lateinit var rootRepository: MonitorRootRepository

    @MockK private lateinit var getNameFromContacts: GetNameFromContacts

    private lateinit var subject: CallTaskController

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = CallTaskController(
            logRepository = logRepository,
            rootRepository = rootRepository,
            statusRepository = statusRepository,
            getNameFromContacts = getNameFromContacts
        )
    }

    @Test
    fun `should return expected status on start call`() = runTest {
        val mockedCursor = mockk<Cursor>()
        mockkStatic(Cursor::class)
        val testNumber = "501222333"
        val expectedName = "<UNKNOWN>"
        val expectedStatus = randomStatus(number = testNumber, ongoing = true, name = expectedName)

        every { getNameFromContacts.invoke(mockedCursor) } returns expectedName
        coEvery { statusRepository.insertStatus(any<MonitorStatus>()) } just runs

        subject.startCall(mockedCursor, testNumber)
        verify { getNameFromContacts.invoke(any<Cursor>()) }
        coVerify(exactly = 1) { statusRepository.insertStatus(
            withArg {
                assertThat(it.number).isEqualTo(expectedStatus.number)
                assertThat(it.ongoing).isEqualTo(expectedStatus.ongoing)
                assertThat(it.name).isEqualTo(expectedStatus.name)
            }
        ) }
    }

    @Test
    fun `should change call status on stop call`() = runTest {
        val testNumber = "501222333"
        val testName = "<UNKNOWN>"
        val testStatus = randomStatus(number = testNumber, ongoing = true, name = testName)

        coEvery { statusRepository.getStatus() } returns testStatus
        coEvery { statusRepository.insertStatus(any<MonitorStatus>()) } just runs
        subject.stopCall()

        coVerify(exactly = 1) { statusRepository.getStatus() }
        coVerify { statusRepository.insertStatus(
            withArg {
                assertThat(it.ongoing).isFalse
                assertThat(it.duration).isNotZero
            }
        ) }
    }

    @Test
    fun `should not add log entry when call status is null`() = runTest {
        coEvery { statusRepository.getStatus() } returns null
        val mockedCursor = mockk<Cursor>()
        mockkStatic(Cursor::class)

        subject.addLogEntry(mockedCursor)

        coVerify(exactly = 1) { statusRepository.getStatus() }
        coVerify(exactly = 0) { logRepository.getLog() }
        coVerify(exactly = 0) { logRepository.insertLogEntry(any()) }
        coVerify(exactly = 0) { logRepository.getAllEntries() }
        coVerify(exactly = 0) { logRepository.insertLog(any()) }
        coVerify(exactly = 0) { getNameFromContacts.invoke(any()) }
    }

    @Test
    fun `should add log entry when call status is not null`() = runTest {
        val testNumber = "501222333"
        val testName = "<UNKNOWN>"
        val testStatus = randomStatus(number = testNumber, ongoing = true, name = testName)
        val mockedCursor = mockk<Cursor>()
        mockkStatic(Cursor::class)
        val testLog = randomLog()

        coEvery { statusRepository.getStatus() } returns testStatus
        coEvery { logRepository.getLog() } returns testLog
        coEvery { getNameFromContacts.invoke(mockedCursor) } returns testName
        coEvery { logRepository.insertLogEntry(any()) } just runs
        coEvery { logRepository.getAllEntries() } returns emptyList()
        coEvery { logRepository.insertLog(any()) } just runs

        subject.addLogEntry(mockedCursor)

        coVerify(exactly = 1) { statusRepository.getStatus() }
        coVerify(exactly = 1) { logRepository.getLog() }
        coVerify(exactly = 1) { getNameFromContacts.invoke(mockedCursor) }
        coVerify(exactly = 1) {
            logRepository.insertLogEntry(
                withArg {
                    assertThat(it.name).isEqualTo(testName)
                    assertThat(it.beginning).isEqualTo(testStatus.start)
                    assertThat(it.duration).isEqualTo(testStatus.duration)
                    assertThat(it.number).isEqualTo(testStatus.number)
                    assertThat(it.timesQueried).isZero
                }
            )
        }
        coVerify(exactly = 1) { logRepository.getAllEntries() }
        coVerify(exactly = 1) {
            logRepository.insertLog(
                withArg {
                    assertThat(it.entries).isNotEqualTo(emptyList<MonitorLogEntry>())
                }
            )
        }
    }

    @Test
    fun `should update log entry`() = runTest {
        coEvery { logRepository.insertLogEntry(any()) } just runs
        val testLogEntry = randomLogEntry()

        subject.updateLogEntry(testLogEntry)
        subject.updateLogEntry(testLogEntry.copy(timesQueried = 1))

        coVerify { logRepository.insertLogEntry(testLogEntry) }
        coVerify { logRepository.insertLogEntry(
            withArg {
                assertThat(it.timesQueried).isOne
            }
        ) }
    }

    @Test
    fun `should add services if not available`() = runTest {
        val testServices = listOf(randomService(), randomService())
        coEvery { rootRepository.getRoot() } returns null
        coEvery { rootRepository.insertRoot(any()) } just runs

        subject.addServices(testServices)

        coVerify(exactly = 1) { rootRepository.getRoot() }
        coVerify(exactly = 1) { rootRepository.insertRoot(
            withArg {
                assertThat(it.services).isEqualTo(testServices)
            }
        ) }
    }

    @Test
    fun `should not add services if they are already available`() = runTest {
        val testServices = listOf(randomService(), randomService())
        coEvery { rootRepository.getRoot() } returns randomRoot()
        coEvery { rootRepository.insertRoot(any()) } just runs

        subject.addServices(testServices)

        coVerify(exactly = 1) { rootRepository.getRoot() }
        coVerify(exactly = 0) { rootRepository.insertRoot(any()) }
    }

    @Test
    fun `should insert log`() = runTest {
        val testLog = randomLog()
        coEvery { logRepository.insertLog(any()) } just runs

        subject.insertLog(testLog)

        coVerify { logRepository.insertLog(testLog) }
    }

    @Test
    fun `should delete root and status tables`() = runTest {
        coEvery { rootRepository.deleteRoot() } just runs
        coEvery { statusRepository.deleteStatus() } just runs

        subject.clearAllTables()

        coVerify(exactly = 1) { rootRepository.deleteRoot() }
        coVerify(exactly = 1) { statusRepository.deleteStatus() }
    }
}