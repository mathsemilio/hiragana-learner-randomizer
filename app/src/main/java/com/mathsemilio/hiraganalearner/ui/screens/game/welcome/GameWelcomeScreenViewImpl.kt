package com.mathsemilio.hiraganalearner.ui.screens.game.welcome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.*
import com.mathsemilio.hiraganalearner.ui.screens.common.BaseObservableView

class GameWelcomeScreenViewImpl(inflater: LayoutInflater, container: ViewGroup?) :
    BaseObservableView<GameWelcomeScreenView.Listener>(), GameWelcomeScreenView {

    private lateinit var imageViewSettingsIcon: ImageView
    private lateinit var textViewOnDifficulty: TextView
    private lateinit var textViewSelectADifficulty: TextView
    private lateinit var chipGroupDifficultyOptions: ChipGroup
    private lateinit var buttonStart: Button

    private var difficultyValue = 0
    private var defaultDifficultyValue = ""

    init {
        setRootView(inflater.inflate(R.layout.game_welcome_screen, container, false))
        initializeViews()
        attachClickListeners()
    }

    override fun setupUI(defaultDifficultyValueFromPreference: String) {
        getGameDefaultDifficultyValue(defaultDifficultyValueFromPreference)

        if (defaultDifficultyValueFromPreference != "0")
            setupUIForDifficultyPreSelected()
    }

    override fun getDifficultyValue(): Int {
        return difficultyValue
    }

    private fun getGameDefaultDifficultyValue(defaultDifficultyValueFromPreference: String) {
        defaultDifficultyValue = when (defaultDifficultyValueFromPreference) {
            "0" -> SHOW_DIFFICULTY_OPTIONS
            "1" -> DEFAULT_DIFFICULTY_BEGINNER
            "2" -> DEFAULT_DIFFICULTY_MEDIUM
            "3" -> DEFAULT_DIFFICULTY_HARD
            else -> throw IllegalArgumentException(ILLEGAL_DEFAULT_DIFFICULTY_VALUE)
        }.also { defaultDifficulty ->
            when (defaultDifficulty) {
                SHOW_DIFFICULTY_OPTIONS -> attachChipGroupDifficultyOptionsOnCheckedChangeListener()
                DEFAULT_DIFFICULTY_BEGINNER -> difficultyValue = GAME_DIFFICULTY_VALUE_BEGINNER
                DEFAULT_DIFFICULTY_MEDIUM -> difficultyValue = GAME_DIFFICULTY_VALUE_MEDIUM
                DEFAULT_DIFFICULTY_HARD -> difficultyValue = GAME_DIFFICULTY_VALUE_HARD
            }
        }
    }

    private fun initializeViews() {
        imageViewSettingsIcon = findViewById(R.id.image_view_settings_icon)
        textViewOnDifficulty = findViewById(R.id.text_body_on_game_difficulty)
        textViewSelectADifficulty = findViewById(R.id.text_body_select_a_difficulty)
        chipGroupDifficultyOptions = findViewById(R.id.chip_group_game_difficulty)
        buttonStart = findViewById(R.id.button_start)
    }

    private fun setupUIForDifficultyPreSelected() {
        setupOnGameDifficultyTextView()

        textViewSelectADifficulty.visibility = View.INVISIBLE
        chipGroupDifficultyOptions.visibility = View.INVISIBLE

        buttonStart.isEnabled = true
    }

    private fun setupOnGameDifficultyTextView() {
        textViewOnDifficulty.apply {
            visibility = View.VISIBLE
            text = context.getString(
                R.string.on_game_difficulty, when (difficultyValue) {
                    GAME_DIFFICULTY_VALUE_BEGINNER -> context.getString(R.string.game_difficulty_beginner)
                    GAME_DIFFICULTY_VALUE_MEDIUM -> context.getString(R.string.game_difficulty_medium)
                    GAME_DIFFICULTY_VALUE_HARD -> context.getString(R.string.game_difficulty_hard)
                    else -> throw IllegalArgumentException(ILLEGAL_GAME_DIFFICULTY_VALUE)
                }
            )
        }
    }

    private fun attachClickListeners() {
        imageViewSettingsIcon.setOnClickListener { onNavigateToSettingsScreen() }
        buttonStart.setOnClickListener { onNavigateToMainScreen(difficultyValue) }
    }

    private fun attachChipGroupDifficultyOptionsOnCheckedChangeListener() {
        chipGroupDifficultyOptions.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == -1)
                buttonStart.isEnabled = false
            else {
                onPlayClickSoundEffect()
                buttonStart.isEnabled = true
                val checkedChip = group.findViewById<Chip>(checkedId)
                setDifficultyValueBasedOnChipText(checkedChip.text.toString())
            }
        }
    }

    private fun setDifficultyValueBasedOnChipText(chipText: String) {
        difficultyValue = when (chipText) {
            getContext().getString(R.string.game_difficulty_beginner) -> GAME_DIFFICULTY_VALUE_BEGINNER
            getContext().getString(R.string.game_difficulty_medium) -> GAME_DIFFICULTY_VALUE_MEDIUM
            getContext().getString(R.string.game_difficulty_hard) -> GAME_DIFFICULTY_VALUE_HARD
            else -> throw IllegalArgumentException(ILLEGAL_GAME_DIFFICULTY_SETTING)
        }
    }

    private fun onPlayClickSoundEffect() {
        getListeners().forEach { it.onPlayClickSoundEffect() }
    }

    private fun onNavigateToSettingsScreen() {
        getListeners().forEach { it.onSettingsIconClicked() }
    }

    private fun onNavigateToMainScreen(difficultyValue: Int) {
        getListeners().forEach { it.onStartButtonClicked(difficultyValue) }
    }
}