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
package com.mathsemilio.hiraganalearner.ui.common.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mathsemilio.hiraganalearner.ui.dialog.promptdialog.PromptDialogViewImpl
import com.mathsemilio.hiraganalearner.ui.screens.MainActivityViewImpl
import com.mathsemilio.hiraganalearner.ui.screens.main.GameMainScreenViewImpl
import com.mathsemilio.hiraganalearner.ui.screens.result.GameResultScreenViewImpl
import com.mathsemilio.hiraganalearner.ui.screens.welcome.GameWelcomeScreenViewImpl

class ViewFactory(private val inflater: LayoutInflater) {

    val mainActivityView get() = MainActivityViewImpl(inflater)

    val promptDialogView get() = PromptDialogViewImpl(inflater)

    fun getGameWelcomeScreenView(container: ViewGroup?) =
        GameWelcomeScreenViewImpl(inflater, container)

    fun getGameMainScreenView(container: ViewGroup?) =
        GameMainScreenViewImpl(inflater, container)

    fun getGameResultScreenView(container: ViewGroup?) =
        GameResultScreenViewImpl(inflater, container)
}