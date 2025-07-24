package com.apap.ctm

import com.apap.ctm.data.db.MonitorLogDao
import com.apap.ctm.data.db.MonitorLogEntryDao
import com.apap.ctm.data.mapper.MonitorLogEntryMapper
import com.apap.ctm.data.mapper.MonitorLogMapper
import com.apap.ctm.data.model.MonitorLogEntity
import com.apap.ctm.data.model.MonitorLogEntryEntity
import com.apap.ctm.data.repository.MonitorLogRepositoryImpl
import com.apap.ctm.domain.model.MonitorLogEntry
import com.apap.ctm.testUtils.randomLog
import com.apap.ctm.testUtils.randomLogEntry
import io.ktor.util.reflect.instanceOf
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class MonitorLogRepositoryImplTest {

    @MockK lateinit var dao: MonitorLogDao
    @MockK lateinit var entryDao: MonitorLogEntryDao
    @MockK lateinit var mapper: MonitorLogMapper
    @MockK lateinit var entryMapper: MonitorLogEntryMapper

    private lateinit var subject: MonitorLogRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = MonitorLogRepositoryImpl(
            dao = dao,
            entryDao = entryDao,
            mapper = mapper,
            entryMapper = entryMapper,
            dispatcher = Dispatchers.Unconfined
        )
    }

    @Test
    fun `should insert log`() = runTest {
        val testLog = randomLog()
        val expectedEntity = MonitorLogEntity(
            entries = emptyList()
        )
        every { mapper.toEntity(testLog) } returns expectedEntity
        coEvery { dao.insert(any()) } just runs

        subject.insertLog(testLog)

        coVerify { dao.insert(
            withArg {
                assertThat(it.id).isZero
                assertThat(it.entries).isEmpty()
            }
        ) }
    }

    @Test
    fun `should insert log entry`() = runTest {
        val testLogEntry = randomLogEntry()
        val expectedEntity = MonitorLogEntryEntity(
            beginning = testLogEntry.beginning,
            duration = testLogEntry.duration,
            number = testLogEntry.number,
            name = testLogEntry.name,
            timesQueried = testLogEntry.timesQueried
        )
        every { entryMapper.toEntity(id = any(), entry = any()) } returns expectedEntity
        coEvery { entryDao.insert(any()) } just runs
        coEvery { entryDao.getAll() } returns listOf()

        subject.insertLogEntry(testLogEntry)

        coVerify { entryDao.insert(
            withArg {
                assertThat(it.id).isZero
                assertThat(it.beginning).isEqualTo(testLogEntry.beginning)
                assertThat(it.duration).isEqualTo(testLogEntry.duration)
                assertThat(it.number).isEqualTo(testLogEntry.number)
                assertThat(it.name).isEqualTo(testLogEntry.name)
                assertThat(it.timesQueried).isEqualTo(testLogEntry.timesQueried)
            }
        ) }
    }

    @Test
    fun `should map all entries to domain model objects`() = runTest {
        val testLogEntry = randomLogEntry()
        val expectedEntity = MonitorLogEntryEntity(
            beginning = testLogEntry.beginning,
            duration = testLogEntry.duration,
            number = testLogEntry.number,
            name = testLogEntry.name,
            timesQueried = testLogEntry.timesQueried
        )
        every { entryMapper.toEntity(id = any(), entry = any()) } returns expectedEntity
        coEvery { entryDao.insert(any()) } just runs
        coEvery { entryDao.getAll() } returns listOf()

        subject.insertLogEntry(testLogEntry)

        coVerify { entryDao.insert(
            withArg {
                assertThat(it.id).isZero
                assertThat(it.beginning).isEqualTo(testLogEntry.beginning)
                assertThat(it.duration).isEqualTo(testLogEntry.duration)
                assertThat(it.number).isEqualTo(testLogEntry.number)
                assertThat(it.name).isEqualTo(testLogEntry.name)
                assertThat(it.timesQueried).isEqualTo(testLogEntry.timesQueried)
            }
        ) }

        coEvery { entryDao.getAll() } returns listOf(expectedEntity)
        coEvery { entryMapper.toDomain(any()) } returns testLogEntry

        val entries = subject.getAllEntries()

        coVerify { entryDao.getAll() }
        coVerify { entryMapper.toDomain(withArg {
            assertThat(it.id).isZero
            assertThat(it.beginning).isEqualTo(testLogEntry.beginning)
            assertThat(it.duration).isEqualTo(testLogEntry.duration)
            assertThat(it.number).isEqualTo(testLogEntry.number)
            assertThat(it.name).isEqualTo(testLogEntry.name)
            assertThat(it.timesQueried).isEqualTo(testLogEntry.timesQueried)
        }) }

        assertThat(entries).allMatch { it.instanceOf(MonitorLogEntry::class) }
    }
}