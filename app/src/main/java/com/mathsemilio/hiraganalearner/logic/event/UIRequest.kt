package com.mathsemilio.hiraganalearner.logic.event

sealed class UIRequest {
    data class StartGame(val gameDifficultyValue: Int) : UIRequest()
    data class CheckAnswer(val selectedRomanization: String) : UIRequest()
    object GetNextSymbol : UIRequest()
    object PauseTimer : UIRequest()
    object RestartTimer : UIRequest()
}