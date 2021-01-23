package com.mathsemilio.hiraganalearner.ui.screens.dialog

import androidx.fragment.app.DialogFragment
import com.mathsemilio.hiraganalearner.di.ControllerCompositionRoot
import com.mathsemilio.hiraganalearner.others.HiraganaRandomizerApplication

abstract class BaseDialogFragment : DialogFragment() {

    protected fun getCompositionRoot(): ControllerCompositionRoot {
        return ControllerCompositionRoot(
            (requireActivity().application as HiraganaRandomizerApplication).compositionRoot, this
        )
    }
}