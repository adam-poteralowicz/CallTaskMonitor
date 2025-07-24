package com.apap.ctm

import com.apap.ctm.data.mapper.MonitorServiceMapper
import com.apap.ctm.data.model.MonitorServiceEntity
import com.apap.ctm.domain.model.MonitorService
import com.apap.ctm.testUtils.randomInt
import com.apap.ctm.testUtils.randomString
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class MonitorServiceMapperTest {

    private lateinit var subject: MonitorServiceMapper

    @Before
    fun setUp() {
        subject = MonitorServiceMapper()
    }

    @Test
    fun `maps entity to domain model object`() {
        val entity = MonitorServiceEntity(
            id = randomInt(),
            name = randomString(),
            uri = randomString()
        )
        val expectedModel = MonitorService(
            name = entity.name,
            uri = entity.uri
        )

        assertThat(subject.toDomain(entity)).isEqualTo(expectedModel)
    }

    @Test
    fun `maps domain model object to entity`() {
        val model = MonitorService(
            name = randomString(),
            uri = randomString()
        )
        val expectedEntity = MonitorServiceEntity(
            name = model.name,
            uri = model.uri
        )

        assertThat(subject.toEntity(model)).isEqualTo(expectedEntity)
    }
}