package com.mathsemilio.hiraganalearner.common.baseobservable

interface Observable<Listener> {
    fun addListener(listener: Listener)
    fun removeListener(listener: Listener)
}