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

package com.mathsemilio.hiraganalearner.ui.screens

import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.mathsemilio.hiraganalearner.databinding.ActivityMainBinding

class MainActivityViewImpl(inflater: LayoutInflater) : MainActivityView() {

    init {
        binding = ActivityMainBinding.inflate(inflater)
        binding.materialToolbarApp.setNavigationOnClickListener {
            notifyToolbarNavigationIconClick()
        }
    }

    override val fragmentContainer get() = binding.frameLayoutFragmentContainer

    override fun showToolbar() {
        binding.appBarLayoutApp.isVisible = true
    }

    override fun hideToolbar() {
        binding.appBarLayoutApp.isVisible = false
    }

    private fun notifyToolbarNavigationIconClick() {
        notifyListener { listener ->
            listener.onToolbarNavigationIconClicked()
        }
    }
}