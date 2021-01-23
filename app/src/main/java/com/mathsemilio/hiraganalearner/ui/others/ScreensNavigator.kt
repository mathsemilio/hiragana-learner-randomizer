package com.mathsemilio.hiraganalearner.ui.others

import androidx.fragment.app.FragmentManager
import com.mathsemilio.hiraganalearner.ui.screens.game.main.GameMainScreen
import com.mathsemilio.hiraganalearner.ui.screens.game.result.GameResultScreen
import com.mathsemilio.hiraganalearner.ui.screens.game.welcome.GameWelcomeScreen
import com.mathsemilio.hiraganalearner.ui.screens.settings.SettingsScreen
import com.mathsemilio.hiraganalearner.R

class ScreensNavigator(
    private val mFragmentManager: FragmentManager,
    private val mFragmentContainerHelper: FragmentContainerHelper
) {
    fun navigateToSettingsScreen() {
        val settingsFragment = SettingsScreen()
        mFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            replace(mFragmentContainerHelper.getFragmentContainer().id, settingsFragment)
            addToBackStack(null)
            commit()
        }
    }

    fun navigateToWelcomeScreen() {
        val gameWelcomeScreenFragment = GameWelcomeScreen()
        mFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            replace(mFragmentContainerHelper.getFragmentContainer().id, gameWelcomeScreenFragment)
            commit()
        }
    }

    fun navigateToMainScreen(difficultyValue: Int) {
        val gameMainScreenFragment = GameMainScreen.newInstance(difficultyValue)
        mFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            replace(mFragmentContainerHelper.getFragmentContainer().id, gameMainScreenFragment)
            commit()
        }
    }

    fun navigateToResultScreen(difficultyValue: Int, score: Int) {
        val gameResultScreenFragment = GameResultScreen.newInstance(difficultyValue, score)
        mFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            replace(mFragmentContainerHelper.getFragmentContainer().id, gameResultScreenFragment)
            commit()
        }
    }
}