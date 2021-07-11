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

import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.mathsemilio.hiraganalearner.databinding.PromptDialogBinding

class PromptDialogViewImpl(inflater: LayoutInflater) : PromptDialogView() {

    init {
        binding = PromptDialogBinding.inflate(inflater, null, false)
    }

    override fun setTitle(title: String) {
        binding.textViewPromptDialogTitle.text = title
    }

    override fun setMessage(message: String) {
        binding.textViewPromptDialogMessage.text = message
    }

    override fun setPositiveButtonText(positiveButtonText: String) {
        binding.buttonPromptDialogPositive.apply {
            text = positiveButtonText
            setOnClickListener { notifyPositiveButtonClicked() }
        }
    }

    override fun setNegativeButtonText(negativeButtonText: String?) {
        if (negativeButtonText != null)
            binding.buttonPromptDialogNegative.apply {
                text = negativeButtonText
                setOnClickListener { notifyNegativeButtonClicked() }
            }
        else
            binding.buttonPromptDialogNegative.isVisible = false
    }

    private fun notifyPositiveButtonClicked() {
        notifyListener { listener ->
            listener.onPositiveButtonClicked()
        }
    }

    private fun notifyNegativeButtonClicked() {
        notifyListener { listener ->
            listener.onNegativeButtonClicked()
        }
    }
}