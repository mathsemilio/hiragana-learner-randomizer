package com.mathsemilio.hiraganalearner.common.observable

interface Observable<Listener> {
    fun addListener(listener: Listener)
    fun removeListener(listener: Listener)
}