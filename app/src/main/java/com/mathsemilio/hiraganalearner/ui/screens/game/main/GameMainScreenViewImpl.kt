package com.mathsemilio.hiraganalearner.ui.screens.game.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.*
import com.mathsemilio.hiraganalearner.ui.screens.common.BaseObservableView

class GameMainScreenViewImpl(inflater: LayoutInflater, container: ViewGroup?) :
    BaseObservableView<GameMainScreenView.Listener>(), GameMainScreenView {

    private lateinit var textViewGameDifficulty: TextView
    private lateinit var textViewGameScore: TextView
    private lateinit var progressBarGameTimer: ProgressBar
    private lateinit var textViewCurrentHiraganaSymbol: TextView
    private lateinit var chipGroupRomanizationOptions: ChipGroup
    private lateinit var chipRomanizationOption1: Chip
    private lateinit var chipRomanizationOption2: Chip
    private lateinit var chipRomanizationOption3: Chip
    private lateinit var chipRomanizationOption4: Chip
    private lateinit var buttonExitGame: FloatingActionButton
    private lateinit var buttonPauseGame: FloatingActionButton
    private lateinit var buttonCheckAnswer: FloatingActionButton
    private lateinit var progressBarGameProgress: ProgressBar

    private var selectedRomanization = ""

    init {
        setRootView(inflater.inflate(R.layout.game_main_screen, container, false))
        initializeViews()
        attachClickListeners()
    }

    override fun setupUI(difficultyValue: Int) {
        setGameDifficultyTextBasedOnDifficultyValue(difficultyValue)
        setupGameTimerProgressBarMax(difficultyValue)
        attachRomanizationOptionsChipGroupOnCheckChangedListener()
    }

    override fun setGameDifficultyTextBasedOnDifficultyValue(difficultyValue: Int) {
        textViewGameDifficulty.text = when (difficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> getContext().getString(R.string.game_difficulty_beginner)
            GAME_DIFFICULTY_VALUE_MEDIUM -> getContext().getString(R.string.game_difficulty_medium)
            GAME_DIFFICULTY_VALUE_HARD -> getContext().getString(R.string.game_difficulty_hard)
            else -> throw IllegalArgumentException(ILLEGAL_GAME_DIFFICULTY_VALUE)
        }
    }

    override fun updateGameScoreTextView(newScore: Int) {
        textViewGameScore.text = getContext().getString(R.string.game_score, newScore)
    }

    override fun updateCurrentHiraganaSymbol(newSymbol: String) {
        textViewCurrentHiraganaSymbol.text = newSymbol
    }

    override fun updateProgressBarGameTimerProgressValue(updatedCountdownTime: Int) {
        progressBarGameTimer.progress = updatedCountdownTime
    }

    override fun updateRomanizationOptionsGroup(updatedRomanizationList: List<String>) {
        chipRomanizationOption1.text = updatedRomanizationList[0]
        chipRomanizationOption2.text = updatedRomanizationList[1]
        chipRomanizationOption3.text = updatedRomanizationList[2]
        chipRomanizationOption4.text = updatedRomanizationList[3]
    }

    override fun updateProgressBarGameProgressValue(updatedProgress: Int) {
        progressBarGameProgress.progress = updatedProgress
    }

    override fun clearRomanizationOptions() {
        chipGroupRomanizationOptions.clearCheck()
    }

    private fun initializeViews() {
        textViewGameDifficulty = findViewById(R.id.text_body_game_difficulty)
        textViewGameScore = findViewById(R.id.text_body_game_score)
        progressBarGameTimer = findViewById(R.id.game_timer_progress_bar)
        textViewCurrentHiraganaSymbol = findViewById(R.id.text_headline_current_hiragana_letter)
        chipGroupRomanizationOptions = findViewById(R.id.chip_group_romanization_options)
        chipRomanizationOption1 = findViewById(R.id.chip_button_option_1)
        chipRomanizationOption2 = findViewById(R.id.chip_button_option_2)
        chipRomanizationOption3 = findViewById(R.id.chip_button_option_3)
        chipRomanizationOption4 = findViewById(R.id.chip_button_option_4)
        buttonExitGame = findViewById(R.id.fab_exit)
        buttonPauseGame = findViewById(R.id.fab_pause)
        buttonCheckAnswer = findViewById(R.id.fab_check_answer)
        progressBarGameProgress = findViewById(R.id.progress_bar_game_progress)
    }

    private fun setupGameTimerProgressBarMax(difficultyValue: Int) {
        progressBarGameTimer.max = when (difficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> PROGRESS_BAR_MAX_VALUE_BEGINNER
            GAME_DIFFICULTY_VALUE_MEDIUM -> PROGRESS_BAR_MAX_VALUE_MEDIUM
            GAME_DIFFICULTY_VALUE_HARD -> PROGRESS_BAR_MAX_VALUE_HARD
            else -> throw IllegalArgumentException(ILLEGAL_GAME_DIFFICULTY_VALUE)
        }
    }

    private fun attachRomanizationOptionsChipGroupOnCheckChangedListener() {
        chipGroupRomanizationOptions.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == -1)
                buttonCheckAnswer.isEnabled = false
            else {
                onPlayClickSoundEffect()
                buttonCheckAnswer.isEnabled = true
                val checkedChip = group.findViewById<Chip>(checkedId)
                selectedRomanization = checkedChip.text.toString()
            }
        }
    }

    private fun attachClickListeners() {
        buttonExitGame.setOnClickListener { onExitGameButtonClicked() }
        buttonPauseGame.setOnClickListener { onPauseGameButtonClicked() }
        buttonCheckAnswer.setOnClickListener { onCheckAnswerButtonClicked(selectedRomanization) }
    }

    private fun onPlayClickSoundEffect() {
        getListeners().forEach { it.playClickSoundEffect() }
    }

    private fun onExitGameButtonClicked() {
        getListeners().forEach { it.onExitButtonClicked() }
    }

    private fun onPauseGameButtonClicked() {
        getListeners().forEach { it.onPauseButtonClicked() }
    }

    private fun onCheckAnswerButtonClicked(selectedRomanization: String) {
        getListeners().forEach { it.onCheckAnswerClicked(selectedRomanization) }
    }
}