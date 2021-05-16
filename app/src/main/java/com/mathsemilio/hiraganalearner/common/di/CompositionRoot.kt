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
package com.mathsemilio.hiraganalearner.common.di

import com.google.android.gms.ads.AdRequest
import com.mathsemilio.hiraganalearner.common.eventbus.EventBus
import com.mathsemilio.hiraganalearner.common.eventbus.EventPublisher
import com.mathsemilio.hiraganalearner.common.eventbus.EventSubscriber

class CompositionRoot {

    val adRequest: AdRequest by lazy {
        AdRequest.Builder().build()
    }

    private val eventBus by lazy {
        EventBus()
    }

    private val _eventPublisher by lazy {
        EventPublisher(eventBus)
    }

    private val _eventSubscriber by lazy {
        EventSubscriber(eventBus)
    }

    val eventPublisher get() = _eventPublisher

    val eventSubscriber get() = _eventSubscriber
}