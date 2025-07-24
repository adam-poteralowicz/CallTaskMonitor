package com.apap.ctm

import com.apap.ctm.data.db.MonitorRootDao
import com.apap.ctm.data.mapper.MonitorRootMapper
import com.apap.ctm.data.model.MonitorRootEntity
import com.apap.ctm.data.repository.MonitorRootRepositoryImpl
import com.apap.ctm.testUtils.randomRoot
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

class MonitorRootRepositoryImplTest {

    @MockK lateinit var dao: MonitorRootDao
    @MockK lateinit var mapper: MonitorRootMapper

    private lateinit var subject: MonitorRootRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = MonitorRootRepositoryImpl(
            dao = dao,
            mapper = mapper,
            dispatcher = Dispatchers.Unconfined
        )
    }

    @Test
    fun `should insert root`() = runTest {
        val testRoot = randomRoot()
        val expectedEntity = MonitorRootEntity(
            start = testRoot.start,
            services = emptyList()
        )
        every { mapper.toEntity(testRoot) } returns expectedEntity
        coEvery { dao.insert(any()) } just runs

        subject.insertRoot(testRoot)

        coVerify { dao.insert(
            withArg {
                assertThat(it.id).isZero
                assertThat(it.start).isEqualTo(testRoot.start)
                assertThat(it.services).isEmpty()
            }
        ) }
    }

    @Test
    fun `should map entity to domain model object`() = runTest {
        val testRoot = randomRoot()
        val expectedEntity = MonitorRootEntity(
            start = testRoot.start,
            services = emptyList()
        )
        every { mapper.toEntity(testRoot) } returns expectedEntity
        coEvery { dao.insert(any()) } just runs

        subject.insertRoot(testRoot)

        coVerify { dao.insert(
            withArg {
                assertThat(it.id).isZero
                assertThat(it.start).isEqualTo(testRoot.start)
                assertThat(it.services).isEmpty()
            }
        ) }

        coEvery { dao.getRoot() } returns expectedEntity
        coEvery { mapper.toDomain(any()) } returns testRoot

        val root = subject.getRoot()

        coVerify { dao.getRoot() }
        coVerify { mapper.toDomain(withArg {
            assertThat(it.id).isZero
            assertThat(it.start).isEqualTo(testRoot.start)
            assertThat(it.services).isEmpty()
        }) }

        assertThat(root).isEqualTo(testRoot)
    }
}