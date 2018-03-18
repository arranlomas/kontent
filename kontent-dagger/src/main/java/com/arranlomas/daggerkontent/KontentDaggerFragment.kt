package com.arranlomas.daggerkontent

import android.annotation.SuppressLint
import com.arranlomas.kontent.commons.objects.KontentAction
import com.arranlomas.kontent.commons.objects.KontentContract
import com.arranlomas.kontent.commons.objects.KontentIntent
import com.arranlomas.kontent.commons.objects.KontentResult
import com.arranlomas.kontent.commons.objects.KontentViewState
import dagger.android.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

abstract class KontentDaggerFragment<I : KontentIntent, A : KontentAction, R : KontentResult, S : KontentViewState>
    : KontentContract.View<I, A, R, S>, DaggerFragment() {

    override lateinit var viewModel: KontentContract.ViewModel<I, A, R, S>
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