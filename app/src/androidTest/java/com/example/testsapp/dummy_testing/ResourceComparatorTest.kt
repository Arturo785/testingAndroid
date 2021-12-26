package com.example.testsapp.dummy_testing

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.testsapp.R
import com.example.testsapp.dummy_tests.ResourceComparator
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test


// tests in androidTest folder need a device or emulator because they need the
// framework utils and tools, the ones in test only use the JVM

class ResourceComparatorTest() {

    // This is the preferred way of using using classes in tests
    private lateinit var resourceComparer: ResourceComparator

    // do not use it this wasy, is bad practice
    //private val resourceComparer = ResourceComparator()


    // wuth this annotation we make sure we have a fresh instance for each test
    @Before
    fun setup() {
        resourceComparer = ResourceComparator()
    }


    @Test
    fun stringResourceSameAsGivenString_returnsTrue() {
        // ApplicationProvider lets us get the context needed for the class
        val context = ApplicationProvider.getApplicationContext<Context>()

        val result = resourceComparer.isEqual(context, R.string.app_name, "TestsApp")
        assertThat(result).isTrue()
    }

    @Test
    fun stringResourceDifferentAsGivenString_returnsFalse() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        val result = resourceComparer.isEqual(context, R.string.app_name, "Hello")
        assertThat(result).isFalse()
    }

}