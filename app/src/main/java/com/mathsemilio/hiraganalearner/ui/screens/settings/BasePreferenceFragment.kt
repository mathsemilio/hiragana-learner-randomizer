package com.mathsemilio.hiraganalearner.ui.screens.settings

import androidx.preference.PreferenceFragmentCompat
import com.mathsemilio.hiraganalearner.di.ControllerCompositionRoot
import com.mathsemilio.hiraganalearner.others.HiraganaRandomizerApplication

abstract class BasePreferenceFragment : PreferenceFragmentCompat() {

    protected fun getCompositionRoot(): ControllerCompositionRoot {
        return ControllerCompositionRoot(
            (requireActivity().application as HiraganaRandomizerApplication).compositionRoot,
            fragment = this
        )
    }
}
