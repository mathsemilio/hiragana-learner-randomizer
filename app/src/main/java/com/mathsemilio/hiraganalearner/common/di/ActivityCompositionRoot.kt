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

package com.mathsemilio.hiraganalearner.common.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.mathsemilio.hiraganalearner.data.manager.PreferencesManager
import com.mathsemilio.hiraganalearner.others.SoundEffectsModule
import com.mathsemilio.hiraganalearner.ui.common.helper.FragmentContainerHelper
import com.mathsemilio.hiraganalearner.ui.common.helper.ThemeHelper
import com.mathsemilio.hiraganalearner.ui.common.helper.ToolbarVisibilityHelper
import com.mathsemilio.hiraganalearner.ui.common.navigation.FragmentTransactionManager
import com.mathsemilio.hiraganalearner.ui.common.navigation.ScreensNavigator

class ActivityCompositionRoot(
    private val compositionRoot: CompositionRoot,
    private val activity: AppCompatActivity
) {
    private val _preferencesManager by lazy {
        PreferencesManager(applicationContext)
    }

    private val _screensNavigator by lazy {
        ScreensNavigator(
            FragmentTransactionManager(
                fragmentManager,
                activity as FragmentContainerHelper
            )
        )
    }

    private val _themeHelper by lazy {
        ThemeHelper(preferencesManager)
    }

    private val _toolbarVisibilityHelper by lazy {
        ToolbarVisibilityHelper()
    }

    fun getActivity() = activity

    val applicationContext: Context get() = activity.applicationContext

    val fragmentManager get() = activity.supportFragmentManager

    val adRequest get() = compositionRoot.adRequest

    val eventPublisher get() = compositionRoot.eventPublisher

    val eventSubscriber get() = compositionRoot.eventSubscriber

    val preferencesManager get() = _preferencesManager

    val screensNavigator get() = _screensNavigator

    val soundEffectsModule
        get() = SoundEffectsModule(
            applicationContext,
            preferencesManager.soundEffectsVolume
        )

    val themeHelper get() = _themeHelper

    val toolbarVisibilityHelper get() = _toolbarVisibilityHelper
}