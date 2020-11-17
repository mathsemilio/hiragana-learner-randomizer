package com.mathsemilio.hiraganalearner.ui.fragment

import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.chip.Chip
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.data.repository.PreferencesRepository
import com.mathsemilio.hiraganalearner.databinding.GameMainScreenBinding
import com.mathsemilio.hiraganalearner.others.*
import com.mathsemilio.hiraganalearner.others.AlertUser.onCorrectAnswer
import com.mathsemilio.hiraganalearner.others.AlertUser.onExitGame
import com.mathsemilio.hiraganalearner.others.AlertUser.onGameIsPaused
import com.mathsemilio.hiraganalearner.others.AlertUser.onTimeOver
import com.mathsemilio.hiraganalearner.others.AlertUser.onWrongAnswer
import com.mathsemilio.hiraganalearner.ui.viewModel.MainGameViewModel

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
    private val viewModel: MainGameViewModel by viewModels()
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private var interstitialAd: InterstitialAd? = null
    private var currentFragmentState = FragmentState.RUNNING
    private var soundPool: SoundPool? = null
    private var soundEffectsVolume = 0F
    private var soundEffectClick = 0
    private var soundEffectButtonClick = 0
    private var soundEffectCorrectAnswer = 0
    private var soundEffectWrongAnswer = 0
    var difficultyValue = 0
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

        subscribeToObservers()

        viewModel.onStartGame(difficultyValue)
    }

    private fun initializeVariables() {
        difficultyValue = GameMainScreenArgs.fromBundle(requireArguments()).difficultyValue

        binding.mainGameScreen = this
        binding.mainGameViewModel = viewModel
        binding.lifecycleOwner = this

        soundEffectsVolume = PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getInt(SOUND_EFFECTS_VOLUME_PREFERENCE_KEY, 0).toFloat().div(10F)

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

        interstitialAd =
            requireContext().setupAndLoadInterstitialAd("ca-app-pub-3940256099942544/1033173712") {
                navigateToScoreScreen()
            }
    }

    private fun attachListeners() {
        binding.chipGroupRomanizationOptions.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == -1) {
                binding.fabCheckAnswer.isEnabled = false
            } else {
                soundPool?.playSFX(soundEffectClick, soundEffectsVolume, PRIORITY_LOW)

                binding.fabCheckAnswer.isEnabled = true

                val checkedButton = group.findViewById<Chip>(checkedId)
                selectedRomanization = checkedButton.text.toString()
            }
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onButtonExitGameClicked()
            }
        }
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private fun subscribeToObservers() {
        viewModel.eventIsAnswerCorrect.observe(viewLifecycleOwner, {
            it?.let { answerIsCorrect ->
                if (answerIsCorrect) {
                    soundPool?.playSFX(soundEffectCorrectAnswer, soundEffectsVolume, PRIORITY_HIGH)

                    currentFragmentState = FragmentState.DIALOG_BEING_SHOWN

                    onCorrectAnswer {
                        if (viewModel.gameFinished) {
                            showAd()
                        } else {
                            viewModel.onCheckAnswerCompleted()
                            currentFragmentState = FragmentState.RUNNING
                            binding.chipGroupRomanizationOptions.clearCheck()
                        }
                    }
                } else {
                    onWrongAnswer {
                        if (viewModel.gameFinished) {
                            showAd()
                        } else {
                            viewModel.onCheckAnswerCompleted()
                            currentFragmentState = FragmentState.RUNNING
                            binding.chipGroupRomanizationOptions.clearCheck()
                        }
                    }
                }
            }
        })

        viewModel.eventTimeOver.observe(viewLifecycleOwner, {
            it?.let {
                soundPool?.playSFX(soundEffectWrongAnswer, soundEffectsVolume, PRIORITY_HIGH)

                currentFragmentState = FragmentState.DIALOG_BEING_SHOWN

                onTimeOver {
                    if (viewModel.gameFinished) {
                        showAd()
                    } else {
                        viewModel.onCheckAnswerCompleted()
                        currentFragmentState = FragmentState.RUNNING
                        binding.chipGroupRomanizationOptions.clearCheck()
                    }
                }
            }
        })

        viewModel.eventButtonPauseClicked.observe(viewLifecycleOwner, {
            it?.let { gameIsPaused ->
                if (gameIsPaused) {
                    soundPool?.playSFX(soundEffectButtonClick, soundEffectsVolume, PRIORITY_MEDIUM)

                    currentFragmentState = FragmentState.DIALOG_BEING_SHOWN

                    onGameIsPaused {
                        soundPool?.playSFX(
                            soundEffectButtonClick,
                            soundEffectsVolume,
                            PRIORITY_MEDIUM
                        )
                        viewModel.onPauseGameCompleted()
                        currentFragmentState = FragmentState.RUNNING
                    }
                }
            }
        })

        viewModel.eventButtonExitGameClicked.observe(viewLifecycleOwner, {
            it?.let { exitGameRequested ->
                if (exitGameRequested) {
                    soundPool?.playSFX(soundEffectButtonClick, soundEffectsVolume, PRIORITY_MEDIUM)

                    currentFragmentState = FragmentState.DIALOG_BEING_SHOWN

                    onExitGame(
                        {
                            soundPool?.playSFX(
                                soundEffectButtonClick,
                                soundEffectsVolume,
                                PRIORITY_MEDIUM
                            )
                            findNavController().navigate(R.id.action_gameMainScreen_to_gameWelcomeScreen)
                        }, {
                            viewModel.onExitGameCancelled()
                            currentFragmentState = FragmentState.RUNNING
                        }
                    )
                }
            }
        })
    }

    private fun showAd() {
        interstitialAd?.let { interstitialAd ->
            if (interstitialAd.isLoaded) interstitialAd.show() else navigateToScoreScreen()
        }
    }

    private fun navigateToScoreScreen() {
        viewModel.gameScore.value?.let { score ->
            if (score == PERFECT_SCORE)
                PreferencesRepository(requireContext()).incrementPerfectScoreValue()

            findNavController().navigate(
                GameMainScreenDirections
                    .actionGameMainScreenToGameScoreScreen(
                        score,
                        difficultyValue
                    )
            )
        }
    }

    override fun onPause() {
        viewModel.onFragmentPaused()

        if (currentFragmentState == FragmentState.RUNNING)
            currentFragmentState = FragmentState.PAUSED

        super.onPause()
    }

    override fun onResume() {
        if (currentFragmentState == FragmentState.PAUSED)
            viewModel.onFragmentRestarted()

        super.onResume()
    }

    override fun onDestroyView() {
        soundPool?.release()
        soundPool = null
        _binding = null
        interstitialAd = null

        super.onDestroyView()
    }
}