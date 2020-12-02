package com.mathsemilio.hiraganalearner.logic.backend

import com.mathsemilio.hiraganalearner.logic.eventWrapper.BackendEvent

interface IBackendObserver {
    fun onBackendEvent(event: BackendEvent)
}