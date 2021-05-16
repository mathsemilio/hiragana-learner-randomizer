/*
Copyright 2020 Matheus Menezes

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.mathsemilio.hiraganalearner.domain.backend

import com.mathsemilio.hiraganalearner.common.observable.BaseObservable
import com.mathsemilio.hiraganalearner.domain.model.HiraganaSymbol

class BackendMediator(gameBackend: GameBackend) : BaseObservable<BackendMediator.Listener>(),
    GameBackend.Listener {

    interface Listener {
        fun onScoreUpdated(score: Int)

        fun onProgressUpdated(progress: Int)

        fun onCountdownTimeUpdated(countdownTime: Int)

        fun onRomanizationOptionsUpdated(romanizationOptions: List<String>)

        fun onHiraganaSymbolUpdated(symbol: HiraganaSymbol)

        fun onCorrectAnswer()

        fun onWrongAnswer()

        fun onGameTimeOver()
    }

    private val mediatorRequestListener = gameBackend as BackendMediatorRequestListener

    private lateinit var _currentSymbol: HiraganaSymbol
    val currentSymbol get() = _currentSymbol

    private var _currentScore = 0
    val currentScore get() = _currentScore

    private var _gameFinished = false
    val gameFinished get() = _gameFinished

    init {
        gameBackend.addListener(this)
    }

    fun startGame(difficultyValue: Int) {
        mediatorRequestListener.onStartGameRequested(difficultyValue)
    }

    fun checkAnswer(selectedRomanization: String) {
        mediatorRequestListener.onCheckAnswerRequested(selectedRomanization)
    }

    fun getNextSymbol() {
        mediatorRequestListener.onGetNextSymbolRequested()
    }

    fun pauseTimer() {
        mediatorRequestListener.onPauseTimerRequested()
    }

    fun resumeTimer() {
        mediatorRequestListener.onResumeGameRequested()
    }

    override fun onSymbolUpdated(symbol: HiraganaSymbol) {
        _currentSymbol = symbol
        listeners.forEach { listener -> listener.onHiraganaSymbolUpdated(symbol) }
    }

    override fun onScoreUpdated(score: Int) {
        _currentScore = score
        listeners.forEach { listener -> listener.onScoreUpdated(score) }
    }

    override fun onProgressUpdated(progress: Int) {
        listeners.forEach { listener -> listener.onProgressUpdated(progress) }
    }

    override fun onCountdownTimeUpdated(countdownTime: Int) {
        listeners.forEach { listener -> listener.onCountdownTimeUpdated(countdownTime) }
    }

    override fun onRomanizationOptionsUpdated(romanizations: List<String>) {
        listeners.forEach { listener -> listener.onRomanizationOptionsUpdated(romanizations) }
    }

    override fun onCorrectAnswer() {
        listeners.forEach { listener -> listener.onCorrectAnswer() }
    }

    override fun onWrongAnswer() {
        listeners.forEach { listener -> listener.onWrongAnswer() }
    }

    override fun onGameTimeOver() {
        listeners.forEach { listener -> listener.onGameTimeOver() }
    }

    override fun onGameFinished() {
        _gameFinished = true
    }
}