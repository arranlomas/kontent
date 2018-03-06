package com.arranlomas.daggerkontent

import android.annotation.SuppressLint
import com.arranlomas.kontent.commons.objects.KontentContract
import com.arranlomas.kontent.commons.objects.KontentIntent
import com.arranlomas.kontent.commons.objects.KontentViewState
import dagger.android.support.DaggerFragment
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

abstract class DaggerKontentFragment<I : KontentIntent, S : KontentViewState> : KontentContract.View<I, S>, DaggerFragment() {

    private val subscriptions = CompositeDisposable()
    private lateinit var viewModel: KontentContract.ViewModel<I, S>
    private lateinit var intents: Observable<I>
    private var onErrorAction: ((Throwable) -> Unit)? = null

    override fun setup(viewModel: KontentContract.ViewModel<I, S>, onErrorAction: ((Throwable) -> Unit)?) {
        this.viewModel = viewModel
        this.onErrorAction = onErrorAction
    }

    override fun setup(viewModel: KontentContract.ViewModel<I, S>) {
        this.viewModel = viewModel
    }

    override fun attachIntents(intents: Observable<I>) {
        this.intents = intents
        viewModel.attachView(intents)
                .subscribeWith(object : BaseSubscriber<S>() {
                    override fun onNext(state: S) {
                        render(state)
                    }
                })
                .addDisposable()
    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }

    private fun Disposable.addDisposable() {
        subscriptions.add(this)
    }

    abstract inner class BaseSubscriber<T>(val showLoading: Boolean = true) : DisposableObserver<T>() {
        override fun onError(e: Throwable) {
            onErrorAction?.invoke(e) ?: e.printStackTrace()
        }

        override fun onStart() {
        }

        override fun onComplete() {

        }
    }
}