package com.mathsemilio.hiraganalearner.activity

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.databinding.ActivityMainBinding
import com.mathsemilio.hiraganalearner.util.SharedPreferencesGameScore
import com.mathsemilio.hiraganalearner.util.TAG_MAIN_ACTIVITY

class MainActivity : AppCompatActivity() {

    //==========================================================================================
    // Companion Object
    //==========================================================================================
    companion object {
        // String keys for the bundle object
        private const val SAVED_SYMBOL_ID = "savedSymbolId"
        private const val SAVED_ROMANIZATION = "savedRomanization"
        private const val SAVED_GAME_SCORE = "savedGameScore"

        // String key for Game Score in the SharedPreferences object
        private const val GAME_SCORE_KEY: String = "gameScore"
    }

    //==========================================================================================
    // Data Class
    //==========================================================================================

    // Data class for representing the hiragana symbols and its corresponding romanizations
    data class HiraganaSymbol(
        val drawableSymbolId: Int,
        val romanization: String
    )

    //==========================================================================================
    // Hiragana Symbol List
    //==========================================================================================

    // List of Hiragana Symbols and its associated Hepburn romanization
    private val hiraganaSymbol: MutableList<HiraganaSymbol> = mutableListOf(
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col1_a, romanization = "A"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col1_i, romanization = "I"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col1_u, romanization = "U"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col1_e, romanization = "E"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col1_o, romanization = "O"),

        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col2_ka, romanization = "KA"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col2_ki, romanization = "KI"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col2_ku, romanization = "KU"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col2_ke, romanization = "KE"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col2_ko, romanization = "KO"),

        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col3_sa, romanization = "SA"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col3_shi, romanization = "SHI"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col3_su, romanization = "SU"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col3_se, romanization = "SE"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col3_so, romanization = "SO"),

        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col4_ta, romanization = "TA"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col4_chi, romanization = "CHI"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col4_tsu, romanization = "TSU"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col4_te, romanization = "TE"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col4_to, romanization = "TO"),

        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col5_na, romanization = "NA"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col5_ni, romanization = "NI"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col5_nu, romanization = "NU"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col5_ne, romanization = "NE"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col5_no, romanization = "NO"),

        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col6_ha, romanization = "HA"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col6_hi, romanization = "HI"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col6_fu, romanization = "FU"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col6_he, romanization = "HE"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col6_ho, romanization = "HO"),

        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col7_ma, romanization = "MA"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col7_mi, romanization = "MI"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col7_mu, romanization = "MU"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col7_me, romanization = "ME"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col7_mo, romanization = "MO"),

        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col8_ya, romanization = "YA"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col8_yu, romanization = "YU"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col8_yo, romanization = "YO"),

        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col9_ra, romanization = "RA"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col9_ri, romanization = "RI"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col9_ru, romanization = "RU"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col9_re, romanization = "RE"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col9_ro, romanization = "RO"),

        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col10_wa, romanization = "WA"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col10_wi, romanization = "WI"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col10_we, romanization = "WE"),
        HiraganaSymbol(drawableSymbolId = R.drawable.ic_col10_wo, romanization = "WO")
    )

    //==========================================================================================
    // Class-wide variables
    //==========================================================================================

    // Variable for the binding class
    private lateinit var binding: ActivityMainBinding

    // Variable for the Game Score
    private var gameScore: Int = 0

    // Variable which implements a lambda function that will call shuffleList().
    private val funShuffleList = { _: DialogInterface, _: Int ->
        shuffleList()
    }

    //==========================================================================================
    // OnCreate
    //==========================================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Log.i(TAG_MAIN_ACTIVITY, "Setting up the Toolbar")
        // Calling the function that sets up the Toolbar
        setUpActivityToolbar()

        // Setting up a OnClickListener for the buttonGerarSimbolo Button
        binding.buttonGerarSimbolo.setOnClickListener {
            shuffleList()
            binding.radioGroupHiraganaOptions.clearCheck()
        }

        /*
        Checking if the savedInstanceState is null (which it is if the activity has not been
        destroyed before), if it is we call the shuffleList function and the game flows from there.
        If it isn't, we call both the handleVectorDisplayAndRadioButtonRandomization and the
        handleUserInteraction function, and pass the parameters recovered from the savedInstanceState.
         */
        if (savedInstanceState == null) {
            Log.i(TAG_MAIN_ACTIVITY, "Calling the shuffleList function")
            shuffleList()
            Log.i(
                TAG_MAIN_ACTIVITY, "Setting the textScoreNumber TextView value as the " +
                        "value of the gameScore variable"
            )
            binding.textScoreNumber.text = gameScore.toString()
        } else {
            Log.i(
                TAG_MAIN_ACTIVITY, "Calling handleVectorDisplayAndRadioButtonRomanization " +
                        "and passing the symbol ID and the romanization saved from the " +
                        "onSaveInstanceState function"
            )
            handleVectorDisplayAndRadioButtonRandomization(
                savedInstanceState.getInt(SAVED_SYMBOL_ID),
                savedInstanceState.getString(SAVED_ROMANIZATION)
            )

            Log.i(
                TAG_MAIN_ACTIVITY, "Calling handleUserInteraction and passing the " +
                        "romanization saved on the onSaveInstanceState function."
            )
            handleUserInteraction(savedInstanceState.getString(SAVED_ROMANIZATION))

            Log.i(TAG_MAIN_ACTIVITY, "Restoring the previous game score")
            gameScore = savedInstanceState.getInt(SAVED_GAME_SCORE)
            binding.textScoreNumber.text = gameScore.toString()
        }
    }

    //==========================================================================================
    // ShuffleList function
    //==========================================================================================

    /**
     * Void function that shuffles the list of Hiragana Symbols, and passes the first
     * drawableSymbolID and romanization to the handleVectorDisplayAndRadioButtonRandomization. It
     * also calls the handleUserInteraction function and passes the same romanization.
     * @author mathsemilio
     */
    private fun shuffleList() {
        // Shuffling the items in the list
        hiraganaSymbol.shuffle()

        /*
         Calling the function that will handle the randomization of the String romanizations for
         the RadioButtons. It also handles the display of the hiragana symbol vector on the screen.
         As a parameter we will pass the first Int containing the drawable symbol ID and the
         string containing the romanization for a Hiragana Symbol.
         */
        handleVectorDisplayAndRadioButtonRandomization(
            hiraganaSymbol.first().drawableSymbolId,
            hiraganaSymbol.first().romanization
        )

        /*
         Calling the function to handle the user interaction and passing the first String
         containing the hiragana symbol romanization.
         */
        handleUserInteraction(hiraganaSymbol.first().romanization)
    }

    //==========================================================================================
    // HandleVectorDisplayAndRadioButtonRandomization function
    //==========================================================================================

    /**
     * Void function that handles the randomization of the text on the RadioButtons, it also
     * handles the display of the hiragana symbol on the screen and which radio button will receive
     * the correct romanization for the user to select.
     * @param drawableSymbolId: Int - Contains the ID of a hiragana symbol vector.
     * @param correctRomanization: String - Contains the string of a romanization corresponding to
     * a particular hiragana symbol.
     * @author mathsemilio
     */
    private fun handleVectorDisplayAndRadioButtonRandomization(
        drawableSymbolId: Int,
        correctRomanization: String?
    ) {
        // Displaying the vector from the resource ID received from the shuffleList function
        binding.imageHiraganaSymbol.setImageResource(drawableSymbolId)

        // A list containing strings to be used as incorrect romanizations for the radio buttons
        val randomSymbolsArray = listOf(
            "E", "O", "KA", "KE", "SA", "SHI", "TSU", "TO",
            "NO", "NI", "HA", "HE", "MU", "MO", "YA", "YU", "RO", "RI", "WA", "WE", "WO"
        )

        // Shuffling the list to change the natural order of its elements
        randomSymbolsArray.shuffled()

        /*
        Setting the text for the Radio Buttons. After shuffling the list, we are giving each of the
        radio buttons the first string romanization sliced from the list. The first{} function was
        used to make sure none of the buttons had the correctRomanization before the we sort which
        of them would receive the string containing the correct romanization. From the 2nd to the
        4th button, it was also added to the boolean expression, the condition that the string
        sliced from the list would not be equal to the text from button above (eg. the text from
        the 2nd radio button will not be equal to text from the 1st one.)
         */
        binding.radioButtonOption1.text = randomSymbolsArray.slice(0..4)
            .first { it != correctRomanization }
        binding.radioButtonOption2.text = randomSymbolsArray.slice(5..9)
            .first { it != correctRomanization && it != binding.radioButtonOption1.text }
        binding.radioButtonOption3.text = randomSymbolsArray.slice(10..14)
            .first { it != correctRomanization && it != binding.radioButtonOption2.text }
        binding.radioButtonOption4.text = randomSymbolsArray.slice(15..20)
            .first { it != correctRomanization && it != binding.radioButtonOption3.text }

        /*
        Simple algorithm to generate a random number between 0 and 4, and based on that number, we
        select which radio button will receive the string containing the correct romanization for
        the symbol on the screen.
         */
        when ((0 until 4).random()) {
            0 -> {
                Log.i(TAG_MAIN_ACTIVITY, "rBtn1 Selected")
                binding.radioButtonOption1.text = correctRomanization
            }
            1 -> {
                Log.i(TAG_MAIN_ACTIVITY, "rBtn2 Selected")
                binding.radioButtonOption2.text = correctRomanization
            }
            2 -> {
                Log.i(TAG_MAIN_ACTIVITY, "rBtn3 Selected")
                binding.radioButtonOption3.text = correctRomanization
            }
            3 -> {
                Log.i(TAG_MAIN_ACTIVITY, "rBtn4 Selected")
                binding.radioButtonOption4.text = correctRomanization
            }
        }
    }

    //==========================================================================================
    // HandleUserInteraction function
    //==========================================================================================

    /**
     * Void function that handles the user interaction with the views on the layout, it also calls
     * the respective functions that will handle the creation of AlertDialogs, that pops up depending
     * on the users answer (if it's  correct or not, based on the @param correctRomanization and
     * the value of the selectedRomanization variable).
     * @param correctRomanization: String? - Contains the string received from the shuffleList
     * function.
     * @author mathsemilio
     */
    private fun handleUserInteraction(correctRomanization: String?) {
        var radioButton: RadioButton
        var selectedRomanization: String

        /*
        Setting a setOnCheckedChangeListener for the radio group to check if any of the buttons is
        selected, if no button is selected, the buttonVerificar Button will be disabled, else the
        button will be enabled, and a onClickListener will be attached to it. Inside the listener
        we will check if the selected romanization (the text from the selected radio button) is
        equal to the correct romanization (passed from the shuffleList function).
        If it is, the dialog alerting the user that his answer is correct will be displayed, and the
        gameScore will be updated. Or else, the dialog alerting the opposite is displayed. In both
        cases the radio button selection will be cleared.
         */
        binding.radioGroupHiraganaOptions.setOnCheckedChangeListener { group, checkedId ->
            if (group.checkedRadioButtonId == -1) {
                binding.buttonVerificar.isEnabled = false
            } else {
                binding.buttonVerificar.isEnabled = true
                radioButton = group.findViewById(checkedId)
                selectedRomanization = radioButton.text.toString()

                binding.buttonVerificar.setOnClickListener {
                    if (selectedRomanization == correctRomanization) {
                        alertDialogCorrectUserInput(correctRomanization)
                        ++gameScore
                        binding.textScoreNumber.text = gameScore.toString()
                        group.clearCheck()
                    } else {
                        alertDialogWrongUserInput(correctRomanization, selectedRomanization)
                        group.clearCheck()
                    }
                }
            }
        }
    }

    //==========================================================================================
    // Toolbar Configuration
    //==========================================================================================

    /**
     * Void function that sets up the toolbar for the activity and also sets the title.
     * @author mathsemilio
     */
    private fun setUpActivityToolbar() {
        setSupportActionBar(findViewById(R.id.main_activity_toolbar))
        supportActionBar?.setTitle(R.string.app_name)
    }

    /**
     * Overridden function for inflating a menu on the activity's toolbar
     * @param menu: Menu?
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Overridden function that will handle the user interaction with the actions on the Toolbar
     * @param item: MenuItem
     */
    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.close_app -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    //==========================================================================================
    // alertDialogCorrectUserInput function
    //==========================================================================================

    /**
     * Void function that builds a AlertDialog, which pops up when the user has selected the
     * correct romanization for the symbol on the screen.
     * @param correctRomanization: String - Contains the correct romanization for the symbol on the
     * screen.
     * @author mathsemilio
     */
    private fun alertDialogCorrectUserInput(correctRomanization: String) {
        Log.i(TAG_MAIN_ACTIVITY, "Building the AlertDialogCorrectUserInput")
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.resposta_correta_title)

        Log.i(TAG_MAIN_ACTIVITY, "Correct Romanization: $correctRomanization")
        builder.setMessage(getString(R.string.resposta_correta_msg, correctRomanization))
        builder.setPositiveButton(
            R.string.proximo_simbolo_btnText,
            DialogInterface.OnClickListener(funShuffleList)
        )
        builder.setCancelable(false)
        builder.show()
    }

    //==========================================================================================
    // alertDialogWrongUserInput function
    //==========================================================================================

    /**
     * Void function that builds a AlertDialog which pops up when the user chooses the wrong
     * romanization for the symbol on the screen.
     * @param correctRomanization: String - Contains the String with the correct romanization for
     * the symbol on the screen.
     * @param selectedRomanization: String - Contains the String with the selected romanization
     * that the user chose from the options on the screen (in this case is the WRONG romanization).
     * @author mathsemilio
     */
    private fun alertDialogWrongUserInput(
        correctRomanization: String?,
        selectedRomanization: String
    ) {
        Log.i(TAG_MAIN_ACTIVITY, "Building the AlertDialogWrongUserInput")
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.resposta_errada_title)

        Log.i(
            TAG_MAIN_ACTIVITY, "Correct Romanization: $correctRomanization"
                    + "Selected Romanization: $selectedRomanization"
        )
        builder.setMessage(
            getString(
                R.string.resposta_errada_msg, correctRomanization,
                selectedRomanization
            )
        )
        builder.setPositiveButton(
            R.string.proximo_simbolo_btnText,
            DialogInterface.OnClickListener(funShuffleList)
        )
        builder.setCancelable(false)
        builder.show()
    }

    //==========================================================================================
    // onBackPressed function
    //==========================================================================================

    /**
     * Overridden void function that will finish the activity once the user presses the back button
     * on his device.
     */
    override fun onBackPressed() {
        Log.i(TAG_MAIN_ACTIVITY, "Calling onBackPressed")
        finish()
        super.onBackPressed()
    }

    //==========================================================================================
    // onSaveInstanceState function
    //==========================================================================================

    /**
     * Overridden void function that saves the current activity state (the current symbol, it's
     * romanization and the game score) on a Bundle object.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        // Saving the current hiragana symbol, it's associated hepburn romanization and the game score
        outState.putInt(SAVED_SYMBOL_ID, hiraganaSymbol.first().drawableSymbolId)
        outState.putString(SAVED_ROMANIZATION, hiraganaSymbol.first().romanization)
        outState.putInt(SAVED_GAME_SCORE, gameScore)

        super.onSaveInstanceState(outState)
    }

    //==========================================================================================
    // onPause
    //==========================================================================================

    /**
     * Overridden function that is called when the activity is paused. In this case, we are checking
     * if the current game score is higher than the value stored on the SharedPreferences object.
     * If it is, the saveGameScore function is called to update the highest score.
     */
    override fun onPause() {
        Log.i(TAG_MAIN_ACTIVITY, "OnPause called")

        val valueStoredSharedPreferences = SharedPreferencesGameScore(this)
            .retrieveGameScore(GAME_SCORE_KEY)

        if (gameScore > valueStoredSharedPreferences!!) {
            SharedPreferencesGameScore(this).saveGameScore(GAME_SCORE_KEY, gameScore)
        }

        super.onPause()
    }

    //==========================================================================================
    // onDestroy
    //==========================================================================================

    /**
     * Overridden function that is called when the activity is about to be destroyed. In this case
     * we are clearing the hiragana symbols list.
     */
    override fun onDestroy() {
        Log.i(TAG_MAIN_ACTIVITY, "OnDestroyed called")
        hiraganaSymbol.clear()
        super.onDestroy()
    }
}