package com.mathsemilio.hiraganalearner.logic.event

import com.mathsemilio.hiraganalearner.data.model.HiraganaSymbol

sealed class BackendEvent {
    data class NewSymbol(val currentSymbol: HiraganaSymbol) : BackendEvent()
    data class GameScoreUpdated(val score: Int) : BackendEvent()
    data class GameProgressUpdated(val progressValue: Int) : BackendEvent()
    data class GameCountDownTimeUpdated(val countDownTimeValue: Int) : BackendEvent()
    data class RomanizationGroupUpdated(val romanizationGroupList: List<String>) : BackendEvent()
    object CorrectAnswer : BackendEvent()
    object WrongAnswer : BackendEvent()
    object GameTimeOver : BackendEvent()
    object GameFinished : BackendEvent()
}