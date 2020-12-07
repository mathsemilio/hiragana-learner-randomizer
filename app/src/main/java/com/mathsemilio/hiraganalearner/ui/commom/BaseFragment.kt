package com.mathsemilio.hiraganalearner.ui.commom

import androidx.fragment.app.Fragment
import com.mathsemilio.hiraganalearner.HiraganaRandomizerApplication
import com.mathsemilio.hiraganalearner.di.CompositionRoot

abstract class BaseFragment : Fragment() {
    fun getCompositionRoot(): CompositionRoot {
        return (requireActivity().application as HiraganaRandomizerApplication).compositionRoot
    }
}