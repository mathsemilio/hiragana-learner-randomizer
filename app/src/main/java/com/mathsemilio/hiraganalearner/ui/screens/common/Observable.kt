package com.mathsemilio.hiraganalearner.ui.screens.common

interface Observable<Listener> {
    fun registerListener(listener: Listener)
    fun removeListener(listener: Listener)
}