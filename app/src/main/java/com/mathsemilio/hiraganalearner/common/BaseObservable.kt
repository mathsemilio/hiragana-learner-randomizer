package com.mathsemilio.hiraganalearner.common

abstract class BaseObservable<Listener> : Observable<Listener> {

    private val mListeners = mutableSetOf<Listener>()

    override fun registerListener(listener: Listener) { mListeners.add(listener) }

    override fun removeListener(listener: Listener) { mListeners.remove(listener) }

    protected fun getListeners() = mListeners.toSet()
}