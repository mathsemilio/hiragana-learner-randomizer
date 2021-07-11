/*
Copyright 2020 Matheus Menezes

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.mathsemilio.hiraganalearner.ui.screens

import android.os.Bundle
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.ui.common.helper.FragmentContainerHelper
import com.mathsemilio.hiraganalearner.ui.common.helper.ThemeHelper
import com.mathsemilio.hiraganalearner.ui.common.helper.ToolbarVisibilityHelper
import com.mathsemilio.hiraganalearner.ui.common.navigation.ScreensNavigator

class MainActivity : BaseActivity(),
    ToolbarVisibilityHelper.Listener,
    MainActivityView.Listener,
    FragmentContainerHelper {

    private lateinit var view: MainActivityViewImpl

    private lateinit var toolbarVisibilityHelper: ToolbarVisibilityHelper
    private lateinit var screensNavigator: ScreensNavigator
    private lateinit var themeHelper: ThemeHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = MainActivityViewImpl(layoutInflater)

        toolbarVisibilityHelper = compositionRoot.toolbarVisibilityHelper
        screensNavigator = compositionRoot.screensNavigator
        themeHelper = compositionRoot.themeHelper

        setTheme(R.style.Theme_SyllabaryRandomizer)

        themeHelper.setThemeFromPreference()

        setContentView(view.rootView)

        if (savedInstanceState == null)
            screensNavigator.toWelcomeScreen()
    }

    override fun onShowToolbar() {
        view.showToolbar()
    }

    override fun onHideToolbar() {
        view.hideToolbar()
    }

    override fun onToolbarNavigationIconClicked() {
        screensNavigator.navigateUp()
    }

    override fun getFragmentContainerId() = view.fragmentContainer.id

    override fun onStart() {
        view.addListener(this)
        toolbarVisibilityHelper.addListener(this)
        super.onStart()
    }

    override fun onStop() {
        view.removeListener(this)
        toolbarVisibilityHelper.removeListener(this)
        super.onStop()
    }
}