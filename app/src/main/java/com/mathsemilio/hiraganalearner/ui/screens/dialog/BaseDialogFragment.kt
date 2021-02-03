package com.mathsemilio.hiraganalearner.ui.screens.dialog

import androidx.fragment.app.DialogFragment
import com.mathsemilio.hiraganalearner.di.ControllerCompositionRoot
import com.mathsemilio.hiraganalearner.ui.screens.MainActivity

abstract class BaseDialogFragment : DialogFragment() {

    private val _compositionRoot by lazy {
        ControllerCompositionRoot((requireActivity() as MainActivity).activityCompositionRoot)
    }
    val compositionRoot get() = _compositionRoot
}