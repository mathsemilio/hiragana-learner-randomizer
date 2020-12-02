package com.mathsemilio.hiraganalearner.logic.backend

import android.os.CountDownTimer
import com.mathsemilio.hiraganalearner.commom.*
import com.mathsemilio.hiraganalearner.logic.eventWrapper.BackendEvent
import com.mathsemilio.hiraganalearner.logic.eventWrapper.UIRequest
import com.mathsemilio.hiraganalearner.others.hiraganaSymbolsList
import kotlin.random.Random

class GameBackend : IObserverContract {

    private lateinit var countDownTimer: CountDownTimer

    private val backendObservers = mutableListOf<IBackendObserver>()
    private val hiraganaSymbolList = hiraganaSymbolsList.toMutableList()

    private var difficultyCountDownTime = 0L
    private var currentCountDownTime = 0L
    private var score = 0
    private var progress = 0

    private var firstRomanizationGroupString = ""
    private var secondRomanizationGroupString = ""
    private var thirdRomanizationGroupString = ""
    private var fourthRomanizationGroupString = ""

    fun getUIRequest(uiRequest: UIRequest) {
        when (uiRequest) {
            is UIRequest.StartGame -> startGame(uiRequest.gameDifficultyValue)
            is UIRequest.CheckAnswer -> checkAnswer(uiRequest.selectedRomanization)
            UIRequest.GetNextSymbol -> getNextSymbol()
            UIRequest.PauseTimer -> cancelTimer()
            UIRequest.RestartTimer -> resumeTimer()
        }
    }

    private fun startGame(gameDifficultyValue: Int) {
        difficultyCountDownTime = when (gameDifficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> COUNTDOWN_TIME_BEGINNER
            GAME_DIFFICULTY_VALUE_MEDIUM -> COUNTDOWN_TIME_MEDIUM
            GAME_DIFFICULTY_VALUE_HARD -> COUNTDOWN_TIME_HARD
            else -> throw IllegalArgumentException(INVALID_GAME_DIFFICULTY_VALUE_EXCEPTION)
        }

        hiraganaSymbolList.shuffle()

        notifyObserver(BackendEvent.NewSymbol(hiraganaSymbolList.first()))
        generateRomanizationGroup()
        startTimer(difficultyCountDownTime)
    }

    private fun startTimer(difficultyCountDownTimer: Long) {
        countDownTimer = object : CountDownTimer(difficultyCountDownTimer, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                currentCountDownTime = (millisUntilFinished / 1000).also {
                    notifyObserver(BackendEvent.GameCountDownTimeUpdated(it.toInt()))
                }
            }

            override fun onFinish() {
                currentCountDownTime = 0L
                notifyObserver(BackendEvent.GameTimeOver)
            }
        }
        countDownTimer.start()
    }

    private fun cancelTimer() = countDownTimer.cancel()

    private fun resumeTimer() = startTimer(currentCountDownTime.times(ONE_SECOND))

    private fun checkAnswer(selectedRomanization: String) {
        cancelTimer()

        if (hiraganaSymbolList.first().romanization == selectedRomanization) {
            notifyObserver(BackendEvent.GameScoreUpdated(++score))
            notifyObserver(BackendEvent.CorrectAnswer)
        } else {
            notifyObserver(BackendEvent.WrongAnswer)
        }
    }

    private fun getNextSymbol() {
        notifyObserver(BackendEvent.GameProgressUpdated(++progress))

        hiraganaSymbolList.removeAt(0)

        notifyObserver(BackendEvent.NewSymbol(hiraganaSymbolList.first()))

        if (hiraganaSymbolList.size == 1) {
            generateRomanizationGroup()
            startTimer(difficultyCountDownTime)
            notifyObserver(BackendEvent.GameFinished)
        } else {
            generateRomanizationGroup()
            startTimer(difficultyCountDownTime)
        }
    }

    private fun generateRomanizationGroup() {
        val romanizationList = arrayOf(
            "A", "I", "U", "E", "O", "KA", "KI", "KU", "KE", "KO", "SA", "SHI", "SU", "SE", "SO",
            "TA", "CHI", "TSU", "TE", "TO", "NA", "NI", "NU", "NE", "NO", "HA", "HI", "FU", "HE",
            "HO", "MA", "MI", "MU", "ME", "MO", "YA", "YU", "YO", "RA", "RI", "RU", "RE", "RO",
            "WA", "WI", "WE", "WO", "N"
        ).let { romanizationList ->
            romanizationList.shuffle()
            romanizationList.filterNot { it == hiraganaSymbolList.first().romanization }
        }

        firstRomanizationGroupString = romanizationList.slice(0..11).random()
        secondRomanizationGroupString = romanizationList.slice(12..23).random()
        thirdRomanizationGroupString = romanizationList.slice(24..35).random()
        fourthRomanizationGroupString = romanizationList.slice(36..46).random()

        setCorrectRomanizationAnswer()

        notifyObserver(
            BackendEvent.RomanizationGroupUpdated(
                listOf(
                    firstRomanizationGroupString,
                    secondRomanizationGroupString,
                    thirdRomanizationGroupString,
                    fourthRomanizationGroupString
                )
            )
        )
    }

    private fun setCorrectRomanizationAnswer() {
        when (Random.nextInt(4)) {
            0 -> firstRomanizationGroupString = hiraganaSymbolList.first().romanization
            1 -> secondRomanizationGroupString = hiraganaSymbolList.first().romanization
            2 -> thirdRomanizationGroupString = hiraganaSymbolList.first().romanization
            3 -> fourthRomanizationGroupString = hiraganaSymbolList.first().romanization
        }
    }

    override fun registerObserver(IBackendObserver: IBackendObserver) {
        if (!backendObservers.contains(IBackendObserver))
            backendObservers.add(IBackendObserver)
    }

    override fun removeObserver(IBackendObserver: IBackendObserver) {
        if (backendObservers.contains(IBackendObserver))
            backendObservers.remove(IBackendObserver)
    }

    override fun notifyObserver(event: BackendEvent) {
        backendObservers.forEach { it.onBackendEvent(event) }
    }
}