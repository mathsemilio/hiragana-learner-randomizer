package com.mathsemilio.hiraganalearner.ui.screens

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.google.android.material.appbar.MaterialToolbar
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.ui.common.view.BaseObservableView

class MainActivityView(layoutInflater: LayoutInflater, parent: ViewGroup?) :
    BaseObservableView<MainActivityContract.View.Listener>(), MainActivityContract.View {

    private var materialToolbarApp: MaterialToolbar
    private var frameLayoutScreenContainer: FrameLayout

    init {
        rootView = layoutInflater.inflate(R.layout.activity_main, parent, false)
        materialToolbarApp = findViewById(R.id.material_toolbar_app)
        frameLayoutScreenContainer = findViewById(R.id.frame_layout_screen_container)
        attachToolbarNavigationIconOnClickListener()
    }

    private fun attachToolbarNavigationIconOnClickListener() {
        materialToolbarApp.setNavigationOnClickListener {
            onToolbarNavigationIconClicked()
        }
    }

    override fun showToolbar() {
        materialToolbarApp.isVisible = true
    }

    override fun hideToolbar() {
        materialToolbarApp.isVisible = false
    }

    override val fragmentContainer: FrameLayout
        get() {
            return frameLayoutScreenContainer
        }

    private fun onToolbarNavigationIconClicked() {
        listeners.forEach { it.onToolbarNavigationIconClicked() }
    }
}