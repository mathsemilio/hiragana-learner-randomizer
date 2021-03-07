package com.mathsemilio.hiraganalearner.ui.common.helper

import androidx.fragment.app.FragmentManager
import com.mathsemilio.hiraganalearner.ui.screens.game.main.GameMainScreen
import com.mathsemilio.hiraganalearner.ui.screens.game.result.GameResultScreen
import com.mathsemilio.hiraganalearner.ui.screens.game.welcome.GameWelcomeScreen
import com.mathsemilio.hiraganalearner.ui.screens.settings.SettingsScreen

class ScreensNavigator(
    private val fragmentManager: FragmentManager,
    private val fragmentContainerHelper: FragmentContainerHelper
) {
    fun navigateToSettingsScreen() {
        fragmentManager.beginTransaction().apply {
            setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
            replace(fragmentContainerHelper.fragmentContainer.id, SettingsScreen())
            addToBackStack(null)
            commit()
        }
    }

    fun navigateToWelcomeScreen() {
        fragmentManager.beginTransaction().apply {
            setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            replace(fragmentContainerHelper.fragmentContainer.id, GameWelcomeScreen())
            commitNow()
        }
    }

    fun navigateToMainScreen(difficultyValue: Int) {
        fragmentManager.beginTransaction().apply {
            setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            replace(
                fragmentContainerHelper.fragmentContainer.id,
                GameMainScreen.newInstance(difficultyValue)
            )
            commitNow()
        }
    }

    fun navigateToResultScreen(difficultyValue: Int, score: Int) {
        fragmentManager.beginTransaction().apply {
            setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            replace(
                fragmentContainerHelper.fragmentContainer.id,
                GameResultScreen.newInstance(difficultyValue, score)
            )
            commitNow()
        }
    }
}