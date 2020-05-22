package com.mathsemilio.hiraganalearner.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.util.DELAY_MILLIS
import com.mathsemilio.hiraganalearner.util.TAG_SPLASH_SCREEN

class SplashScreen : AppCompatActivity() {

    // Handler
    private var delayHandler: Handler? = null

    /*
    Runnable that will check if the isFinishing property is false, if it is the openMainActivity
    function will be called.
     */
    private val runnable: Runnable = Runnable {
        if (!isFinishing) {
            openMainActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Initializing the handler object
        delayHandler = Handler()

        Log.i(TAG_SPLASH_SCREEN, "Calling the postDelayed function from the delayHandler")
        // Calling the postDelayed function, responsible for delaying the start of the MainActivity
        delayHandler!!.postDelayed(runnable, DELAY_MILLIS)
    }

    /**
     *  Void function that declares a explicit intent for starting the MainActivity. It also calls
     *  the finish() function for destroying the SplashScreen activity
     *  @author mathsemilio
     */
    private fun openMainActivity() {
        val intentOpenMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentOpenMainActivity)
        finish()
    }

    /**
     * Overridden function for removing the runnable callback once the activity is destroyed. It
     * only removes the callback if the delayHandler is not null.
     */
    override fun onDestroy() {
        Log.i(TAG_SPLASH_SCREEN, "OnDestroyCalled")
        if (delayHandler != null) {
            delayHandler!!.removeCallbacks(runnable)
        }
        super.onDestroy()
    }
}