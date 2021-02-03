package com.mathsemilio.hiraganalearner.ui.screens.common

import androidx.fragment.app.Fragment
import com.mathsemilio.hiraganalearner.di.ControllerCompositionRoot
import com.mathsemilio.hiraganalearner.ui.screens.MainActivity

abstract class BaseFragment : Fragment() {

    private val _compositionRoot by lazy {
        ControllerCompositionRoot((requireActivity() as MainActivity).activityCompositionRoot)
    }
    val compositionRoot get() = _compositionRoot
}