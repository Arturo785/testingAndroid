package com.example.testsapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.testsapp.getOrAwaitValue
import com.example.testsapp.launchFragmentInHiltContainer
import com.example.testsapp.ui.fragments.ShoppingFragment
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named


// This is because the Junit class needs to gather data from
// the android framework
@ExperimentalCoroutinesApi
@SmallTest // with this we tell that this file will run unit tests only
@HiltAndroidTest
class ShoppingDaoTest {

    // thanks to this we can run our code in an asynchronous way otherwise it would be impossible
    // because Junit treats the code by itself in synchronously way
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    // the pyramid goes like this
    // top is UI testing
    // middle is Instrumentation testing
    // the base is unit testing

    @Inject
    @Named("test_db")
    lateinit var database: ShoppingItemDatabase

    private lateinit var dao: ShoppingDao

    @Before
    fun setup() {
        hiltRule.inject() // this gives the init of the injection

        dao = database.shoppingDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    // we use run blocking and main thread queries because we don't want
    // parallelism errors and we want to run the tests in the same thread
    @Test
    fun insertShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).contains(shoppingItem)
    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).doesNotContain(shoppingItem)
    }


    @Test
    fun observeTotalPriceSum() = runBlockingTest {
        val shoppingItem1 = ShoppingItem("name", 2, 10f, "url", id = 1)
        val shoppingItem2 = ShoppingItem("name", 4, 5.5f, "url", id = 2)
        val shoppingItem3 = ShoppingItem("name", 0, 100f, "url", id = 3)

        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalPriceSum = dao.observeTotalPrice().getOrAwaitValue()

        assertThat(totalPriceSum).isEqualTo(2 * 10f + 4 * 5.5f) // the amount multiplied with the price
    }

}