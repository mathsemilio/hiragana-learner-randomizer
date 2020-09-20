package com.mathsemilio.hiraganalearner.ui.fragment

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.chip.Chip
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.databinding.MainGameScreenBinding
import com.mathsemilio.hiraganalearner.others.*
import com.mathsemilio.hiraganalearner.ui.viewModel.MainGameViewModel
import com.mathsemilio.hiraganalearner.ui.viewModel.MainGameViewModelFactory

/**
 * Fragment class for the main game screen
 */
class MainGameScreen : Fragment() {

    /**
     * Enum for representing the Fragment States, used to manage how the timer will behave
     * during the fragment's lifecycle states.
     */
    private enum class FragmentState { RUNNING, PAUSED, DIALOG_BEING_SHOWN }

    private lateinit var binding: MainGameScreenBinding
    private lateinit var viewModelFactory: MainGameViewModelFactory
    private lateinit var viewModel: MainGameViewModel
    private lateinit var soundPool: SoundPool
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private var currentFragmentState = FragmentState.RUNNING
    private var gameDifficultyValue = 0
    private var soundEffectsVolume = 0f
    private var soundClick = 0
    private var soundButtonClick = 0
    private var soundCorrectAnswer = 0
    private var soundWrongAnswer = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_game_screen, container, false)

        initializeVariables()

        setupUI()

        subscribeToObservers()

        setupSoundPoolAndLoadSounds()

        return binding.root
    }

    /**
     * Initializes and assigns key components for this fragment.
     */
    private fun initializeVariables() {
        gameDifficultyValue = MainGameScreenArgs.fromBundle(requireArguments()).gameDifficultyValue

        viewModelFactory = MainGameViewModelFactory(gameDifficultyValue)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainGameViewModel::class.java)

        binding.mainGameViewModel = viewModel

        binding.lifecycleOwner = this

        soundEffectsVolume = PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getInt(SOUND_EFFECTS_VOLUME_PREF_KEY, 0).toFloat().div(10f)
    }

    /**
     * Sets up the UI, attaches listeners for the Floating Action Buttons and configures a
     * onBackPressed callback.
     */
    private fun setupUI() {
        binding.textBodyGameDifficulty.text = when (gameDifficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> getString(R.string.game_difficulty_beginner)
            GAME_DIFFICULTY_VALUE_MEDIUM -> getString(R.string.game_difficulty_medium)
            else -> getString(R.string.game_difficulty_hard)
        }

        binding.chipGroupRomanizationOptions.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == -1) {
                binding.fabCheckAnswer.isEnabled = false
            } else {
                soundPool.play(soundClick, soundEffectsVolume, soundEffectsVolume, 0, 0, 1F)

                binding.fabCheckAnswer.isEnabled = true

                val selectedChip = group.findViewById<Chip>(checkedId)
                val selectedRomanization: String = selectedChip.text.toString()

                binding.fabCheckAnswer.setOnClickListener {
                    viewModel.countDownTimer.cancel()

                    currentFragmentState = FragmentState.DIALOG_BEING_SHOWN

                    if (viewModel.hiraganaSymbolsMutableList.size == 1) {
                        viewModel.getLastLetter(selectedRomanization)
                    } else {
                        viewModel.checkUserInput(selectedRomanization)
                    }
                }
            }
        }

        binding.fabExit.setOnClickListener {
            soundPool.play(soundButtonClick, soundEffectsVolume, soundEffectsVolume, 0, 0, 1F)

            currentFragmentState = FragmentState.DIALOG_BEING_SHOWN

            viewModel.countDownTimer.cancel()

            requireContext().buildMaterialDialog(
                getString(R.string.alert_dialog_exit_game_title),
                getString(R.string.alert_dialog_exit_game_message),
                getString(R.string.alert_dialog_exit_game_positive_button_text),
                getString(R.string.alert_dialog_exit_game_negative_button_text),
                false,
                { _, _ ->
                    soundPool.play(
                        soundButtonClick,
                        soundEffectsVolume,
                        soundEffectsVolume,
                        0,
                        0,
                        1F
                    )
                    findNavController().navigate(R.id.action_mainGameScreen_to_gameWelcomeScreen)
                },
                { _, _ ->
                    soundPool.play(
                        soundButtonClick,
                        soundEffectsVolume,
                        soundEffectsVolume,
                        0,
                        0,
                        1F
                    )

                    viewModel.startGameTimer(viewModel.currentGameTime.value!!.times(1000))
                    currentFragmentState = FragmentState.RUNNING
                }
            )
        }

        binding.fabPauseTimer.setOnClickListener {
            soundPool.play(soundButtonClick, soundEffectsVolume, soundEffectsVolume, 0, 0, 1F)

            currentFragmentState = FragmentState.DIALOG_BEING_SHOWN

            viewModel.countDownTimer.cancel()

            requireContext().buildMaterialDialog(
                getString(R.string.alert_dialog_game_paused_title),
                getString(R.string.alert_dialog_game_paused_message),
                getString(R.string.alert_dialog_game_paused_positive_button_text),
                null, false,
                { _, _ ->
                    soundPool.play(
                        soundButtonClick,
                        soundEffectsVolume,
                        soundEffectsVolume,
                        0,
                        0,
                        1F
                    )
                    viewModel.startGameTimer(viewModel.currentGameTime.value!!.times(1000))
                    currentFragmentState = FragmentState.RUNNING
                },
                null
            )
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentFragmentState = FragmentState.DIALOG_BEING_SHOWN

                viewModel.countDownTimer.cancel()

                requireContext().buildMaterialDialog(
                    getString(R.string.alert_dialog_on_back_pressed_title),
                    getString(R.string.alert_dialog_on_back_pressed_message),
                    getString(R.string.alert_dialog_on_back_pressed_positive_button_text),
                    getString(R.string.alert_dialog_on_back_pressed_negative_button_text),
                    false,
                    { _, _ ->
                        soundPool.play(
                            soundButtonClick,
                            soundEffectsVolume,
                            soundEffectsVolume,
                            0,
                            0,
                            1F
                        )
                        viewModel.startGameTimer(viewModel.currentGameTime.value!!.times(1000))
                        currentFragmentState = FragmentState.RUNNING
                    },
                    { _, _ ->
                        soundPool.play(
                            soundButtonClick,
                            soundEffectsVolume,
                            soundEffectsVolume,
                            0,
                            0,
                            1F
                        )

                        findNavController().navigate(R.id.action_mainGameScreen_to_gameWelcomeScreen)
                    }
                )
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    /**
     * Subscribes to the observers to observe game event related LiveData variables.
     */
    private fun subscribeToObservers() {
        viewModel.eventCorrectAnswer.observe(viewLifecycleOwner, { answerIsCorrect ->
            if (answerIsCorrect) {
                soundPool.play(soundCorrectAnswer, soundEffectsVolume, soundEffectsVolume, 1, 0, 1F)

                requireContext().buildMaterialDialog(
                    getString(R.string.alert_dialog_correct_answer_title),
                    getString(R.string.alert_dialog_correct_answer_message),
                    getString(R.string.alert_dialog_correct_answer_positive_button_text),
                    null,
                    false,
                    { _, _ -> checkEventGameFinished() },
                    null
                )
            } else {
                soundPool.play(soundWrongAnswer, soundEffectsVolume, soundEffectsVolume, 1, 0, 1F)

                requireContext().buildMaterialDialog(
                    getString(R.string.alert_dialog_wrong_answer_title),
                    getString(
                        R.string.alert_dialog_wrong_answer_message,
                        viewModel.currentHiraganaLetterRomanization.value!!
                    ),
                    getString(R.string.alert_dialog_wrong_answer_positive_button_text),
                    null,
                    false,
                    { _, _ -> checkEventGameFinished() },
                    null
                )
            }
        })

        viewModel.eventTimeOver.observe(viewLifecycleOwner, { timeIsOver ->
            if (timeIsOver) {
                soundPool.play(soundWrongAnswer, soundEffectsVolume, soundEffectsVolume, 1, 0, 1F)

                requireContext().buildMaterialDialog(
                    getString(R.string.alert_dialog_time_over_title),
                    getString(
                        R.string.alert_dialog_time_over_message,
                        viewModel.currentHiraganaLetterRomanization.value!!
                    ),
                    getString(R.string.alert_dialog_time_over_positive_button_text),
                    null,
                    false,
                    { _, _ -> checkEventGameFinished() },
                    null
                )
            }
        })
    }

    /**
     * Sets up Sound Pool and loads the sounds to be used in this fragment.
     */
    private fun setupSoundPoolAndLoadSounds() {
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_GAME)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        soundClick = soundPool.load(requireContext(), R.raw.brandondelehoy_series_of_clicks, 1)
        soundButtonClick = soundPool.load(requireContext(), R.raw.jaoreir_button_simple_01, 1)
        soundCorrectAnswer =
            soundPool.load(requireContext(), R.raw.mativve_electro_success_sound, 1)
        soundWrongAnswer = soundPool.load(requireContext(), R.raw.autistic_lucario_error, 1)
    }

    /**
     * Navigates, checks if the game score is equal to a perfect score, and passes the game
     * difficulty value and the game score to GameScoreScreen.
     *
     * @param gameScore Integer that represents the game score to sent to the GameScoreScreen
     * @param gameDifficultyValue Integer that represents the game difficulty value to be sent
     * to the GameScoreScreen.
     */
    private fun navigateToScoreScreen(gameScore: Int, gameDifficultyValue: Int) {
        if (gameScore == PERFECT_SCORE)
            SharedPreferencesPerfectScores(requireContext()).updatePerfectScore()

        val action = MainGameScreenDirections.actionMainGameScreenToGameScoreScreen(
            gameScore,
            gameDifficultyValue
        )

        findNavController().navigate(action)
    }

    /**
     * Checks if the eventGameFinished value is true, if it is, the navigateToScreen will be
     * called, else, the chip group will be cleared of any checked buttons and the getNextLetter
     * function from the ViewModel will be called.
     */
    private fun checkEventGameFinished() {
        if (viewModel.eventGameFinished.value == true) {
            navigateToScoreScreen(viewModel.gameScore.value!!, gameDifficultyValue)
        } else {
            binding.chipGroupRomanizationOptions.clearCheck()
            viewModel.getNextLetter()
        }
    }

    override fun onPause() {
        viewModel.countDownTimer.cancel()

        if (currentFragmentState == FragmentState.RUNNING)
            currentFragmentState = FragmentState.PAUSED

        super.onPause()
    }

    override fun onResume() {
        if (currentFragmentState == FragmentState.PAUSED) {
            viewModel.startGameTimer(viewModel.currentGameTime.value!!.times(1000))
        }

        super.onResume()
    }
}