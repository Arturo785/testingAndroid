package com.example.testsapp

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint


// This class will be used to test fragments because the normal framework creates
// empty activities that can not be injected
@AndroidEntryPoint
class HiltTestActivity : AppCompatActivity()