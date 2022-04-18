package com.example.citiesoftheworld.view.city

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.citiesoftheworld.database.model.city.CityRepositoryAndroidTest
import com.example.citiesoftheworld.getOrAwaitValue
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.robolectric.annotation.Config

@Config(sdk = [30]) // https://github.com/robolectric/robolectric/pull/6776
class CitiesViewModelTest {

    // Subject under test
    private lateinit var citiesViewModel: CitiesViewModel
    private lateinit var cityRepositoryTest: CityRepositoryAndroidTest

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        cityRepositoryTest = CityRepositoryAndroidTest()
        citiesViewModel = CitiesViewModel(cityRepositoryTest)
    }

    @Test
    fun testSetFilter() {
        //when
        citiesViewModel.setFilter("Name")

        //then
        val value = citiesViewModel.filterLiveData.getOrAwaitValue()

        MatcherAssert.assertThat(value, Matchers.`is`("Name"))
    }
}