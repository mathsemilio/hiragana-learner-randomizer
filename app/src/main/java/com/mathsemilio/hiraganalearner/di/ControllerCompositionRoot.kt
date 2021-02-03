package com.mathsemilio.hiraganalearner.di

import androidx.activity.OnBackPressedCallback
import com.mathsemilio.hiraganalearner.others.notification.TrainingNotificationHelper
import com.mathsemilio.hiraganalearner.ui.others.DialogHelper
import com.mathsemilio.hiraganalearner.ui.others.MessagesHelper
import com.mathsemilio.hiraganalearner.ui.others.ToolbarVisibilityHelper
import com.mathsemilio.hiraganalearner.ui.screens.common.InterstitialAdUseCase
import com.mathsemilio.hiraganalearner.ui.screens.game.main.usecase.AlertUserUseCase
import com.mathsemilio.hiraganalearner.ui.screens.game.main.viewmodel.GameMainScreenViewModel
import com.mathsemilio.hiraganalearner.ui.screens.game.result.usecase.ShareGameScoreUseCase

class ControllerCompositionRoot(private val activityCompositionRoot: ActivityCompositionRoot) {

    private val activity get() = activityCompositionRoot.getActivity()

    private val context get() = activityCompositionRoot.context

    private val fragmentManager get() = activityCompositionRoot.fragmentManager

    val viewFactory get() = activityCompositionRoot.viewFactory

    val preferencesRepository get() = activityCompositionRoot.preferencesRepository

    val soundEffectsModule get() = activityCompositionRoot.soundEffectsModule

    val screensNavigator get() = activityCompositionRoot.screensNavigator

    val adRequest get() = activityCompositionRoot.adRequest

    val appThemeUtil get() = activityCompositionRoot.appThemeUtil

    val trainingNotificationHelper get() = TrainingNotificationHelper(context)

    val toolbarVisibilityHelper get() = activity as ToolbarVisibilityHelper

    val messagesHelper get() = MessagesHelper(context)

    val dialogHelper get() = DialogHelper(context, fragmentManager)

    val interstitialAdUseCase get() = InterstitialAdUseCase(activity, context, adRequest)

    val gameMainScreenViewModel get() = GameMainScreenViewModel()

    fun getOnBackPressedCallback(onBackPressed: () -> Unit) =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = onBackPressed()
        }

    val alertUserUseCase get() = AlertUserUseCase(context, fragmentManager)

    fun getShareGameScoreUseCase(score: Int) = ShareGameScoreUseCase(context, score)
}