package com.mathsemilio.hiraganalearner.ui.screens.common

import android.content.Context
import android.view.View
import com.mathsemilio.hiraganalearner.common.BaseObservable

abstract class BaseObservableView<ListenerType> : BaseObservable<ListenerType>(),
    com.mathsemilio.hiraganalearner.ui.screens.common.View {

    private lateinit var mRootView: View

    override fun getRootView() = mRootView

    protected fun setRootView(rootView: View) {
        mRootView = rootView
    }

    protected fun <T : View> findViewById(id: Int): T = getRootView().findViewById(id)

    protected fun getContext(): Context = getRootView().context
}