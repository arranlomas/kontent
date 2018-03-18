package com.arranlomas.kontent.commons.objects

import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

abstract class KontentFragment<I : KontentIntent, A : KontentAction, R : KontentResult, S : KontentViewState>
    : KontentContract.View<I, A, R, S>, Fragment() {

    override lateinit var viewModel: KontentContract.ViewModel<I, A, R, S>
    override val subscriptions: CompositeDisposable = CompositeDisposable()
    override var onErrorAction: ((Throwable) -> Unit)? = null

    override val intentSubscriber: DisposableObserver<S> = object : KontentContract.BaseSubscriber<S>(onErrorAction) {
        override fun onNext(state: S) {
            render(state)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }
}