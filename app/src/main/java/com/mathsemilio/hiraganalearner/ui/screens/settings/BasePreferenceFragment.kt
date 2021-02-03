package com.mathsemilio.hiraganalearner.ui.screens.settings

import androidx.preference.PreferenceFragmentCompat
import com.mathsemilio.hiraganalearner.di.ControllerCompositionRoot
import com.mathsemilio.hiraganalearner.ui.screens.MainActivity

abstract class BasePreferenceFragment : PreferenceFragmentCompat() {

    private val _compositionRoot by lazy {
        ControllerCompositionRoot((requireActivity() as MainActivity).activityCompositionRoot)
    }
    val compositionRoot get() = _compositionRoot
}