package com.arranlomas.daggerkontent

import android.annotation.SuppressLint
import com.arranlomas.kontent.commons.objects.*
import dagger.android.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

abstract class KontentDaggerFragment<A : KontentAction, R : KontentResult, S : KontentViewState>
    : KontentContract.View<A, R, S>, DaggerFragment() {

    override lateinit var viewModel: KontentContract.ViewModel<A, R, S>
    override val subscriptions: CompositeDisposable = CompositeDisposable()
    override var onErrorAction: ((Throwable) -> Unit)? = null

    override val intentSubscriber: DisposableObserver<S> = object : KontentContract.BaseSubscriber<S>(onErrorAction) {
        override fun onNext(state: S) {
            render(state)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }
}