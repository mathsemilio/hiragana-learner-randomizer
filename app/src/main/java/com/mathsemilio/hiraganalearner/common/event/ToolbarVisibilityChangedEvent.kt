package com.mathsemilio.hiraganalearner.common.event

class ToolbarVisibilityChangedEvent(private val _event: Event) {

    enum class Event { SHOW_TOOLBAR, HIDE_TOOLBAR }

    val event get() = _event
}