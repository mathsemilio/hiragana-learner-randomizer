package com.mathsemilio.hiraganalearner.ui.screens.game.result.usecase

import android.content.Context
import android.content.Intent
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.PERFECT_SCORE

class ShareGameScoreUseCase(private val context: Context, score: Int) {

    private val shareScoreIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            if (score == PERFECT_SCORE)
                context.getString(R.string.share_perfect_final_score)
            else
                context.getString(R.string.share_final_score, score)
        )
        type = "text/plain"
    }

    fun shareGameScore() {
        context.apply {
            startActivity(
                Intent.createChooser(
                    shareScoreIntent,
                    getString(R.string.game_score_create_chooser_title)
                )
            )
        }
    }
}