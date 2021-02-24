package com.mathsemilio.hiraganalearner.ui.screens.game.result

import android.content.Context
import android.content.Intent
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.PERFECT_SCORE

class ShareGameScoreHelper(private val context: Context) {

    private fun getShareGameScoreIntent(score: Int): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getShareGameScoreString(score))
            type = "text/plain"
        }
    }

    private fun getShareGameScoreString(score: Int): String {
        return if (score == PERFECT_SCORE) {
            context.getString(R.string.share_perfect_final_score)
        } else {
            context.getString(R.string.share_final_score, score)
        }
    }

    fun shareGameScore(score: Int) {
        context.apply {
            startActivity(
                Intent.createChooser(
                    getShareGameScoreIntent(score),
                    getString(R.string.game_score_create_chooser_title)
                )
            )
        }
    }
}