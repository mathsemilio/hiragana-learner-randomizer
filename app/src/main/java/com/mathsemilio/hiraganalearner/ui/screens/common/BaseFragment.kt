package com.mathsemilio.hiraganalearner.ui.screens.common

import androidx.fragment.app.Fragment
import com.mathsemilio.hiraganalearner.di.ControllerCompositionRoot
import com.mathsemilio.hiraganalearner.others.HiraganaRandomizerApplication

abstract class BaseFragment : Fragment() {

    protected fun getCompositionRoot(): ControllerCompositionRoot {
        return ControllerCompositionRoot(
            (requireActivity().application as HiraganaRandomizerApplication).compositionRoot, this
        )
    }
}