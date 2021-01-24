package com.mathsemilio.hiraganalearner.ui.screens.common

abstract class BaseObservable<Listener> : IObservable<Listener> {

    private val mListeners = mutableSetOf<Listener>()

    override fun registerListener(listener: Listener) { mListeners.add(listener) }

    override fun removeListener(listener: Listener) { mListeners.remove(listener) }

    protected fun getListeners() = mListeners.toSet()
}