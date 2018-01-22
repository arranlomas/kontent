package com.arranlomas.kontent.commons.objects.android

import android.support.v7.app.AppCompatActivity
import com.arranlomas.kontent.commons.objects.mvi.KontentContract
import com.arranlomas.kontent.commons.objects.mvi.KontentIntent
import com.arranlomas.kontent.commons.objects.mvi.KontentViewState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

/**
 * Created by arran on 17/12/2017.
 */
abstract class KontentActivity<I : KontentIntent, S : KontentViewState> : KontentContract.View<I, S>, AppCompatActivity() {

    override val subscriptions = CompositeDisposable()
    private lateinit var interactor: KontentContract.Interactor<I, S>
    lateinit override var intents: Observable<I>
    override var onErrorAction: ((Throwable) -> Unit)? = null

    override fun setup(interactor: KontentContract.Interactor<I, S>, onErrorAction: ((Throwable) -> Unit)?) {
        this.interactor = interactor
        this.onErrorAction = onErrorAction
    }

    override fun setup(interactor: KontentContract.Interactor<I, S>) {
        this.interactor = interactor
    }


    override fun attachIntents(intents: Observable<I>) {
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

    private fun Disposable.addDisposable() {
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