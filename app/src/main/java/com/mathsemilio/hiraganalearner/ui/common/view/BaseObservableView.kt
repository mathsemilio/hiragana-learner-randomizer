package com.mathsemilio.hiraganalearner.ui.common.view

import android.content.Context
import android.view.View
import com.mathsemilio.hiraganalearner.common.observable.BaseObservable

abstract class BaseObservableView<L> : BaseObservable<L>(), IView {

    private lateinit var _rootView: View

    override var rootView
        get() = _rootView
        set(value) {
            _rootView = value
        }

    protected fun <T : View> findViewById(id: Int): T = _rootView.findViewById(id)

    protected val context: Context get() = _rootView.context
}