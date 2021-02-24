package com.mathsemilio.hiraganalearner.domain.usecase

import com.mathsemilio.hiraganalearner.common.PERFECT_SCORE
import com.mathsemilio.hiraganalearner.common.observable.BaseObservable
import com.mathsemilio.hiraganalearner.data.repository.PreferencesRepository

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
        listeners.forEach { it.onGetNextSymbol() }
    }

    private fun onShowInterstitialAd() {
        listeners.forEach { it.onShowInterstitialAd() }
    }
}