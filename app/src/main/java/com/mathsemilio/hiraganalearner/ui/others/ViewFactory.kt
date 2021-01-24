package com.mathsemilio.hiraganalearner.ui.others

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mathsemilio.hiraganalearner.ui.screens.game.main.GameMainScreenViewImpl
import com.mathsemilio.hiraganalearner.ui.screens.game.result.GameResultScreenViewImpl
import com.mathsemilio.hiraganalearner.ui.screens.game.welcome.GameWelcomeScreenViewImpl

class ViewFactory(private val mInflater: LayoutInflater) {

    fun getGameWelcomeScreenView(container: ViewGroup?) =
        GameWelcomeScreenViewImpl(mInflater, container)

    fun getGameMainScreenView(container: ViewGroup?) =
        GameMainScreenViewImpl(mInflater, container)

    fun getGameResultScreenView(container: ViewGroup?) =
        GameResultScreenViewImpl(mInflater, container)
}