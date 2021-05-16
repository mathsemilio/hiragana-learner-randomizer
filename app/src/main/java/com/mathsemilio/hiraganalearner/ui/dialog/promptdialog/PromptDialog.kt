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
package com.mathsemilio.hiraganalearner.ui.dialog.promptdialog

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mathsemilio.hiraganalearner.common.*
import com.mathsemilio.hiraganalearner.common.eventbus.EventPublisher
import com.mathsemilio.hiraganalearner.ui.dialog.BaseDialogFragment
import com.mathsemilio.hiraganalearner.ui.common.event.PromptDialogEvent

class PromptDialog private constructor() : BaseDialogFragment(), PromptDialogView.Listener {

    companion object Factory {
        fun newInstance(
            dialogTitle: String,
            dialogMessage: String,
            positiveButtonText: String,
            negativeButtonText: String?,
            isCancelable: Boolean = false
        ): PromptDialog {
            val args = Bundle(5).apply {
                putString(ARG_DIALOG_TITLE, dialogTitle)
                putString(ARG_DIALOG_MESSAGE, dialogMessage)
                putString(ARG_POSITIVE_BUTTON_TEXT, positiveButtonText)
                putString(ARG_NEGATIVE_BUTTON_TEXT, negativeButtonText)
                putBoolean(ARG_IS_CANCELABLE, isCancelable)
            }
            val promptDialog = PromptDialog()
            promptDialog.arguments = args
            return promptDialog
        }
    }

    private lateinit var dialogView: PromptDialogView

    private lateinit var eventPublisher: EventPublisher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventPublisher = compositionRoot.eventPublisher
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogView = compositionRoot.viewFactory.promptDialogView

        dialogView.apply {
            setTitle(requireArguments().getString(ARG_DIALOG_TITLE, ""))
            setMessage(requireArguments().getString(ARG_DIALOG_MESSAGE, ""))
            setPositiveButtonText(requireArguments().getString(ARG_POSITIVE_BUTTON_TEXT, ""))
            setNegativeButtonText(requireArguments().getString(ARG_NEGATIVE_BUTTON_TEXT))
        }

        val dialogBuilder = MaterialAlertDialogBuilder(requireContext()).apply {
            setView(dialogView.rootView)
            isCancelable = requireArguments().getBoolean(ARG_IS_CANCELABLE, false)
        }

        return dialogBuilder.create()
    }

    override fun onPositiveButtonClicked() {
        dismiss()
        eventPublisher.publish(PromptDialogEvent.PositiveButtonClicked)
    }

    override fun onNegativeButtonClicked() {
        dismiss()
        eventPublisher.publish(PromptDialogEvent.NegativeButtonClicked)
    }

    override fun onStart() {
        super.onStart()
        dialogView.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        dialogView.removeListener(this)
    }
}