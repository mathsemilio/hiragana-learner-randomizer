package com.mathsemilio.hiraganalearner.common.factory

import androidx.activity.OnBackPressedCallback

object BackPressedCallbackFactory {

    fun getOnBackPressedCallback(onBackPressed: () -> Unit) =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = onBackPressed()
        }
}