package com.mathsemilio.hiraganalearner.common.dependencyinjection

import androidx.activity.OnBackPressedCallback
import com.mathsemilio.hiraganalearner.ui.screens.game.main.MainScreenViewModel
import com.mathsemilio.hiraganalearner.others.notification.TrainingNotificationHelper
import com.mathsemilio.hiraganalearner.ui.common.helper.DialogHelper
import com.mathsemilio.hiraganalearner.ui.common.helper.MessagesHelper
import com.mathsemilio.hiraganalearner.ui.common.helper.ToolbarVisibilityHelper
import com.mathsemilio.hiraganalearner.ui.common.helper.InterstitialAdUseHelper
import com.mathsemilio.hiraganalearner.ui.screens.game.main.AlertUserHelper
import com.mathsemilio.hiraganalearner.domain.usecase.GetSymbolUseCase
import com.mathsemilio.hiraganalearner.ui.screens.game.result.ShareGameScoreHelper

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
    
    val mainScreenViewModel get() = MainScreenViewModel()

    fun getOnBackPressedCallback(onBackPressed: () -> Unit) =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = onBackPressed()
        }

    val interstitialAdHelper get() = InterstitialAdUseHelper(activity, context, adRequest)

    val alertUserHelper get() = AlertUserHelper(dialogHelper)

    val getSymbolUseCase get() = GetSymbolUseCase(preferencesRepository)

    val shareGameScoreHelper get() = ShareGameScoreHelper(context)
}