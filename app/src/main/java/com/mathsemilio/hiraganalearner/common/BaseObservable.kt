package com.mathsemilio.hiraganalearner.common

import com.mathsemilio.hiraganalearner.ui.screens.common.Observable

abstract class BaseObservable<Listener> : Observable<Listener> {

    private val mListeners = mutableSetOf<Listener>()

    override fun registerListener(listener: Listener) {
        mListeners.add(listener)
    }

    override fun removeListener(listener: Listener) {
        mListeners.remove(listener)
    }

    protected fun getListeners() = mListeners.toSet()
}