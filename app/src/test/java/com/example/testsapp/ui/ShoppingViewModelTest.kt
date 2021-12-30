package com.example.testsapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.testsapp.MainCoroutineRule
import com.example.testsapp.getOrAwaitValueTest
import com.example.testsapp.other.Constants
import com.example.testsapp.other.Resource
import com.example.testsapp.repositories.FakeShoppingRepositoryShared
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    // whit this we allow tests to happen synchronously
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // this is needed because the coroutine tries to access the mainScope which we don't have
    // access in tests, that's why we create our own scope
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setup() {
        //We use this because real API calls are to slow and tests should be fast
        viewModel = ShoppingViewModel(FakeShoppingRepositoryShared())
    }

    @Test
    fun `insert shopping item with empty field, returns error`() {
        viewModel.insertShoppingItem("name", "", "3.0")

        // the value supposed to be observed and converted to a value
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled() is Resource.Error).isTrue()
    }

    @Test
    fun `insert shopping item with too long name, returns error`() {
        val invalidName = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH + 1) {
                append(1)
            }
        }

        // the value supposed to be observed and converted to a value
        viewModel.insertShoppingItem(invalidName, "5", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled() is Resource.Error).isTrue()
    }

    @Test
    fun `insert shopping item with too long price, returns error`() {
        val invalidPriceString = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH + 1) {
                append(1)
            }
        }

        viewModel.insertShoppingItem("name", "5", invalidPriceString)

        // the value supposed to be observed and converted to a value
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled() is Resource.Error).isTrue()
    }

    @Test
    fun `insert shopping item with too high amount, returns error`() {
        viewModel.insertShoppingItem("name", "9999999999999999999", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled() is Resource.Error).isTrue()
    }

    @Test
    fun `insert shopping item with valid input, returns success`() {
        viewModel.insertShoppingItem("name", "5", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled() is Resource.Success).isTrue()
    }

    @Test
    fun `insert shopping item and cleans the image url, returns success`() {
        viewModel.insertShoppingItem("name", "5", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        val valueImageUrl = viewModel.curImageUrl.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled() is Resource.Success).isTrue()
        assertThat(valueImageUrl).isEqualTo("")
    }
}

