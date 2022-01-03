package com.example.testsapp.ui.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.testsapp.*
import com.example.testsapp.data.local.ShoppingItem
import com.example.testsapp.repositories.FakeShoppingRepositoryShared
import com.example.testsapp.ui.ShoppingViewModel
import com.google.common.truth.Truth
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

    @Inject
    lateinit var fragmentFactory: ShoppingFragmentFactory

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


    @Test
    fun clickInsertIntoDb_shoppingItemInsertedIntoDb() {
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryShared())

        launchFragmentInHiltContainer<AddShoppingItemFragment>(
            // we pass the factory because our fragment uses dependency injection
            fragmentFactory = fragmentFactory
        ) {
            viewModel = testViewModel
        }

        // puts text into the edit texts
        onView(withId(R.id.etShoppingItemName)).perform(replaceText("shopping item"))
        onView(withId(R.id.etShoppingItemAmount)).perform(replaceText("5"))
        onView(withId(R.id.etShoppingItemPrice)).perform(replaceText("5.5"))


        // this button is supposed to call the view model and to perform the
        // insertion
        onView(withId(R.id.btnAddShoppingItem)).perform(click())

        assertThat(testViewModel.shoppingItems.getOrAwaitValue())
            .contains(ShoppingItem("shopping item", 5, 5.5f, ""))
    }

}
