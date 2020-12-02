package com.mathsemilio.hiraganalearner.logic.backend

import com.mathsemilio.hiraganalearner.logic.eventWrapper.BackendEvent

interface IObserverContract {
    fun registerObserver(IBackendObserver: IBackendObserver)
    fun removeObserver(IBackendObserver: IBackendObserver)
    fun notifyObserver(event: BackendEvent)
}