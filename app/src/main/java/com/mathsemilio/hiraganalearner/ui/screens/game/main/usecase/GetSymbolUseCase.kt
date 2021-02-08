package com.mathsemilio.hiraganalearner.ui.screens.game.main.usecase

import com.mathsemilio.hiraganalearner.common.PERFECT_SCORE
import com.mathsemilio.hiraganalearner.common.baseobservable.BaseObservable
import com.mathsemilio.hiraganalearner.data.preferences.repository.PreferencesRepository

class GetSymbolUseCase(private val preferencesRepository: PreferencesRepository) :
    BaseObservable<GetSymbolUseCase.Listener>() {

    interface Listener {
        fun onGetNextSymbol()
        fun onShowInterstitialAd()
    }

    fun getNextSymbol(gameFinished: Boolean, score: Int) {
        if (gameFinished) {
            if (score == PERFECT_SCORE)
                preferencesRepository.incrementPerfectScoresValue()

            onShowInterstitialAd()
        } else {
            onGetNextSymbol()
        }
    }

    private fun onGetNextSymbol() {
        getListeners().forEach { it.onGetNextSymbol() }
    }

    private fun onShowInterstitialAd() {
        getListeners().forEach { it.onShowInterstitialAd() }
    }
}