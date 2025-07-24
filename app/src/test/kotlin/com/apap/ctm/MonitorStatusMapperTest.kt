package com.apap.ctm

import com.apap.ctm.data.mapper.MonitorStatusMapper
import com.apap.ctm.data.model.MonitorStatusEntity
import com.apap.ctm.domain.model.MonitorStatus
import com.apap.ctm.testUtils.randomBoolean
import com.apap.ctm.testUtils.randomDateTime
import com.apap.ctm.testUtils.randomInt
import com.apap.ctm.testUtils.randomNumber
import com.apap.ctm.testUtils.randomString
import com.apap.ctm.util.toDateTimeString
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class MonitorStatusMapperTest {

    private lateinit var subject: MonitorStatusMapper

    @Before
    fun setUp() {
        subject = MonitorStatusMapper()
    }

    @Test
    fun `maps entity to domain model object`() {
        val entity = MonitorStatusEntity(
            id = randomInt(),
            start = randomDateTime().toDateTimeString(),
            stop = randomDateTime().toDateTimeString(),
            duration = randomInt(),
            ongoing = randomBoolean(),
            number = randomNumber().joinToString(""),
            name = randomString(),
        )
        val expectedModel = MonitorStatus(
            number = entity.number,
            ongoing = entity.ongoing,
            duration = entity.duration,
            name = entity.name,
            start = entity.start,
            stop = entity.stop
        )

        assertThat(subject.toDomain(entity)).isEqualTo(expectedModel)
    }

    @Test
    fun `maps domain model object to entity`() {
        val model = MonitorStatus(
            number = randomNumber().toString(),
            ongoing = randomBoolean(),
            duration = randomInt(),
            name = randomString(),
            start = randomDateTime().toDateTimeString(),
            stop = randomDateTime().toDateTimeString()
        )
        val expectedEntity = MonitorStatusEntity(
            id = 0,
            start = model.start,
            stop = model.stop,
            duration = model.duration,
            ongoing = model.ongoing,
            number = model.number,
            name = model.name
        )

        assertThat(subject.toEntity(model)).isEqualTo(expectedEntity)
    }
}