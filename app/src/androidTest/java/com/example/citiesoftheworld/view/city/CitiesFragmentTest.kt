package com.example.citiesoftheworld.view.city

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.citiesoftheworld.R
import com.example.citiesoftheworld.database.model.city.CityRepository
import com.example.citiesoftheworld.database.model.city.CityRepositoryAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@FlowPreview
@ExperimentalStdlibApi
@MediumTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CitiesFragmentTest{

    private lateinit var repository: CityRepository

    @Before
    fun initRepository() {
        repository = CityRepositoryAndroidTest()
//        ServiceLocator.tasksRepository = repository
    }

    @After
    fun cleanupDb() = runBlockingTest {
//        ServiceLocator.resetRepository()
    }

//    @OptIn(ExperimentalStdlibApi::class, kotlinx.coroutines.FlowPreview::class)
    @Test
    fun activeTaskDetails_DisplayedInUi()  = runBlockingTest{
        // GIVEN - Add active (incomplete) task to the DB
//        val activeTask = Task("Active Task", "AndroidX Rocks", false)
//        repository.saveTask(activeTask)

        // GIVEN - On CitiesFragment screen
        launchFragmentInContainer<CitiesFragment>(Bundle(), R.style.Theme_CitiesOfTheWorld)

    //WHEN

    onView(withId(R.id.spinner)).perform(click())
    onView(withText("Spinner Item 1")).perform(click());

    onView(withId(R.id.spinner)).perform(click())
    onView(withText("Spinner Item 1")).perform(click());

        // THEN - Task details are displayed on the screen
        // make sure that the title/description are both shown and correct
//        onView(withId(R.id.task_detail_title_text))
//            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//        onView(withId(R.id.task_detail_title_text))
//            .check(ViewAssertions.matches(ViewMatchers.withText("Active Task")))
//        onView(withId(R.id.task_detail_description_text))
//            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//        onView(withId(R.id.task_detail_description_text))
//            .check(ViewAssertions.matches(ViewMatchers.withText("AndroidX Rocks")))
//
//        // and make sure the "active" checkbox is shown unchecked
//        onView(withId(R.id.task_detail_complete_checkbox))
//            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//        onView(withId(R.id.task_detail_complete_checkbox))
//            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isChecked())))

    }

}