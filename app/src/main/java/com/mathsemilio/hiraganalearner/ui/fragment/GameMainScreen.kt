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
class GameMainScreen : Fragment() {

    /**
     * Enum for representing the Fragment States, used to manage how the timer will behave
     * during its lifecycle states.
     */
    private enum class FragmentState { RUNNING, PAUSED, DIALOG_BEING_SHOWN }

    private var _binding: GameMainScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var gameViewModel: MainGameViewModel
    private lateinit var viewModelFactory: MainGameViewModelFactory
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private lateinit var interstitialAd: InterstitialAd
    private var currentFragmentState = FragmentState.RUNNING
    private var soundPool: SoundPool? = null
    private var isSoundEffectsEnabled = true
    private var soundEffectsVolume = 0F
    private var soundEffectClick = 0
    private var soundEffectButtonClick = 0
    private var soundEffectCorrectAnswer = 0
    private var soundEffectWrongAnswer = 0
    private var difficultyValue = 0
    var selectedRomanization = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.game_main_screen, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeVariables()

        attachListeners()

        setupInterstitialAd()

        subscribeToObservers()
    }

    private fun initializeVariables() {
        difficultyValue = GameMainScreenArgs.fromBundle(requireArguments()).difficultyValue

        viewModelFactory = MainGameViewModelFactory(difficultyValue)
        gameViewModel = ViewModelProvider(this, viewModelFactory).get(MainGameViewModel::class.java)

        binding.mainGameScreen = this
        binding.mainGameViewModel = gameViewModel
        binding.lifecycleOwner = this

        soundEffectsVolume = PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getInt(SOUND_EFFECTS_VOLUME_PREFERENCE_KEY, 0).toFloat().div(10F)

        if (soundEffectsVolume == 0F) {
            isSoundEffectsEnabled = false
        } else {
            soundPool = setupSoundPool(2).also {
                soundEffectButtonClick =
                    it.load(requireContext(), R.raw.jaoreir_button_simple_01, PRIORITY_MEDIUM)
                soundEffectClick =
                    it.load(requireContext(), R.raw.brandondelehoy_series_of_clicks, PRIORITY_LOW)
                soundEffectCorrectAnswer =
                    it.load(requireContext(), R.raw.mativve_electro_success_sound, PRIORITY_HIGH)
                soundEffectWrongAnswer =
                    it.load(requireContext(), R.raw.autistic_lucario_error, PRIORITY_HIGH)
            }
        }
    }

    private fun attachListeners() {
        binding.chipGroupRomanizationOptions.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == -1) {
                binding.fabCheckAnswer.isEnabled = false
            } else {
                soundPool?.playSFX(
                    isSoundEffectsEnabled,
                    soundEffectClick,
                    soundEffectsVolume,
                    PRIORITY_LOW
                )

                binding.fabCheckAnswer.isEnabled = true

                val checkedButton = group.findViewById<Chip>(checkedId)
                selectedRomanization = checkedButton.text.toString()
            }
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                gameViewModel.exitGame()
            }
        }
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private fun subscribeToObservers() {
        gameViewModel.eventCorrectAnswer.observe(viewLifecycleOwner, {
            it?.let { answerIsCorrect ->
                if (answerIsCorrect) {
                    soundPool?.playSFX(
                        isSoundEffectsEnabled,
                        soundEffectCorrectAnswer,
                        soundEffectsVolume,
                        PRIORITY_HIGH
                    )

                    currentFragmentState = FragmentState.DIALOG_BEING_SHOWN

                    requireContext().buildMaterialDialog(
                        getString(R.string.alert_dialog_correct_answer_title),
                        getString(R.string.alert_dialog_correct_answer_message),
                        getString(R.string.alert_dialog_correct_answer_positive_button_text),
                        negativeButtonText = null,
                        isCancelable = false,
                        positiveButtonListener = { _, _ ->
                            if (gameViewModel.flagGameFinished) {
                                showAdAndNavigateToScoreScreen()
                            } else {
                                gameViewModel.checkUserInputCompleted()
                                currentFragmentState = FragmentState.RUNNING
                                binding.chipGroupRomanizationOptions.clearCheck()
                            }
                        },
                        negativeButtonListener = null
                    )
                }
            }
        })

        gameViewModel.eventWrongAnswer.observe(viewLifecycleOwner, {
            it?.let { correctRomanization ->
                soundPool?.playSFX(
                    isSoundEffectsEnabled,
                    soundEffectWrongAnswer,
                    soundEffectsVolume,
                    PRIORITY_HIGH
                )

                currentFragmentState = FragmentState.DIALOG_BEING_SHOWN

                requireContext().buildMaterialDialog(
                    getString(R.string.alert_dialog_wrong_answer_title),
                    getString(R.string.alert_dialog_wrong_answer_message, correctRomanization),
                    getString(R.string.alert_dialog_wrong_answer_positive_button_text),
                    negativeButtonText = null,
                    isCancelable = false,
                    positiveButtonListener = { _, _ ->
                        if (gameViewModel.flagGameFinished) {
                            showAdAndNavigateToScoreScreen()
                        } else {
                            gameViewModel.handleWrongAnswerEvent()
                            currentFragmentState = FragmentState.RUNNING
                            binding.chipGroupRomanizationOptions.clearCheck()
                        }
                    },
                    negativeButtonListener = null
                )
            }
        })

        gameViewModel.eventTimeOver.observe(viewLifecycleOwner, {
            it?.let { romanizationAnswer ->
                soundPool?.playSFX(
                    isSoundEffectsEnabled,
                    soundEffectWrongAnswer,
                    soundEffectsVolume,
                    PRIORITY_HIGH
                )

                currentFragmentState = FragmentState.DIALOG_BEING_SHOWN

                requireContext().buildMaterialDialog(
                    getString(R.string.alert_dialog_time_over_title),
                    getString(R.string.alert_dialog_time_over_message, romanizationAnswer),
                    getString(R.string.alert_dialog_time_over_positive_button_text),
                    negativeButtonText = null,
                    isCancelable = false,
                    positiveButtonListener = { _, _ ->
                        if (gameViewModel.flagGameFinished) {
                            showAdAndNavigateToScoreScreen()
                        } else {
                            gameViewModel.handleTimeOverEvent()
                            currentFragmentState = FragmentState.RUNNING
                            binding.chipGroupRomanizationOptions.clearCheck()
                        }
                    },
                    negativeButtonListener = null
                )
            }
        })

        gameViewModel.eventButtonPauseClicked.observe(viewLifecycleOwner, {
            it?.let { gameIsPaused ->
                if (gameIsPaused) {
                    soundPool?.playSFX(
                        isSoundEffectsEnabled,
                        soundEffectButtonClick,
                        soundEffectsVolume,
                        PRIORITY_MEDIUM
                    )

                    currentFragmentState = FragmentState.DIALOG_BEING_SHOWN

                    requireContext().buildMaterialDialog(
                        getString(R.string.alert_dialog_game_paused_title),
                        getString(R.string.alert_dialog_game_paused_message),
                        getString(R.string.alert_dialog_game_paused_positive_button_text),
                        negativeButtonText = null,
                        isCancelable = false,
                        positiveButtonListener = { _, _ ->
                            soundPool?.playSFX(
                                isSoundEffectsEnabled,
                                soundEffectButtonClick,
                                soundEffectsVolume,
                                PRIORITY_MEDIUM
                            )

                            gameViewModel.pauseGameCompleted()
                            currentFragmentState = FragmentState.RUNNING
                        },
                        negativeButtonListener = null
                    )
                }
            }
        })

        gameViewModel.eventButtonExitGameClicked.observe(viewLifecycleOwner, {
            it?.let { exitGame ->
                soundPool?.playSFX(
                    isSoundEffectsEnabled,
                    soundEffectButtonClick,
                    soundEffectsVolume,
                    PRIORITY_MEDIUM
                )

                currentFragmentState = FragmentState.DIALOG_BEING_SHOWN

                if (exitGame) {
                    requireContext().buildMaterialDialog(
                        getString(R.string.alert_dialog_exit_game_title),
                        getString(R.string.alert_dialog_exit_game_message),
                        getString(R.string.alert_dialog_exit_game_positive_button_text),
                        getString(R.string.alert_dialog_exit_game_negative_button_text),
                        isCancelable = false,
                        positiveButtonListener = { _, _ ->
                            soundPool?.playSFX(
                                isSoundEffectsEnabled,
                                soundEffectButtonClick,
                                soundEffectsVolume,
                                PRIORITY_MEDIUM
                            )

                            findNavController().navigate(R.id.action_gameMainScreen_to_gameWelcomeScreen)
                        },
                        { _, _ ->
                            gameViewModel.exitGameCancelled()
                            currentFragmentState = FragmentState.RUNNING
                        }
                    )
                }
            }
        })
    }

    private fun showAdAndNavigateToScoreScreen() {
        if (interstitialAd.isLoaded) interstitialAd.show() else navigateToScoreScreen()
    }

    private fun navigateToScoreScreen() {
        gameViewModel.gameBackend?.gameScore?.value?.let { score ->
            if (score == PERFECT_SCORE)
                SharedPreferencesPerfectScores(requireContext()).incrementPerfectScore()

            findNavController().navigate(
                GameMainScreenDirections.actionGameMainScreenToGameScoreScreen(
                    score,
                    difficultyValue
                )
            )
        }
    }

    private fun setupInterstitialAd() {
        interstitialAd = InterstitialAd(requireContext()).apply {
            adUnitId = getString(R.string.interstitialAdUnitId)
            adListener = (object : AdListener() {
                override fun onAdClosed() {
                    navigateToScoreScreen()
                }
            })
            loadAd(AdRequest.Builder().build())
        }
    }

    override fun onPause() {
        gameViewModel.pauseGameTimer()

        if (currentFragmentState == FragmentState.RUNNING)
            currentFragmentState = FragmentState.PAUSED

        super.onPause()
    }

    override fun onResume() {
        if (currentFragmentState == FragmentState.PAUSED)
            gameViewModel.restartGameTimer()

        super.onResume()
    }

    override fun onDestroyView() {
        if (isSoundEffectsEnabled) {
            soundPool?.release()
            soundPool = null
        }
        _binding = null

        super.onDestroyView()
    }
}