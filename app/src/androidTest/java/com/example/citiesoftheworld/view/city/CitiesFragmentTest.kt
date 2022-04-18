package com.example.citiesoftheworld.view.city

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.example.citiesoftheworld.database.model.city.CityRepository
import com.example.citiesoftheworld.database.model.city.CityRepositoryAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith


@FlowPreview
@ExperimentalStdlibApi
@MediumTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CitiesFragmentTest{

    private lateinit var repository: CityRepository
    private lateinit var citiesViewModel: CitiesViewModel

    @Before
    fun initRepositoryWithViewModel() {
        repository = CityRepositoryAndroidTest()
//        ServiceLocator.tasksRepository = repository
        citiesViewModel = CitiesViewModel(repository)
    }

    @After
    fun cleanupDb() = runBlockingTest {
//        ServiceLocator.resetRepository()
    }

    /*@Test
    fun mapViewSelected_DisplayMapView() = runBlockingTest{
        //Given that fragment is launched
        launchFragmentInContainer<CitiesFragment>(Bundle(), R.style.Theme_CitiesOfTheWorld)
//        onView(withId(R.id.citiesRecyclerView)).perform(click())

        //when
        citiesViewModel.setMapViewVisiblePostValue(false)
//        onView(withMenuIdOrText(R.id.menu_spinner, R.string.list_view)).perform(click())


        //then
        onView(withId(R.id.citiesRecyclerView))
            .check(matches(isDisplayed()))

    }*/

    fun withMenuIdOrText(@IdRes id: Int, @StringRes menuText: Int): Matcher<View?>? {
        val matcher: Matcher<View?> = withId(id)
        return try {
            onView(matcher).check(matches(isDisplayed()))
            matcher
        } catch (NoMatchingViewException: Exception) {
            openActionBarOverflowOrOptionsMenu(
                getInstrumentation().targetContext
            )
            withText(menuText)
        }
    }

}