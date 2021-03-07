package com.mathsemilio.hiraganalearner.common.event.poster

import com.mathsemilio.hiraganalearner.common.observable.BaseObservable

class EventPoster : BaseObservable<EventPoster.Listener>() {

    interface Listener {
        fun onEvent(event: Any)
    }

    fun postEvent(event: Any) {
        listeners.forEach { it.onEvent(event) }
    }
}