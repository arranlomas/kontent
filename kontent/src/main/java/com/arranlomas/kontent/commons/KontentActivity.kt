package com.arranlomas.kontent.commons

import android.support.v7.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

/**
 * Created by arran on 17/12/2017.
 */
abstract class KontentActivity<S : KontentContract.ViewState, E : KontentContract.Intent> : KontentContract.View<S, E>, AppCompatActivity() {

    override val subscriptions = CompositeDisposable()
    private lateinit var interactor: KontentContract.Interactor<S, E>
    lateinit override var intents: Observable<E>
    override var onErrorAction: ((Throwable) -> Unit)? = null

    override fun setup(interactor: KontentContract.Interactor<S, E>, onErrorAction: ((Throwable) -> Unit)?) {
        this.interactor = interactor
        this.onErrorAction = onErrorAction
    }

    override fun setup(interactor: KontentContract.Interactor<S, E>) {
        this.interactor = interactor
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

    override fun onDestroy() {
        super.onDestroy()
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