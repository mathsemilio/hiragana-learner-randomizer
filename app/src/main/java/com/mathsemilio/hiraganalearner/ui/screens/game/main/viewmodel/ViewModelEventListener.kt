package com.mathsemilio.hiraganalearner.ui.screens.game.main.viewmodel

import com.mathsemilio.hiraganalearner.domain.hiragana.HiraganaSymbol

interface ViewModelEventListener {
    fun onGameScoreUpdated(score: Int)
    fun onGameProgressUpdated(progressValue: Int)
    fun onGameCountDownTimeUpdated(countDownTime: Int)
    fun onRomanizationGroupUpdated(romanizationGroupList: List<String>)
    fun onCurrentHiraganaSymbolUpdated(newSymbol: HiraganaSymbol)
    fun onCorrectAnswer()
    fun onWrongAnswer()
    fun onGameTimeOver()
}