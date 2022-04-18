package com.example.citiesoftheworld.database.model.city

//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import com.example.citiesoftheworld.getOrAwaitValue

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.citiesoftheworld.database.AppDatabase
import com.example.citiesoftheworld.database.model.cityAndCountry.CityAndCountry
import com.example.citiesoftheworld.database.model.country.Country
import com.example.citiesoftheworld.database.model.country.CountryDao
import com.example.citiesoftheworld.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIterableContainingInOrder.contains
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class DaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var cityDao: CityDao
    private lateinit var countryDao: CountryDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        cityDao = database.cityDao
        countryDao = database.countryDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertCityAndCountry() = runBlockingTest {

        val city = City(
                id = 1L,
                name = "Name",
                localName = "Local name",
                lat = "2.0000",
                lng = "4.0000",
                countryId = 1L
            )

        val country = Country(
                id = 1L,
                name = "Name",
                code = "0001",
                continentId = "1"
            )

        val cityAndCountry = CityAndCountry(
            city = city,
            country = country
        )

        cityDao.insert(city)
        countryDao.insert(country)
        val allCities = cityDao.getCitiesLiveData().getOrAwaitValue()

        assertThat(allCities, contains(cityAndCountry))

    }

    @Test
    fun getCitiesByName() = runBlockingTest {

        val cityAndCountry1 = CityAndCountry(
            city = City(
                id = 1L,
                name = "City1",
                localName = "Local name1",
                lat = "2.0000",
                lng = "4.0000",
                countryId = 1L
            ),
            country = Country(
                id = 1L,
                name = "Country1",
                code = "0001",
                continentId = "1"
            )
        )

        val cityAndCountry2 = CityAndCountry(
            city = City(
                id = 2L,
                name = "City2",
                localName = "Local name2",
                lat = "2.0000",
                lng = "4.0000",
                countryId = 2L
            ),
            country = Country(
                id = 2L,
                name = "Country2",
                code = "0001",
                continentId = "1"
            )
        )

        cityDao.insert(cityAndCountry1.city)
        cityAndCountry1.country?.let { countryDao.insert(it) }

        cityDao.insert(cityAndCountry2.city)
        cityAndCountry2.country?.let { countryDao.insert(it) }

        val allCities = cityDao.getCitiesByNameLiveData("City1").getOrAwaitValue()

//        assertThat(allCities, doesNotContains(cityAndCountry2))

        assertThat(allCities, not(hasItem(cityAndCountry2)))


//        val city = allCities[0]

//        assertThat(city, 'is' city)

//        assertThat(allCities, CoreMatchers.everyItem(HasItemMatcher("2")))

//        assertThat(allCities.getCon, equals(cityAndCountry1.city.name))

//        Assert.assertThat(testedList, CoreMatchers.everyItem(HasItemMatcher("2")))

    }

//    @Test
//    fun deleteShoppingItem() = runBlockingTest {
//        val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
//        cityDao.insertShoppingItem(shoppingItem)
//        cityDao.deleteShoppingItem(shoppingItem)
//
//        val allShoppingItems = cityDao.observeAllShoppingItems().getOrAwaitValue()
//
//        assertThat(allShoppingItems).doesNotContain(shoppingItem)
//    }

    /*@Test
    fun observeTotalPriceSum() = runBlockingTest {
        val shoppingItem1 = ShoppingItem("name", 2, 10f, "url", id = 1)
        val shoppingItem2 = ShoppingItem("name", 4, 5.5f, "url", id = 2)
        val shoppingItem3 = ShoppingItem("name", 0, 100f, "url", id = 3)
        cityDao.insertShoppingItem(shoppingItem1)
        cityDao.insertShoppingItem(shoppingItem2)
        cityDao.insertShoppingItem(shoppingItem3)

        val totalPriceSum = cityDao.observeTotalPrice().getOrAwaitValue()

        assertThat(totalPriceSum).isEqualTo(2 * 10f + 4 * 5.5f)
    }*/



}













