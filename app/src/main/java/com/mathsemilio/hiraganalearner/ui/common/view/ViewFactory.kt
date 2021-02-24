package com.mathsemilio.hiraganalearner.ui.common.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mathsemilio.hiraganalearner.ui.screens.MainActivityViewImpl
import com.mathsemilio.hiraganalearner.ui.screens.game.main.GameMainScreenViewImpl
import com.mathsemilio.hiraganalearner.ui.screens.game.result.GameResultScreenViewImpl
import com.mathsemilio.hiraganalearner.ui.screens.game.welcome.GameWelcomeScreenViewImpl

class ViewFactory(private val layoutInflater: LayoutInflater) {

    fun getMainActivityView(parent: ViewGroup?) =
        MainActivityViewImpl(layoutInflater, parent)

    fun getGameWelcomeScreenView(container: ViewGroup?) =
        GameWelcomeScreenViewImpl(layoutInflater, container)

    fun getGameMainScreenView(container: ViewGroup?) =
        GameMainScreenViewImpl(layoutInflater, container)

    fun getGameResultScreenView(container: ViewGroup?) =
        GameResultScreenViewImpl(layoutInflater, container)
}