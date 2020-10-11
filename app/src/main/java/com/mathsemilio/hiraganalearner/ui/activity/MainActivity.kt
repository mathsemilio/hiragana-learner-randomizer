package com.mathsemilio.hiraganalearner.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.mathsemilio.hiraganalearner.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Activity that hosts the Game fragments.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        setupUI()

        setupOnDestinationChangedListener()
    }

    private fun setupUI() {
        setSupportActionBar(toolbar_app as Toolbar?)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    private fun setupOnDestinationChangedListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.settingsFragment) {
                supportActionBar?.hide()
            } else {
                supportActionBar?.show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.popBackStack()
    }
}