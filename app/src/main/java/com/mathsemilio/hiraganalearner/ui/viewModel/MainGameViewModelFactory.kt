package com.mathsemilio.hiraganalearner.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class MainGameViewModelFactory(private val gameDifficultyValue: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainGameViewModel::class.java)) {
            return MainGameViewModel(gameDifficultyValue) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}