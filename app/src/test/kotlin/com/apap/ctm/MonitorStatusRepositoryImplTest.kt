package com.apap.ctm

import com.apap.ctm.data.db.MonitorStatusDao
import com.apap.ctm.data.mapper.MonitorStatusMapper
import com.apap.ctm.data.model.MonitorStatusEntity
import com.apap.ctm.data.repository.MonitorStatusRepositoryImpl
import com.apap.ctm.testUtils.randomStatus
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

class MonitorStatusRepositoryImplTest {

    @MockK lateinit var dao: MonitorStatusDao
    @MockK lateinit var mapper: MonitorStatusMapper

    private lateinit var subject: MonitorStatusRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = MonitorStatusRepositoryImpl(
            dao = dao,
            mapper = mapper,
            dispatcher = Dispatchers.Unconfined
        )
    }

    @Test
    fun `should insert status`() = runTest {
        val testStatus = randomStatus()
        val expectedEntity = MonitorStatusEntity(
            start = testStatus.start,
            stop = testStatus.stop,
            duration = testStatus.duration,
            ongoing = testStatus.ongoing,
            number = testStatus.number,
            name = testStatus.name
        )
        every { mapper.toEntity(testStatus) } returns expectedEntity
        coEvery { dao.insert(any()) } just runs

        subject.insertStatus(testStatus)

        coVerify { dao.insert(
            withArg {
                assertThat(it.id).isZero
                assertThat(it.start).isEqualTo(testStatus.start)
                assertThat(it.stop).isEqualTo(testStatus.stop)
                assertThat(it.duration).isEqualTo(testStatus.duration)
                assertThat(it.ongoing).isEqualTo(testStatus.ongoing)
                assertThat(it.number).isEqualTo(testStatus.number)
                assertThat(it.name).isEqualTo(testStatus.name)
            }
        ) }
    }

    @Test
    fun `should map entity to domain model object`() = runTest {
        val testStatus = randomStatus()
        val expectedEntity = MonitorStatusEntity(
            start = testStatus.start,
            stop = testStatus.stop,
            duration = testStatus.duration,
            ongoing = testStatus.ongoing,
            number = testStatus.number,
            name = testStatus.name
        )
        every { mapper.toEntity(testStatus) } returns expectedEntity
        coEvery { dao.insert(any()) } just runs

        subject.insertStatus(testStatus)

        coVerify { dao.insert(
            withArg {
                assertThat(it.id).isZero
                assertThat(it.start).isEqualTo(testStatus.start)
                assertThat(it.stop).isEqualTo(testStatus.stop)
                assertThat(it.duration).isEqualTo(testStatus.duration)
                assertThat(it.ongoing).isEqualTo(testStatus.ongoing)
                assertThat(it.number).isEqualTo(testStatus.number)
                assertThat(it.name).isEqualTo(testStatus.name)
            }
        ) }

        coEvery { dao.getStatus() } returns expectedEntity
        coEvery { mapper.toDomain(any()) } returns testStatus

        val root = subject.getStatus()

        coVerify { dao.getStatus() }
        coVerify { mapper.toDomain(withArg {
            assertThat(it.id).isZero
            assertThat(it.start).isEqualTo(testStatus.start)
            assertThat(it.stop).isEqualTo(testStatus.stop)
            assertThat(it.duration).isEqualTo(testStatus.duration)
            assertThat(it.ongoing).isEqualTo(testStatus.ongoing)
            assertThat(it.number).isEqualTo(testStatus.number)
            assertThat(it.name).isEqualTo(testStatus.name)
        }) }

        assertThat(root).isEqualTo(testStatus)
    }
}