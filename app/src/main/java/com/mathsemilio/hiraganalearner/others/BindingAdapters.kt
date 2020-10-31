package com.mathsemilio.hiraganalearner.others

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.mathsemilio.hiraganalearner.R

@BindingAdapter("app:hideOnDifficultyText")
fun hideOnDifficultyText(textView: TextView, defaultDifficulty: String) {
    textView.visibility = if (defaultDifficulty == "0") View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("app:setOnDifficultyText")
fun setOnDifficultyText(textView: TextView, defaultDifficulty: String) {
    textView.apply {
        text = context.getString(
            R.string.on_game_difficulty, when (defaultDifficulty) {
                DEFAULT_DIFFICULTY_EASY -> context.getString(R.string.game_difficulty_beginner)
                DEFAULT_DIFFICULTY_MEDIUM -> context.getString(R.string.game_difficulty_medium)
                else -> context.getString(R.string.game_difficulty_hard)
            }
        )
    }
}

@BindingAdapter("app:hideViewWhenDifficultyIsPreselected")
fun hideViewWhenDifficultyIsPreselected(view: View, defaultDifficulty: String) {
    view.visibility = if (defaultDifficulty == "0") View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("app:enableButtonForDifficultyPreselected")
fun enableButtonForDifficultyPreselected(button: Button, defaultDifficulty: String) {
    button.isEnabled = defaultDifficulty != "0"
}

@BindingAdapter("app:setGameDifficultyText")
fun setGameDifficultyText(textView: TextView, gameDifficultyValue: Int) {
    textView.text = when (gameDifficultyValue) {
        GAME_DIFFICULTY_VALUE_BEGINNER -> textView.context.getString(R.string.game_difficulty_beginner)
        GAME_DIFFICULTY_VALUE_MEDIUM -> textView.context.getString(R.string.game_difficulty_medium)
        else -> textView.context.getString(R.string.game_difficulty_hard)
    }
}

@BindingAdapter("app:setCountDownTimerProgressBarMax")
fun setCountDownTimerProgressBarMax(progressBar: ProgressBar, gameDifficultyValue: Int) {
    progressBar.max = when (gameDifficultyValue) {
        GAME_DIFFICULTY_VALUE_BEGINNER -> PROGRESS_BAR_MAX_VALUE_BEGINNER
        GAME_DIFFICULTY_VALUE_MEDIUM -> PROGRESS_BAR_MAX_VALUE_MEDIUM
        else -> PROGRESS_BAR_MAX_VALUE_HARD
    }
}

@BindingAdapter("app:setFinalScoreText")
fun setFinalScoreText(textView: TextView, gameScore: Int) {
    textView.text = when (gameScore) {
        PERFECT_SCORE -> textView.context.getString(R.string.perfect_score)
        else -> textView.context.getString(R.string.final_score)
    }
}

@BindingAdapter("app:setRatingBarStarNumber")
fun setRatingBarStarNumber(ratingBar: RatingBar, gameScore: Int) {
    when {
        gameScore <= 12 -> {
            ratingBar.rating = 1.0F
        }
        gameScore in 13..23 -> {
            ratingBar.rating = 2.0F
        }
        gameScore in 24..47 -> {
            ratingBar.rating = 3.0F
        }
        else -> ratingBar.rating = 4.0F
    }
}