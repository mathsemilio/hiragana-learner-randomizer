package com.mathsemilio.hiraganalearner.ui.screens.main

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
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.chip.Chip
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.commom.PERFECT_SCORE
import com.mathsemilio.hiraganalearner.commom.PRIORITY_HIGH
import com.mathsemilio.hiraganalearner.commom.PRIORITY_LOW
import com.mathsemilio.hiraganalearner.commom.PRIORITY_MEDIUM
import com.mathsemilio.hiraganalearner.data.preferences.repository.PreferencesRepository
import com.mathsemilio.hiraganalearner.databinding.GameMainScreenBinding
import com.mathsemilio.hiraganalearner.logic.eventWrapper.GameEvent
import com.mathsemilio.hiraganalearner.ui.commom.AlertUserHelper.onCorrectAnswer
import com.mathsemilio.hiraganalearner.ui.commom.AlertUserHelper.onExitGame
import com.mathsemilio.hiraganalearner.ui.commom.AlertUserHelper.onGameIsPaused
import com.mathsemilio.hiraganalearner.ui.commom.AlertUserHelper.onTimeOver
import com.mathsemilio.hiraganalearner.ui.commom.AlertUserHelper.onWrongAnswer
import com.mathsemilio.hiraganalearner.ui.commom.util.playSFX
import com.mathsemilio.hiraganalearner.ui.commom.util.setupAndLoadInterstitialAd
import com.mathsemilio.hiraganalearner.ui.commom.util.setupSoundPool

class GameMainScreen : Fragment() {

    private enum class FragmentState { RUNNING, PAUSED, DIALOG_BEING_SHOWN }

    private var _binding: GameMainScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainGameViewModel by viewModels()

    private var interstitialAd: InterstitialAd? = null

    private lateinit var preferencesRepository: PreferencesRepository
    private lateinit var onBackPressedCallback: OnBackPressedCallback
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.game_main_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            mainGameScreen = this@GameMainScreen
            mainGameViewModel = viewModel
            lifecycleOwner = this@GameMainScreen
        }

        interstitialAd = setupAndLoadInterstitialAd(getString(R.string.interstitialAdUnitId)) {
            navigateToScoreScreen()
        }

        preferencesRepository = PreferencesRepository(requireContext())

        soundPool = setupSoundPool(maxAudioStreams = 2)

        soundEffectsVolume = preferencesRepository.getSoundEffectsVolume()

        difficultyValue = GameMainScreenArgs.fromBundle(requireArguments()).difficultyValue

        loadSoundEffects()

        attachListeners()

        startGame()

        observeGameEvents()
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
            override fun handleOnBackPressed() = alertUserOnExitGame()
        }

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private fun loadSoundEffects() {
        soundPool?.apply {
            soundEffectButtonClick =
                load(requireContext(), R.raw.jaoreir_button_simple_01, PRIORITY_MEDIUM)
            soundEffectClick =
                load(requireContext(), R.raw.brandondelehoy_series_of_clicks, PRIORITY_LOW)
            soundEffectCorrectAnswer =
                load(requireContext(), R.raw.mativve_electro_success_sound, PRIORITY_HIGH)
            soundEffectWrongAnswer =
                load(requireContext(), R.raw.autistic_lucario_error, PRIORITY_HIGH)
        }
    }

    private fun startGame() {
        viewModel.startGame(difficultyValue)
    }

    private fun observeGameEvents() {
        viewModel.gameEvent.observe(viewLifecycleOwner, {
            it?.let { gameEvent ->
                when (gameEvent) {
                    GameEvent.AnswerIsCorrect -> alertUserOnCorrectAnswer()
                    GameEvent.AnswerIsWrong -> alertUserOnWrongAnswer()
                    GameEvent.TimeIsOver -> alertUserOnTimeOver()
                    GameEvent.Paused -> alertUserOnGamePaused()
                    GameEvent.Exit -> alertUserOnExitGame()
                }
            }
        })
    }

    private fun alertUserOnCorrectAnswer() {
        soundPool?.playSFX(soundEffectCorrectAnswer, soundEffectsVolume, PRIORITY_HIGH)
        currentFragmentState = FragmentState.DIALOG_BEING_SHOWN
        onCorrectAnswer { checkIfGameIsFinished() }
    }

    private fun alertUserOnWrongAnswer() {
        soundPool?.playSFX(soundEffectWrongAnswer, soundEffectsVolume, PRIORITY_HIGH)
        currentFragmentState = FragmentState.DIALOG_BEING_SHOWN
        onWrongAnswer(viewModel.currentHiraganaRomanization.value.toString()) {
            checkIfGameIsFinished()
        }
    }

    private fun alertUserOnTimeOver() {
        soundPool?.playSFX(soundEffectWrongAnswer, soundEffectsVolume, PRIORITY_HIGH)
        currentFragmentState = FragmentState.DIALOG_BEING_SHOWN
        onTimeOver(viewModel.currentHiraganaRomanization.value.toString()) {
            checkIfGameIsFinished()
        }
    }

    private fun alertUserOnGamePaused() {
        soundPool?.playSFX(soundEffectButtonClick, soundEffectsVolume, PRIORITY_MEDIUM)
        currentFragmentState = FragmentState.DIALOG_BEING_SHOWN
        onGameIsPaused { viewModel.resumeTimer() }
    }

    private fun alertUserOnExitGame() {
        soundPool?.playSFX(soundEffectButtonClick, soundEffectsVolume, PRIORITY_MEDIUM)
        currentFragmentState = FragmentState.DIALOG_BEING_SHOWN
        onExitGame({ navigateToWelcomeScreen() }, { viewModel.resumeTimer() })
    }

    private fun checkIfGameIsFinished() {
        if (viewModel.gameIsFinished) {
            showAd()
        } else {
            viewModel.onGameEventHandled()
            viewModel.getNextSymbol()
            binding.chipGroupRomanizationOptions.clearCheck()
            currentFragmentState = FragmentState.RUNNING
        }
    }

    private fun showAd() {
        interstitialAd?.run { if (isLoaded) show() else navigateToScoreScreen() }
    }

    private fun navigateToWelcomeScreen() {
        findNavController().navigate(R.id.action_gameMainScreen_to_gameWelcomeScreen)
    }

    private fun navigateToScoreScreen() {
        viewModel.score.value?.let { score ->
            if (score == PERFECT_SCORE)
                preferencesRepository.incrementPerfectScoresValue()

            findNavController().navigate(
                GameMainScreenDirections.actionGameMainScreenToGameScoreScreen(
                    score, difficultyValue
                )
            )
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseTimer()

        if (currentFragmentState == FragmentState.RUNNING)
            currentFragmentState = FragmentState.PAUSED
    }

    override fun onResume() {
        super.onResume()
        if (currentFragmentState == FragmentState.PAUSED)
            viewModel.resumeTimer()
    }

    override fun onDestroyView() {
        soundPool?.release()
        soundPool = null
        _binding = null
        interstitialAd = null
        super.onDestroyView()
    }
}