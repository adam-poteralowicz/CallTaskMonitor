package com.apap.ctm

import com.apap.ctm.data.mapper.MonitorRootMapper
import com.apap.ctm.data.mapper.MonitorServiceMapper
import com.apap.ctm.data.model.MonitorRootEntity
import com.apap.ctm.domain.model.MonitorRoot
import com.apap.ctm.testUtils.randomDateTime
import com.apap.ctm.testUtils.randomInt
import com.apap.ctm.util.toDateTimeString
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class MonitorRootMapperTest {

    private lateinit var subject: MonitorRootMapper
    @MockK lateinit var serviceMapper: MonitorServiceMapper

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = MonitorRootMapper(serviceMapper)
    }

    @Test
    fun `maps entity to domain model object`() {
        val entity = MonitorRootEntity(
            id = randomInt(),
            start = randomDateTime().toDateTimeString(),
            services = emptyList()
        )
        val expectedModel = MonitorRoot(
            start = entity.start,
            services = emptyList()
        )

        assertThat(subject.toDomain(entity)).isEqualTo(expectedModel)
    }

    @Test
    fun `maps domain model object to entity`() {
        val model = MonitorRoot(
            start = randomDateTime().toDateTimeString(),
            services = emptyList()
        )
        val expectedEntity = MonitorRootEntity(
            id = 0,
            start = model.start,
            services = emptyList()
        )

        assertThat(subject.toEntity(model)).isEqualTo(expectedEntity)
    }
}