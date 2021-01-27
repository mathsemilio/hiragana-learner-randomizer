package com.mathsemilio.hiraganalearner.ui.others

import androidx.fragment.app.FragmentManager
import com.mathsemilio.hiraganalearner.R
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
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            replace(fragmentContainerHelper.getFragmentContainer().id, SettingsScreen())
            addToBackStack(null)
            commit()
        }
    }

    fun navigateToWelcomeScreen() {
        fragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            replace(fragmentContainerHelper.getFragmentContainer().id, GameWelcomeScreen())
            commitNow()
        }
    }

    fun navigateToMainScreen(difficultyValue: Int) {
        fragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            replace(
                fragmentContainerHelper.getFragmentContainer().id,
                GameMainScreen.newInstance(difficultyValue)
            )
            commitNow()
        }
    }

    fun navigateToResultScreen(difficultyValue: Int, score: Int) {
        fragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            replace(
                fragmentContainerHelper.getFragmentContainer().id,
                GameResultScreen.newInstance(difficultyValue, score)
            )
            commitNow()
        }
    }
}