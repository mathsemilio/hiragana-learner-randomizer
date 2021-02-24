package com.mathsemilio.hiraganalearner.ui.screens

import android.widget.FrameLayout

interface MainActivityView {

    interface Listener {
        fun onToolbarNavigationIconClicked()
    }

    fun showToolbar()
    fun hideToolbar()
    val fragmentContainer: FrameLayout
}