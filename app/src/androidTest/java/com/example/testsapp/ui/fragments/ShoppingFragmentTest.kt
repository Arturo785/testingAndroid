package com.example.testsapp.ui.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.testsapp.R
import com.example.testsapp.adapters.ShoppingItemAdapter
import com.example.testsapp.data.local.ShoppingItem
import com.example.testsapp.getOrAwaitValue
import com.example.testsapp.launchFragmentInHiltContainer
import com.example.testsapp.ui.ShoppingViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ShoppingFragmentTest {


    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Inject
    lateinit var testFragmentFactory: TestShoppingFragmentFactory


    @Test
    fun swipeShoppingItem_deleteItemInDb() {
        val shoppingItem = ShoppingItem("TEST", 1, 1f, "TEST", 1)
        var testViewModel: ShoppingViewModel? = null

        launchFragmentInHiltContainer<ShoppingFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel // this is the injected that comes from the
            // factory that we just passed
            viewModel?.insertShoppingItemIntoDb(shoppingItem)
        }


        // pretend to erase the item from the recycler
        onView(withId(R.id.rvShoppingItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ShoppingItemAdapter.ShoppingItemViewHolder>(
                0,
                swipeLeft()
            )
        )

        assertThat(testViewModel?.shoppingItems?.getOrAwaitValue()).doesNotContain(shoppingItem)
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