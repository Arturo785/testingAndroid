package com.example.testsapp.dummy_tests

import android.content.Context

class ResourceComparator {

    fun isEqual(context: Context, resId: Int, string: String): Boolean {
        return context.getString(resId) == string
    }


}