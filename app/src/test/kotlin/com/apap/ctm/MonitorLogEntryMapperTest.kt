package com.apap.ctm

import com.apap.ctm.data.mapper.MonitorLogEntryMapper
import com.apap.ctm.data.model.MonitorLogEntryEntity
import com.apap.ctm.domain.model.MonitorLogEntry
import com.apap.ctm.testUtils.randomDateTime
import com.apap.ctm.testUtils.randomInt
import com.apap.ctm.testUtils.randomNumber
import com.apap.ctm.testUtils.randomString
import com.apap.ctm.util.toDateTimeString
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class MonitorLogEntryMapperTest {

    private lateinit var subject: MonitorLogEntryMapper

    @Before
    fun setUp() {
        subject = MonitorLogEntryMapper()
    }

    @Test
    fun `maps entity to domain model object`() {
        val entity = MonitorLogEntryEntity(
            id = randomInt(),
            beginning = randomDateTime().toDateTimeString(),
            duration = randomInt(),
            number = randomNumber().joinToString(""),
            name = randomString(),
            timesQueried = randomInt()
        )
        val expectedModel = MonitorLogEntry(
            name = entity.name,
            number = entity.number,
            duration = entity.duration,
            beginning = entity.beginning,
            timesQueried = entity.timesQueried
        )

        assertThat(subject.toDomain(entity)).isEqualTo(expectedModel)
    }

    @Test
    fun `maps domain model object to entity`() {
        val model = MonitorLogEntry(
            name = randomString(),
            number = randomNumber().toString(),
            duration = randomInt(),
            beginning = randomDateTime().toDateTimeString(),
            timesQueried = randomInt()
        )
        val expectedEntity = MonitorLogEntryEntity(
            beginning = model.beginning,
            duration = model.duration,
            number = model.number,
            name = model.name,
            timesQueried = model.timesQueried
        )

        assertThat(subject.toEntity(0,model)).isEqualTo(expectedEntity)
    }
}