package com.mathsemilio.hiraganalearner.ui.screens

import android.os.Bundle
import android.widget.FrameLayout
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.ui.common.helper.FragmentContainerHelper
import com.mathsemilio.hiraganalearner.ui.common.helper.ScreensNavigator
import com.mathsemilio.hiraganalearner.ui.common.helper.ToolbarVisibilityHelper

class MainActivity : BaseActivity(),
    MainActivityView.Listener,
    FragmentContainerHelper,
    ToolbarVisibilityHelper {

    private lateinit var mainActivityView: MainActivityViewImpl

    private lateinit var screensNavigator: ScreensNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityView = compositionRoot.viewFactory.getMainActivityView(null)

        screensNavigator = compositionRoot.screensNavigator

        setTheme(R.style.Theme_HiraganaLearner)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            screensNavigator.navigateToWelcomeScreen()
    }

    override fun onToolbarNavigationIconClicked() {
        supportFragmentManager.popBackStackImmediate()
    }

    override fun getFragmentContainer(): FrameLayout {
        return mainActivityView.fragmentContainer
    }

    override fun showToolbar() {
        mainActivityView.showToolbar()
    }

    override fun hideToolbar() {
        mainActivityView.hideToolbar()
    }
}