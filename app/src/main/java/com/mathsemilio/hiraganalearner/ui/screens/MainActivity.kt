package com.mathsemilio.hiraganalearner.ui.screens

import android.os.Bundle
import android.widget.FrameLayout
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.event.ToolbarVisibilityChangedEvent
import com.mathsemilio.hiraganalearner.common.event.poster.EventPoster
import com.mathsemilio.hiraganalearner.ui.common.helper.FragmentContainerHelper
import com.mathsemilio.hiraganalearner.ui.common.helper.ScreensNavigator

class MainActivity : BaseActivity(),
    MainActivityContract.View.Listener,
    FragmentContainerHelper,
    EventPoster.Listener {

    private lateinit var view: MainActivityView

    private lateinit var screensNavigator: ScreensNavigator
    private lateinit var eventPoster: EventPoster

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = compositionRoot.viewFactory.getMainActivityView(null)

        screensNavigator = compositionRoot.screensNavigator
        eventPoster = compositionRoot.eventPoster

        setTheme(R.style.Theme_HiraganaLearner)

        setContentView(view.rootView)

        if (savedInstanceState == null)
            screensNavigator.navigateToWelcomeScreen()
    }

    override fun onToolbarNavigationIconClicked() {
        supportFragmentManager.popBackStackImmediate()
    }

    override val fragmentContainer: FrameLayout
        get() {
            return view.fragmentContainer
        }

    override fun onEvent(event: Any) {
        when (event) {
            is ToolbarVisibilityChangedEvent -> handleToolbarVisibilityChangedEvent(event.event)
        }
    }

    private fun handleToolbarVisibilityChangedEvent(event: ToolbarVisibilityChangedEvent.Event) {
        when (event) {
            ToolbarVisibilityChangedEvent.Event.SHOW_TOOLBAR -> view.showToolbar()
            ToolbarVisibilityChangedEvent.Event.HIDE_TOOLBAR -> view.hideToolbar()
        }
    }

    override fun onStart() {
        view.addListener(this)
        eventPoster.addListener(this)
        super.onStart()
    }

    override fun onStop() {
        view.removeListener(this)
        eventPoster.removeListener(this)
        super.onStop()
    }
}