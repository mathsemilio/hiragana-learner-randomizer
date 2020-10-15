package com.mathsemilio.hiraganalearner.ui.fragment

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
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.chip.Chip
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.databinding.GameMainScreenBinding
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

    private var _binding: GameMainScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelFactory: MainGameViewModelFactory
    private lateinit var viewModel: MainGameViewModel
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private lateinit var interstitialAd: InterstitialAd
    private var currentFragmentState = FragmentState.RUNNING
    private var soundPool: SoundPool? = null
    private var gameDifficultyValue = 0
    private var soundEffectsEnabled = true
    private var soundEffectsVolume = 0f
    private var soundClick = 0
    private var soundButtonClick = 0
    private var soundCorrectAnswer = 0
    private var soundWrongAnswer = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.game_main_screen, container, false)

        initializeVariables()

        setupUI()

        subscribeToObservers()

        setupInterstitialAd()

        return binding.root
    }

    private fun initializeVariables() {
        gameDifficultyValue = MainGameScreenArgs.fromBundle(requireArguments()).gameDifficultyValue

        viewModelFactory = MainGameViewModelFactory(gameDifficultyValue)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainGameViewModel::class.java)

        binding.mainGameViewModel = viewModel

        binding.lifecycleOwner = this

        soundEffectsVolume = PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getInt(SOUND_EFFECTS_VOLUME_PREF_KEY, 0).toFloat().div(10f)

        if (soundEffectsVolume == 0f) {
            soundEffectsEnabled = false
        } else {
            soundPool = setupSoundPool(2)
            loadSounds()
        }
    }

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
                if (soundEffectsEnabled)
                    soundPool?.play(soundClick, soundEffectsVolume, soundEffectsVolume, 0, 0, 1F)

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
            if (soundEffectsEnabled)
                soundPool?.play(soundButtonClick, soundEffectsVolume, soundEffectsVolume, 0, 0, 1F)

            currentFragmentState = FragmentState.DIALOG_BEING_SHOWN

            viewModel.countDownTimer.cancel()

            requireContext().buildMaterialDialog(
                getString(R.string.alert_dialog_exit_game_title),
                getString(R.string.alert_dialog_exit_game_message),
                getString(R.string.alert_dialog_exit_game_positive_button_text),
                getString(R.string.alert_dialog_exit_game_negative_button_text),
                cancelable = false,
                { _, _ ->
                    if (soundEffectsEnabled)
                        soundPool?.play(
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
                    if (soundEffectsEnabled)
                        soundPool?.play(
                            soundButtonClick,
                            soundEffectsVolume,
                            soundEffectsVolume,
                            0,
                            0,
                            1F
                        )

                    viewModel.currentGameTime.value?.let { currentGameTime ->
                        viewModel.startGameTimer(currentGameTime.times(1000))
                    }
                    currentFragmentState = FragmentState.RUNNING
                }
            )
        }

        binding.fabPauseTimer.setOnClickListener {
            if (soundEffectsEnabled)
                soundPool?.play(soundButtonClick, soundEffectsVolume, soundEffectsVolume, 0, 0, 1F)

            currentFragmentState = FragmentState.DIALOG_BEING_SHOWN

            viewModel.countDownTimer.cancel()

            requireContext().buildMaterialDialog(
                getString(R.string.alert_dialog_game_paused_title),
                getString(R.string.alert_dialog_game_paused_message),
                getString(R.string.alert_dialog_game_paused_positive_button_text),
                negativeButtonText = null,
                cancelable = false,
                { _, _ ->
                    if (soundEffectsEnabled)
                        soundPool?.play(
                            soundButtonClick,
                            soundEffectsVolume,
                            soundEffectsVolume,
                            0,
                            0,
                            1F
                        )

                    viewModel.currentGameTime.value?.let { currentGameTime ->
                        viewModel.startGameTimer(currentGameTime.times(1000))
                    }
                    currentFragmentState = FragmentState.RUNNING
                },
                negativeButtonListener = null
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
                    cancelable = false,
                    { _, _ ->
                        if (soundEffectsEnabled)
                            soundPool?.play(
                                soundButtonClick,
                                soundEffectsVolume,
                                soundEffectsVolume,
                                0,
                                0,
                                1F
                            )

                        viewModel.currentGameTime.value?.let { currentGameTime ->
                            viewModel.startGameTimer(currentGameTime.times(1000))
                        }
                        currentFragmentState = FragmentState.RUNNING
                    },
                    { _, _ ->
                        if (soundEffectsEnabled)
                            soundPool?.play(
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
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    private fun subscribeToObservers() {
        viewModel.eventCorrectAnswer.observe(viewLifecycleOwner, { answerIsCorrect ->
            if (answerIsCorrect) {
                if (soundEffectsEnabled)
                    soundPool?.play(
                        soundCorrectAnswer,
                        soundEffectsVolume,
                        soundEffectsVolume,
                        1,
                        0,
                        1F
                    )

                requireContext().buildMaterialDialog(
                    getString(R.string.alert_dialog_correct_answer_title),
                    getString(R.string.alert_dialog_correct_answer_message),
                    getString(R.string.alert_dialog_correct_answer_positive_button_text),
                    negativeButtonText = null,
                    cancelable = false,
                    { _, _ -> checkEventGameFinished() },
                    negativeButtonListener = null
                )
            } else {
                if (soundEffectsEnabled)
                    soundPool?.play(
                        soundWrongAnswer,
                        soundEffectsVolume,
                        soundEffectsVolume,
                        1,
                        0,
                        1F
                    )

                requireContext().buildMaterialDialog(
                    getString(R.string.alert_dialog_wrong_answer_title),
                    getString(
                        R.string.alert_dialog_wrong_answer_message,
                        viewModel.currentHiraganaLetterRomanization.value!!
                    ),
                    getString(R.string.alert_dialog_wrong_answer_positive_button_text),
                    negativeButtonText = null,
                    cancelable = false,
                    { _, _ -> checkEventGameFinished() },
                    negativeButtonListener = null
                )
            }
        })

        viewModel.eventTimeOver.observe(viewLifecycleOwner, { timeIsOver ->
            if (timeIsOver) {
                if (soundEffectsEnabled)
                    soundPool?.play(
                        soundWrongAnswer,
                        soundEffectsVolume,
                        soundEffectsVolume,
                        1,
                        0,
                        1F
                    )

                requireContext().buildMaterialDialog(
                    getString(R.string.alert_dialog_time_over_title),
                    getString(
                        R.string.alert_dialog_time_over_message,
                        viewModel.currentHiraganaLetterRomanization.value!!
                    ),
                    getString(R.string.alert_dialog_time_over_positive_button_text),
                    negativeButtonText = null,
                    cancelable = false,
                    { _, _ -> checkEventGameFinished() },
                    negativeButtonListener = null
                )
            }
        })
    }

    private fun setupInterstitialAd() {
        interstitialAd = InterstitialAd(requireContext()).apply {
            adUnitId = getString(R.string.interstitialAdUnitId)
            adListener = (object : AdListener() {
                override fun onAdClosed() {
                    viewModel.gameScore.value?.let { gameScore ->
                        navigateToScoreScreen(gameScore, gameDifficultyValue)
                    } ?: navigateToScoreScreen(0, gameDifficultyValue)
                }
            })
            loadAd(AdRequest.Builder().build())
        }
    }

    private fun showAdAndNavigateToScoreScreen() {
        if (interstitialAd.isLoaded) {
            interstitialAd.show()
        } else {
            viewModel.gameScore.value?.let { gameScore ->
                navigateToScoreScreen(gameScore, gameDifficultyValue)
            } ?: navigateToScoreScreen(0, gameDifficultyValue)
        }
    }

    private fun checkEventGameFinished() {
        if (viewModel.eventGameFinished.value == true) {
            showAdAndNavigateToScoreScreen()
        } else {
            binding.chipGroupRomanizationOptions.clearCheck()
            viewModel.getNextLetter()
        }
    }

    private fun navigateToScoreScreen(gameScore: Int, gameDifficultyValue: Int) {
        if (gameScore == PERFECT_SCORE)
            SharedPreferencesPerfectScores(requireContext()).incrementPerfectScore()

        findNavController().navigate(
            MainGameScreenDirections.actionMainGameScreenToGameScoreScreen(
                gameScore,
                gameDifficultyValue
            )
        )
    }

    private fun loadSounds() {
        soundPool?.let { soundPool ->
            soundButtonClick = soundPool.load(requireContext(), R.raw.jaoreir_button_simple_01, 2)
            soundClick = soundPool.load(requireContext(), R.raw.brandondelehoy_series_of_clicks, 1)
            soundCorrectAnswer =
                soundPool.load(requireContext(), R.raw.mativve_electro_success_sound, 3)
            soundWrongAnswer = soundPool.load(requireContext(), R.raw.autistic_lucario_error, 3)
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
            viewModel.currentGameTime.value?.let { currentGameTime ->
                viewModel.startGameTimer(currentGameTime.times(1000))
            }
        }

        super.onResume()
    }

    override fun onDestroyView() {
        if (soundEffectsEnabled) {
            soundPool?.release()
            soundPool = null
        }
        _binding = null

        super.onDestroyView()
    }
}