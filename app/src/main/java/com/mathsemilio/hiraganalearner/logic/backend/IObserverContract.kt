package com.mathsemilio.hiraganalearner.logic.backend

import com.mathsemilio.hiraganalearner.logic.event.BackendEvent

interface IObserverContract {
    fun registerObserver(IBackendObserver: IBackendObserver)
    fun removeObserver(IBackendObserver: IBackendObserver)
    fun notifyObserver(event: BackendEvent)
}