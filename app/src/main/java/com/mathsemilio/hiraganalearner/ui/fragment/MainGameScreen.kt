package com.mathsemilio.hiraganalearner.ui.fragment

import android.content.DialogInterface
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.databinding.MainGameScreenBinding
import com.mathsemilio.hiraganalearner.ui.viewModel.MainGameViewModel
import com.mathsemilio.hiraganalearner.ui.viewModel.MainGameViewModelFactory
import com.mathsemilio.hiraganalearner.util.*

/**
 * Fragment class for the main game screen
 */
class MainGameScreen : Fragment() {

    private lateinit var binding: MainGameScreenBinding
    private lateinit var viewModelFactory: MainGameViewModelFactory
    private lateinit var viewModel: MainGameViewModel
    private lateinit var soundPool: SoundPool
    private var soundClick: Int? = null
    private var soundCorrectAnswer: Int? = null
    private var soundWrongAnswer: Int? = null
    private var gameDifficultyValue: Int? = null
    private var isRestored = false
    private var isDialogBeingShown = false

    //==========================================================================================
    // onCreateView
    //==========================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Using the inflate function from the DataBindingUtil class to inflate the layout for
        // this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.main_game_screen, container, false)

        gameDifficultyValue =
            MainGameScreenArgs.fromBundle(requireArguments()).gameDifficultyValue

        viewModelFactory = MainGameViewModelFactory(gameDifficultyValue!!)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainGameViewModel::class.java)

        // Setting the ViewModel for Data Binding - this allows the UI elements in the layout to
        // access the data in the ViewModel directly
        binding.mainGameViewModel = viewModel

        // Setting the current activity as the lifecycle owner of the binding, so that the binding
        // can observe LiveData updates
        binding.lifecycleOwner = this

        subscribeToObservers()

        setupSoundPoolAndLoadSounds()

        binding.textBodyGameDifficulty.text =
            getGameDifficultyStringBasedOnTheDifficultyValue(gameDifficultyValue!!)

        /*
        Listener for the chipGroupRomanizationOptions chip group to enable or disable the
        buttonVerifyAnswer button.
        */
        binding.chipGroupRomanizationOptions.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == -1) {
                binding.buttonVerifyAnswer.isEnabled = false
            } else {
                soundPool.play(soundClick!!, 1F, 1F, 0, 0, 1F)

                binding.buttonVerifyAnswer.isEnabled = true

                // Getting the text from the selected chip
                val selectedChip = group.findViewById<Chip>(checkedId)
                val selectedRomanization: String = selectedChip.text.toString()

                // Listener for the buttonVerifyAnswer button
                binding.buttonVerifyAnswer.setOnClickListener {
                    viewModel.countDownTimer?.cancel()

                    isDialogBeingShown = true

                    /*
                    Checking the hiraganaLettersList size, if equals 1, the getLastLetter
                    function is called, else checkUserInput is called.
                    */
                    if (viewModel.hiraganaLettersList.size == 1) {
                        viewModel.getLastLetter(selectedRomanization)
                    } else {
                        viewModel.checkUserInput(selectedRomanization)
                    }
                }
            }
        }

        binding.buttonExit.setOnClickListener {
            findNavController().navigate(R.id.action_mainGameScreen_to_gameWelcomeScreen)
        }

        // Returning the root of the inflated layout
        return binding.root
    }

    //==========================================================================================
    // subscribeToObservers function
    //==========================================================================================
    /**
     * Subscribes (attaches) the observers to each LiveData variables to be observed.
     */
    private fun subscribeToObservers() {
        /*
        Observing the eventCorrectAnswer to show an alert dialog based on if the user's answer
        is correct or not.
        */
        viewModel.eventCorrectAnswer.observe(viewLifecycleOwner, Observer { answerIsCorrect ->
            if (answerIsCorrect) {
                soundPool.play(soundCorrectAnswer!!, 1F, 1F, 1, 0, 1F)

                buildAlertDialog(
                    R.string.alertDialogCorrectAnswer_title,
                    getString(R.string.alertDialogCorrectAnswer_msg),
                    R.string.alertDialogCorrectAnswer_positive_button_text,
                    DialogInterface.OnClickListener { _, _ ->
                        /*
                        Checking the eventGameFinished value, if it's true, the
                        navigateToScoreScreen is called, else, the getNextLetter function is
                        called and the radioGroupHiraganaLetters radio group is cleared.
                        */
                        if (viewModel.eventGameFinished.value == true) {
                            navigateToScoreScreen(
                                viewModel.gameScore.value!!,
                                gameDifficultyValue!!
                            )
                        } else {
                            binding.chipGroupRomanizationOptions.clearCheck()
                            viewModel.getNextLetter()
                        }
                    })
            } else {
                // Building an AlertDialog to alert the user that his answer is incorrect
                soundPool.play(soundWrongAnswer!!, 1F, 1F, 1, 0, 1F)

                buildAlertDialog(
                    R.string.alertDialogWrongAnswer_title,
                    getString(
                        R.string.alertDialogWrongAnswer_msg,
                        viewModel.currentHiraganaLetterRomanization.value
                    ),
                    R.string.alertDialogWrongAnswer_positive_button_text,
                    DialogInterface.OnClickListener { _, _ ->
                        if (viewModel.eventGameFinished.value == true) {
                            navigateToScoreScreen(
                                viewModel.gameScore.value!!,
                                gameDifficultyValue!!
                            )
                        } else {
                            binding.chipGroupRomanizationOptions.clearCheck()
                            viewModel.getNextLetter()
                        }
                    })
            }
        })

        viewModel.eventTimeOver.observe(viewLifecycleOwner, Observer { timeIsOver ->
            if (timeIsOver) {
                soundPool.play(soundWrongAnswer!!, 1F, 1F, 1, 0, 1F)

                buildAlertDialog(
                    R.string.alertDialogTimeOver_title,
                    getString(
                        R.string.alertDialogTimeOver_msg,
                        viewModel.currentHiraganaLetterRomanization.value
                    ),
                    R.string.alertDialogTimeOver_positive_button_text,
                    DialogInterface.OnClickListener { _, _ ->
                        if (viewModel.eventGameFinished.value == true) {
                            navigateToScoreScreen(
                                viewModel.gameScore.value!!,
                                gameDifficultyValue!!
                            )
                        } else {
                            binding.chipGroupRomanizationOptions.clearCheck()
                            viewModel.getNextLetter()
                        }
                    })
            }
        })
    }

    //==========================================================================================
    // setupSoundPoolAndLoadSounds function
    //==========================================================================================
    /**
     * Builds a SoundPool object and loads the sound effect to be played in this fragment.
     */
    private fun setupSoundPoolAndLoadSounds() {
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_GAME)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(audioAttributes)
            .build()

        soundClick = soundPool.load(requireContext(), R.raw.brevicep_normal_click, 1)
        soundCorrectAnswer =
            soundPool.load(requireContext(), R.raw.mativve_electro_success_sound, 1)
        soundWrongAnswer = soundPool.load(requireContext(), R.raw.autistic_lucario_error, 1)
    }

    //==========================================================================================
    // buildAlertDialog function
    //==========================================================================================
    /**
     * Build an alert dialog to alert the user about certain game events.
     *
     * @param title Integer for title's resource id
     * @param message String for the dialog's message
     * @param positiveButtonText Integer for the dialog's positive button resource id
     * @param listener DialogInterface.OnClickListener for listening when the positive button
     * is clicked
     */
    private fun buildAlertDialog(
        title: Int, message: String, positiveButtonText: Int,
        listener: DialogInterface.OnClickListener
    ) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(positiveButtonText, listener)
            setCancelable(false)
            show()
        }
    }

    //==========================================================================================
    // setGameDifficultyStringBasedOnTheDifficultyValue function
    //==========================================================================================
    /**
     * Based on the game difficulty value, returns a String that corresponds the
     * game difficulty value.
     */
    private fun getGameDifficultyStringBasedOnTheDifficultyValue(gameDifficultyValue: Int): String {
        return when (gameDifficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> getString(R.string.game_difficulty_beginner)
            GAME_DIFFICULTY_VALUE_MEDIUM -> getString(R.string.game_difficulty_medium)
            else -> getString(R.string.game_difficulty_hard)
        }
    }

    //==========================================================================================
    // getGameTimeRemainingDefaultValue function
    //==========================================================================================
    /**
     * Returns the default remaining game time based on the game's difficulty.
     *
     * @param gameDifficultyValue Game difficulty value to be evaluated.
     * @return The countdown time in Long.
     */
    private fun getGameTimeRemainingDefaultValue(gameDifficultyValue: Int): Long {
        return when (gameDifficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> COUNTDOWN_TIME_BEGINNER
            GAME_DIFFICULTY_VALUE_MEDIUM -> COUNTDOWN_TIME_MEDIUM
            else -> COUNTDOWN_TIME_HARD
        }
    }

    //==========================================================================================
    // navigateToScoreScreen function
    //==========================================================================================
    /**
     * Navigates from the current screen to the score screen, while passing the game's score and
     * difficulty value to the destination.
     *
     * @param gameScore Integer for the game score to be passed to the score fragment
     * @param gameDifficultyValue Integer that corresponds the game difficulty
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

    //==========================================================================================
    // Fragment lifecycle callbacks
    //==========================================================================================
    override fun onPause() {
        super.onPause()
        isRestored = true

        viewModel.countDownTimer?.cancel()

        SharedPreferencesRemainingGameTime(requireContext()).saveRemainingGameTime(
            viewModel.currentGameTime.value!!
        )
    }

    override fun onResume() {
        super.onResume()
        if (isDialogBeingShown) {
            isRestored = false
        } else if (isRestored) {
            val gameTimeRemaining =
                SharedPreferencesRemainingGameTime(requireContext()).retrieveGameTimeRemaining(
                    getGameTimeRemainingDefaultValue(gameDifficultyValue!!)
                )

            viewModel.startGameTimer(gameTimeRemaining.times(ONE_SECOND))
        }
    }
}