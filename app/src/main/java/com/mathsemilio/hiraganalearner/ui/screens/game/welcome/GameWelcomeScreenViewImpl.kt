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

    private lateinit var mImageViewSettingsIcon: ImageView
    private lateinit var mTextViewOnDifficulty: TextView
    private lateinit var mTextViewSelectADifficulty: TextView
    private lateinit var mChipGroupDifficultyOptions: ChipGroup
    private lateinit var mButtonStart: Button

    private var mDifficultyValue = 0
    private var mDefaultDifficultyValue = ""

    init {
        setRootView(inflater.inflate(R.layout.game_welcome_screen, container, false))
        initializeViews()
    }

    override fun onControllerViewCreated(difficultyValueFromPreference: String) {
        getGameDefaultDifficultyValue(difficultyValueFromPreference)

        if (mDefaultDifficultyValue != "0") setupUIForDifficultyPreSelected()

        attachClickListeners()
    }

    override fun getDifficultyValue(): Int {
        return mDifficultyValue
    }

    private fun initializeViews() {
        mImageViewSettingsIcon = findViewById(R.id.image_view_settings_icon)
        mTextViewOnDifficulty = findViewById(R.id.text_body_on_game_difficulty)
        mTextViewSelectADifficulty = findViewById(R.id.text_body_select_a_difficulty)
        mChipGroupDifficultyOptions = findViewById(R.id.chip_group_game_difficulty)
        mButtonStart = findViewById(R.id.button_start)
    }

    private fun getGameDefaultDifficultyValue(defaultDifficultyValueFromPreference: String) {
        mDefaultDifficultyValue = when (defaultDifficultyValueFromPreference) {
            "0" -> SHOW_DIFFICULTY_OPTIONS
            "1" -> DEFAULT_DIFFICULTY_BEGINNER
            "2" -> DEFAULT_DIFFICULTY_MEDIUM
            "3" -> DEFAULT_DIFFICULTY_HARD
            else -> throw IllegalArgumentException(ILLEGAL_DEFAULT_DIFFICULTY_VALUE)
        }.also { defaultDifficulty ->
            when (defaultDifficulty) {
                SHOW_DIFFICULTY_OPTIONS -> attachChipGroupDifficultyOptionsOnCheckedChangeListener()
                DEFAULT_DIFFICULTY_BEGINNER -> mDifficultyValue = GAME_DIFFICULTY_VALUE_BEGINNER
                DEFAULT_DIFFICULTY_MEDIUM -> mDifficultyValue = GAME_DIFFICULTY_VALUE_MEDIUM
                DEFAULT_DIFFICULTY_HARD -> mDifficultyValue = GAME_DIFFICULTY_VALUE_HARD
            }
        }
    }

    private fun setupUIForDifficultyPreSelected() {
        setupOnGameDifficultyTextView()

        mTextViewSelectADifficulty.visibility = View.INVISIBLE
        mChipGroupDifficultyOptions.visibility = View.INVISIBLE

        mButtonStart.isEnabled = true
    }

    private fun setupOnGameDifficultyTextView() {
        mTextViewOnDifficulty.apply {
            visibility = View.VISIBLE
            text = context.getString(
                R.string.on_game_difficulty, when (mDifficultyValue) {
                    GAME_DIFFICULTY_VALUE_BEGINNER -> context.getString(R.string.game_difficulty_beginner)
                    GAME_DIFFICULTY_VALUE_MEDIUM -> context.getString(R.string.game_difficulty_medium)
                    GAME_DIFFICULTY_VALUE_HARD -> context.getString(R.string.game_difficulty_hard)
                    else -> throw IllegalArgumentException(ILLEGAL_GAME_DIFFICULTY_VALUE)
                }
            )
        }
    }

    private fun attachClickListeners() {
        mImageViewSettingsIcon.setOnClickListener { navigateToSettingsScreen() }
        mButtonStart.setOnClickListener { navigateToMainScreen(mDifficultyValue) }
    }

    private fun attachChipGroupDifficultyOptionsOnCheckedChangeListener() {
        mChipGroupDifficultyOptions.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == -1)
                mButtonStart.isEnabled = false
            else {
                playClickSoundEffect()
                mButtonStart.isEnabled = true
                val checkedChip = group.findViewById<Chip>(checkedId)
                setDifficultyValueBasedOnChipText(checkedChip.text.toString())
            }
        }
    }

    private fun setDifficultyValueBasedOnChipText(chipText: String) {
        mDifficultyValue = when (chipText) {
            getContext().getString(R.string.game_difficulty_beginner) -> GAME_DIFFICULTY_VALUE_BEGINNER
            getContext().getString(R.string.game_difficulty_medium) -> GAME_DIFFICULTY_VALUE_MEDIUM
            getContext().getString(R.string.game_difficulty_hard) -> GAME_DIFFICULTY_VALUE_HARD
            else -> throw IllegalArgumentException(ILLEGAL_GAME_DIFFICULTY_SETTING)
        }
    }

    private fun playClickSoundEffect() {
        getListeners().forEach { it.onPlayClickSoundEffect() }
    }

    private fun navigateToSettingsScreen() {
        getListeners().forEach { it.onSettingsIconClicked() }
    }

    private fun navigateToMainScreen(difficultyValue: Int) {
        getListeners().forEach { it.onStartButtonClicked(difficultyValue) }
    }
}