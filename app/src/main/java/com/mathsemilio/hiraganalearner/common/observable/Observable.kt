package com.mathsemilio.hiraganalearner.common.observable

interface Observable<T> {
    fun addListener(listener: T)
    fun removeListener(listener: T)
}