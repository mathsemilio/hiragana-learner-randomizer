package com.mathsemilio.hiraganalearner.logic.backend

import com.mathsemilio.hiraganalearner.logic.event.BackendEvent

interface IBackendObserver {
    fun onBackendEvent(event: BackendEvent)
}