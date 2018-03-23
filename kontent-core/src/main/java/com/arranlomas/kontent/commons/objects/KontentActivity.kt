package com.arranlomas.kontent.commons.objects

import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

abstract class KontentActivity<A : KontentAction, R : KontentResult, S : KontentViewState>
    : KontentContract.View<A, R, S>, AppCompatActivity() {

    override lateinit var viewModel: KontentContract.ViewModel<A, R, S>
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