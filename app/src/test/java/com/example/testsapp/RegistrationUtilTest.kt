package com.example.testsapp

import com.example.testsapp.dummy_tests.RegistrationUtil
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {

    // this naming is ok because we don't call the tests instead Junit does it
    @Test
    fun `empty username returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "",
            "12345",
            "12345",
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `valid input username=paswords`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "jorge785",
            "12345",
            "12345",
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `username already exists return false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "Peter",
            "12345",
            "12345",
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `invalid password return false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "test123",
            "",
            "",
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `mistmatch password return false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "test123",
            "123",
            "12345",
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `password too short return false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "test123",
            "12",
            "12",
        )
        assertThat(result).isFalse()
    }


}