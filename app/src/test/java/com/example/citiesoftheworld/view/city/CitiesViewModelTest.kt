package com.example.citiesoftheworld.view.city

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.citiesoftheworld.database.model.city.CityRepositoryTest
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.robolectric.annotation.Config

@Config(sdk = [30]) // https://github.com/robolectric/robolectric/pull/6776
class CitiesViewModelTest {

    // Subject under test
    private lateinit var citiesViewModel: CitiesViewModel
    private lateinit var cityRepositoryTest: CityRepositoryTest

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        cityRepositoryTest = CityRepositoryTest()
        citiesViewModel = CitiesViewModel(cityRepositoryTest)
    }

    fun testSetFilter() {}
}