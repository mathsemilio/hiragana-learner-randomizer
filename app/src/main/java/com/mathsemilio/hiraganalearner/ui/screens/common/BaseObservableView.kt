package com.mathsemilio.hiraganalearner.ui.screens.common

import android.content.Context
import android.view.View
import com.mathsemilio.hiraganalearner.common.observable.BaseObservable

abstract class BaseObservableView<ListenerType> : BaseObservable<ListenerType>(), IView {

    private lateinit var rootView: View

    override fun getRootView() = rootView

    protected fun setRootView(view: View) { rootView = view }

    protected fun <T : View> findViewById(id: Int): T = getRootView().findViewById(id)

    protected val context: Context get() = getRootView().context
}