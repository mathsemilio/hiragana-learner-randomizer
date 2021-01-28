package com.mathsemilio.hiraganalearner.common

interface Observable<Listener> {
    fun registerListener(listener: Listener)
    fun removeListener(listener: Listener)
}