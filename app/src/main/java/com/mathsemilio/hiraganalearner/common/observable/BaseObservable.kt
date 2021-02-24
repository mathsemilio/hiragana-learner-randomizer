package com.mathsemilio.hiraganalearner.common.observable

abstract class BaseObservable<T> : Observable<T> {

    private val listenersSet = mutableSetOf<T>()

    override fun addListener(listener: T) { listenersSet.add(listener) }

    override fun removeListener(listener: T) { listenersSet.remove(listener) }

    protected val listeners get() = listenersSet.toSet()
}