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
        val settingsFragment = SettingsScreen()
        fragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            replace(fragmentContainerHelper.getFragmentContainer().id, settingsFragment)
            addToBackStack(null)
            commit()
        }
    }

    fun navigateToWelcomeScreen() {
        val gameWelcomeScreenFragment = GameWelcomeScreen()
        fragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            replace(fragmentContainerHelper.getFragmentContainer().id, gameWelcomeScreenFragment)
            commitNow()
        }
    }

    fun navigateToMainScreen(difficultyValue: Int) {
        val gameMainScreenFragment = GameMainScreen.newInstance(difficultyValue)
        fragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            replace(fragmentContainerHelper.getFragmentContainer().id, gameMainScreenFragment)
            commitNow()
        }
    }

    fun navigateToResultScreen(difficultyValue: Int, score: Int) {
        val gameResultScreenFragment = GameResultScreen.newInstance(difficultyValue, score)
        fragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            replace(fragmentContainerHelper.getFragmentContainer().id, gameResultScreenFragment)
            commitNow()
        }
    }
}