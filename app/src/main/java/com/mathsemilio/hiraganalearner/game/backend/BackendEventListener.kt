package com.mathsemilio.hiraganalearner.game.backend

import com.mathsemilio.hiraganalearner.domain.hiragana.HiraganaSymbol

interface BackendEventListener {
    fun onSymbolUpdated(newSymbol: HiraganaSymbol)
    fun onGameScoreUpdated(newScore: Int)
    fun onGameProgressUpdated(updatedProgress: Int)
    fun onGameCountdownTimeUpdated(updatedCountdownTime: Int)
    fun onRomanizationGroupUpdated(updatedRomanizationGroupList: List<String>)
    fun onCorrectAnswer()
    fun onWrongAnswer()
    fun onGameTimeOver()
    fun onGameFinished()
}