package com.mathsemilio.hiraganalearner.ui.common

import androidx.fragment.app.Fragment
import com.mathsemilio.hiraganalearner.common.dependencyinjection.ControllerCompositionRoot
import com.mathsemilio.hiraganalearner.ui.screens.MainActivity

abstract class BaseFragment : Fragment() {

    private val _compositionRoot by lazy {
        ControllerCompositionRoot((requireActivity() as MainActivity).compositionRoot)
    }
    val compositionRoot get() = _compositionRoot
}