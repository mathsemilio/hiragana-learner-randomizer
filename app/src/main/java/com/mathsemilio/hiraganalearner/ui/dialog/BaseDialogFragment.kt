/*
Copyright 2020 Matheus Menezes

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.mathsemilio.hiraganalearner.ui.dialog

import androidx.fragment.app.DialogFragment
import com.mathsemilio.hiraganalearner.common.di.ControllerCompositionRoot
import com.mathsemilio.hiraganalearner.ui.screens.MainActivity

abstract class BaseDialogFragment : DialogFragment() {

    private val _compositionRoot by lazy {
        ControllerCompositionRoot((requireActivity() as MainActivity).compositionRoot)
    }
    val compositionRoot get() = _compositionRoot
}