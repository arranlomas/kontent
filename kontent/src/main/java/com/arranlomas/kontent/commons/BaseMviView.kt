package com.arranlomas.kontent.commons

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

/**
 * Created by arran on 17/12/2017.
 */
abstract class BaseMviView<S : BaseMviContract.ViewState, E : BaseMviContract.Intent> : BaseMviContract.View<S, E>{
    override val subscriptions = CompositeDisposable()
    lateinit override var interactor: BaseMviContract.Interactor<S, E>
    lateinit override var intents: Observable<E>
    override var onErrorAction: ((Throwable) -> Unit)? = null

    override fun setup(interactor: BaseMviContract.Interactor<S, E>, onErrorAction: ((Throwable) -> Unit)?) {
        this.interactor = interactor
        this.onErrorAction = onErrorAction
    }

    override fun attachIntents(intents: Observable<E>) {
        this.intents = intents
        interactor.attachView(intents)
                .subscribeWith(object : BaseSubscriber<S>() {
                    override fun onNext(state: S) {
                        render(state)
                    }
                })
                .addDisposable()
    }

    fun onDestroy() {
        subscriptions.dispose()
    }

    fun Disposable.addDisposable() {
        subscriptions.add(this)
    }

    abstract inner class BaseSubscriber<T>(val showLoading: Boolean = true) : DisposableObserver<T>() {
        override fun onError(e: Throwable) {
            onErrorAction
        }

        override fun onStart() {
        }

        override fun onComplete() {

        }
    }
}