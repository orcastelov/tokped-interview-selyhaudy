package com.tokopedia.maps.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.maps.MapsService
import com.tokopedia.maps.model.CountryDto
import com.tokopedia.maps.scheduler.TrampolineSchedulerProvider
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MapsViewModelImplTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: MapsViewModelImpl
    private val mapsServiceMock: MapsService = mockk()

    @Before
    fun setUp() {
        every { mapsServiceMock.getAllCountries() } returns Observable.just(listOf(INDONESIA, MALAYSIA))
        viewModel = createViewModel()
    }

    @Test
    fun allCountries() {
        assertEquals(viewModel.allCountries.value?.first(), INDONESIA.toModel())
        assertEquals(viewModel.allCountries.value?.last(), MALAYSIA.toModel())
    }

    @Test
    fun findCountryByName() {
        assertEquals(viewModel.findCountryByName(INDONESIA.name), INDONESIA.toModel())
        assertEquals(viewModel.findCountryByName(MALAYSIA.name), MALAYSIA.toModel())
    }

    private fun createViewModel(): MapsViewModelImpl {
        return MapsViewModelImpl(
                TrampolineSchedulerProvider(),
                mapsServiceMock
        )
    }

    companion object {
        val INDONESIA = CountryDto(
                name = "Indonesia",
                capital = "Jakarta",
                population = 260_000_000,
                callingCodes = listOf("62"),
                latlng = listOf(6.2088, 106.8456)
        )
        val MALAYSIA = CountryDto(
                name = "Malaysia",
                capital = "Kuala Lumpur",
                population = 31_000_000,
                callingCodes = listOf("60"),
                latlng = listOf(3.1390, 101.6869)
        )
    }
}