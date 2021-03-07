package com.mathsemilio.hiraganalearner.ui.screens.dialog

import androidx.fragment.app.DialogFragment
import com.mathsemilio.hiraganalearner.common.di.ControllerCompositionRoot
import com.mathsemilio.hiraganalearner.ui.screens.MainActivity

abstract class BaseDialogFragment : DialogFragment() {

    private val _compositionRoot by lazy {
        ControllerCompositionRoot((requireActivity() as MainActivity).compositionRoot)
    }
    val compositionRoot get() = _compositionRoot
}