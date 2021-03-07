package com.mathsemilio.hiraganalearner.common.di

import com.mathsemilio.hiraganalearner.common.factory.BackPressedCallbackFactory
import com.mathsemilio.hiraganalearner.domain.usecase.GetSymbolUseCase
import com.mathsemilio.hiraganalearner.others.notification.TrainingNotificationHelper
import com.mathsemilio.hiraganalearner.ui.common.helper.DialogHelper
import com.mathsemilio.hiraganalearner.ui.common.helper.InterstitialAdUseHelper
import com.mathsemilio.hiraganalearner.ui.common.helper.MessagesHelper
import com.mathsemilio.hiraganalearner.ui.screens.game.main.AlertUserHelper
import com.mathsemilio.hiraganalearner.ui.screens.game.main.MainScreenViewModel
import com.mathsemilio.hiraganalearner.ui.screens.game.result.ShareGameScoreHelper

class ControllerCompositionRoot(private val activityCompositionRoot: ActivityCompositionRoot) {

    private val activity get() = activityCompositionRoot.getActivity()

    private val context get() = activityCompositionRoot.context

    private val fragmentManager get() = activityCompositionRoot.fragmentManager

    val viewFactory get() = activityCompositionRoot.viewFactory

    val preferencesRepository get() = activityCompositionRoot.preferencesRepository

    val soundEffectsModule get() = activityCompositionRoot.soundEffectsModule

    val screensNavigator get() = activityCompositionRoot.screensNavigator

    val eventPoster get() = activityCompositionRoot.eventPoster

    val adRequest get() = activityCompositionRoot.adRequest

    val appThemeUtil get() = activityCompositionRoot.appThemeUtil

    val trainingNotificationHelper get() = TrainingNotificationHelper(context)

    val messagesHelper get() = MessagesHelper(context)

    val dialogHelper get() = DialogHelper(context, fragmentManager)

    val mainScreenViewModel get() = MainScreenViewModel()

    val backPressedCallbackFactory get() = BackPressedCallbackFactory

    val interstitialAdHelper get() = InterstitialAdUseHelper(activity, context, adRequest)

    val alertUserHelper get() = AlertUserHelper(dialogHelper)

    val getSymbolUseCase get() = GetSymbolUseCase(preferencesRepository)

    val shareGameScoreHelper get() = ShareGameScoreHelper(context)
}