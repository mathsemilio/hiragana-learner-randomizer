package com.mathsemilio.hiraganalearner.ui.screens.game.main.viewmodel

import com.mathsemilio.hiraganalearner.domain.hiragana.HiraganaSymbol

interface ViewModelEventListener {
    fun onGameScoreUpdated(newScore: Int)
    fun onGameProgressUpdated(updatedProgress: Int)
    fun onGameCountDownTimeUpdated(updatedCountdownTime: Int)
    fun onRomanizationGroupUpdated(updatedRomanizationGroupList: List<String>)
    fun onCurrentHiraganaSymbolUpdated(newSymbol: HiraganaSymbol)
    fun onCorrectAnswer()
    fun onWrongAnswer()
    fun onGameTimeOver()
}