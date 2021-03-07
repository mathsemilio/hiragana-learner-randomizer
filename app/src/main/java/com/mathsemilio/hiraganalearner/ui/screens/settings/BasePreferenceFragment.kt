package com.mathsemilio.hiraganalearner.ui.screens.settings

import androidx.preference.PreferenceFragmentCompat
import com.mathsemilio.hiraganalearner.common.di.ControllerCompositionRoot
import com.mathsemilio.hiraganalearner.ui.screens.MainActivity

abstract class BasePreferenceFragment : PreferenceFragmentCompat() {

    private val _compositionRoot by lazy {
        ControllerCompositionRoot((requireActivity() as MainActivity).compositionRoot)
    }
    val compositionRoot get() = _compositionRoot
}