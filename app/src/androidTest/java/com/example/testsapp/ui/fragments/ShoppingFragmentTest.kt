package com.example.testsapp.ui.fragments

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.testsapp.R
import com.example.testsapp.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ShoppingFragmentTest {


    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickAddShoppingItemButton_navigateToAddShoppingItemFragment() {
        // what mockito does is that creates fake classes that can mimic the behavior of
        // the class and we can also give the behavior that we want
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<ShoppingFragment> {
            // give the navigation to our fragment
            Navigation.setViewNavController(requireView(), navController)
        }

        // Expresso is used to make UI testing
        // we use a view by id and make it perform the action that we want
        onView(withId(R.id.fabAddShoppingItem)).perform(click())

        // check that the action was performed
        verify(navController).navigate(
            ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment()
        )
    }
}