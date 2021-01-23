package com.mathsemilio.hiraganalearner.ui.screens.game.result.usecase

import android.content.Context
import android.content.Intent
import com.mathsemilio.hiraganalearner.R

class ShareGameScoreUseCase(private val mContext: Context, score: Int) {

    private val mShareScoreIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            if (score == 48)
                mContext.getString(R.string.share_perfect_final_score)
            else
                mContext.getString(R.string.share_final_score, score)
        )
        type = "text/plain"
    }

    fun shareGameScore() {
        mContext.apply {
            startActivity(
                Intent.createChooser(
                    mShareScoreIntent,
                    getString(R.string.game_score_create_chooser_title)
                )
            )
        }
    }
}