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

import com.mathsemilio.hiraganalearner.common.provider.BackPressedCallbackProvider
import com.mathsemilio.hiraganalearner.domain.backend.BackendMediator
import com.mathsemilio.hiraganalearner.domain.backend.GameBackend
import com.mathsemilio.hiraganalearner.others.notification.TrainingNotificationScheduler
import com.mathsemilio.hiraganalearner.ui.common.helper.InterstitialAdHelper
import com.mathsemilio.hiraganalearner.ui.common.manager.DialogManager
import com.mathsemilio.hiraganalearner.ui.common.manager.MessagesManager

class ControllerCompositionRoot(private val activityCompositionRoot: ActivityCompositionRoot) {

    private val activity get() = activityCompositionRoot.getActivity()

    private val applicationContext get() = activityCompositionRoot.applicationContext

    private val fragmentManager get() = activityCompositionRoot.fragmentManager

    val adRequest get() = activityCompositionRoot.adRequest

    val backendMediator get() = BackendMediator(GameBackend())

    val backPressedCallbackProvider get() = BackPressedCallbackProvider

    val dialogManager get() = DialogManager(fragmentManager, applicationContext)

    val eventPublisher get() = activityCompositionRoot.eventPublisher

    val eventSubscriber get() = activityCompositionRoot.eventSubscriber

    val interstitialAdHelper get() = InterstitialAdHelper(applicationContext, adRequest, activity)

    val messagesManager get() = MessagesManager(applicationContext)

    val preferencesManager get() = activityCompositionRoot.preferencesManager

    val soundEffectsModule get() = activityCompositionRoot.soundEffectsModule

    val screensNavigator get() = activityCompositionRoot.screensNavigator

    val themeHelper get() = activityCompositionRoot.themeHelper

    val trainingNotificationScheduler get() = TrainingNotificationScheduler(applicationContext)

    val toolbarVisibilityHelper get() = activityCompositionRoot.toolbarVisibilityHelper
}