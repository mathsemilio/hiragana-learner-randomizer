package com.mathsemilio.hiraganalearner.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mathsemilio.hiraganalearner.R

class MainActivity : AppCompatActivity() {

    //==========================================================================================
    // onCreate
    //==========================================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}