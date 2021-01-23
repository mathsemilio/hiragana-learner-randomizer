package com.mathsemilio.hiraganalearner.ui.screens.common

interface IObservable<Listener> {
    fun registerListener(listener: Listener)
    fun removeListener(listener: Listener)
}