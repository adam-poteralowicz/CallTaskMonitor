package com.apap.ctm

import com.apap.ctm.data.mapper.MonitorLogEntryMapper
import com.apap.ctm.data.mapper.MonitorLogMapper
import com.apap.ctm.data.model.MonitorLogEntity
import com.apap.ctm.domain.model.MonitorLog
import com.apap.ctm.testUtils.randomInt
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class MonitorLogMapperTest {

    private lateinit var subject: MonitorLogMapper
    @MockK lateinit var entryMapper: MonitorLogEntryMapper

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = MonitorLogMapper(entryMapper)
    }

    @Test
    fun `maps entity to domain model object`() {
        val entity = MonitorLogEntity(
            id = randomInt(),
            entries = emptyList()
        )
        val expectedModel = MonitorLog(
            entries = emptyList()
        )

        assertThat(subject.toDomain(entity)).isEqualTo(expectedModel)
    }

    @Test
    fun `maps domain model object to entity`() {
        val model = MonitorLog(
            entries = emptyList()
        )
        val expectedEntity = MonitorLogEntity(
            id = 0,
            entries = emptyList()
        )

        assertThat(subject.toEntity(model)).isEqualTo(expectedEntity)
    }
}