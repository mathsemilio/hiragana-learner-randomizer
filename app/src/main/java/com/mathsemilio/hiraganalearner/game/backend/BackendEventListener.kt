package com.mathsemilio.hiraganalearner.game.backend

import com.mathsemilio.hiraganalearner.domain.hiragana.HiraganaSymbol

interface BackendEventListener {
    fun onSymbolUpdated(symbol: HiraganaSymbol)
    fun onGameScoreUpdated(score: Int)
    fun onGameProgressUpdated(progress: Int)
    fun onGameCountdownTimeUpdated(countDownTime: Int)
    fun onRomanizationGroupUpdated(romanizationGroupList: List<String>)
    fun onCorrectAnswer()
    fun onWrongAnswer()
    fun onGameTimeOver()
    fun onGameFinished()
}