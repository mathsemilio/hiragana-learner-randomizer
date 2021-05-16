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
package com.mathsemilio.hiraganalearner.ui.common.navigation

import com.mathsemilio.hiraganalearner.ui.screens.main.GameMainFragment
import com.mathsemilio.hiraganalearner.ui.screens.result.GameResultFragment
import com.mathsemilio.hiraganalearner.ui.screens.settings.SettingsFragment
import com.mathsemilio.hiraganalearner.ui.screens.welcome.GameWelcomeFragment

class ScreensNavigator(private val fragmentTransactionManager: FragmentTransactionManager) {

    fun toSettingsScreen() {
        fragmentTransactionManager.pushFragmentOntoContainer(SettingsFragment(), null)
    }

    fun toWelcomeScreen() {
        fragmentTransactionManager.replaceFragmentOnContainerWith(GameWelcomeFragment())
    }

    fun toMainScreen(difficultyValue: Int) {
        fragmentTransactionManager.replaceFragmentOnContainerWith(
            GameMainFragment.withDifficultyValue(difficultyValue)
        )
    }

    fun toResultScreen(difficultyValue: Int, score: Int) {
        fragmentTransactionManager.replaceFragmentOnContainerWith(
            GameResultFragment.withGameResult(difficultyValue, score)
        )
    }

    fun navigateUp() {
        fragmentTransactionManager.popCurrentFragmentFromContainer()
    }
}