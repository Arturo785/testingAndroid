package com.example.testsapp.ui.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.testsapp.*
import com.example.testsapp.repositories.FakeShoppingRepositoryShared
import com.example.testsapp.ui.ShoppingViewModel
import com.google.common.truth.Truth
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
class AddShoppingItemFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    // whit this we allow tests to happen synchronously
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // this is needed because the coroutine tries to access the mainScope which we don't have
    // access in tests, that's why we create our own scope
    @get:Rule
    var mainCoroutineRule = MainCoroutineRuleShared() // comes from testing shared directory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun pressBackButton_popBackStack() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        // fun made by mockito
        pressBack()

        // check that this was made
        verify(navController).popBackStack()
    }

    @Test
    fun pressIvImageGoToPickImageFragment() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.ivShoppingImage)).perform(click())

        verify(navController).navigate(
            AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
        )
    }

    @Test
    fun pressBackButton_cleansUrlImage() {
        val navController = mock(NavController::class.java)
        val testViewModel =
            ShoppingViewModel(FakeShoppingRepositoryShared()) // comes from testing shared directory

        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
            viewModel.setCurImageUrl("not null")
        }

        // fun made by mockito
        pressBack()

        val valueImage = testViewModel.curImageUrl.getOrAwaitValue()

        Truth.assertThat(valueImage).isEmpty()
    }


}
