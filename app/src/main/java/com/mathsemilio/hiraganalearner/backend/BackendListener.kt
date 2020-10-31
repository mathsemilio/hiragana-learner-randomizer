package com.mathsemilio.hiraganalearner.backend

interface BackendListener {
    fun onCorrectAnswer()
    fun onWrongAnswer(correctRomanization: String)
    fun onCountDownTimeFinished(romanizationAnswer: String)
    fun onGameFinished()
}