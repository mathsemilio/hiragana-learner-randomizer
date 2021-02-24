package com.mathsemilio.hiraganalearner.ui.screens

import androidx.appcompat.app.AppCompatActivity
import com.mathsemilio.hiraganalearner.common.dependencyinjection.ActivityCompositionRoot
import com.mathsemilio.hiraganalearner.others.HiraganaRandomizerApplication

abstract class BaseActivity : AppCompatActivity() {

    private val _compositionRoot by lazy {
        ActivityCompositionRoot(
            (application as HiraganaRandomizerApplication).compositionRoot,
            activity = this
        )
    }
    val compositionRoot get() = _compositionRoot
}