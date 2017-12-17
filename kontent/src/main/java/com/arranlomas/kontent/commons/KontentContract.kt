package com.arranlomas.kontent.commons

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by arran on 17/12/2017.
 */
interface KontentContract {

    interface View<S : KontentContract.ViewState, E : KontentContract.Intent> {
        val subscriptions: CompositeDisposable
        var intents: Observable<E>
        var onErrorAction: ((Throwable) -> Unit)?
        fun setup(interactor: KontentContract.Interactor<S, E>, onErrorAction: ((Throwable) -> Unit)? = null)
        fun setup(interactor: KontentContract.Interactor<S, E>)

        fun attachIntents(intents: Observable<E>)

        fun render(state: S)
    }

    interface Interactor<S : ViewState, E : Intent> {
        fun attachView(intents: Observable<E>): Observable<S>
    }

    interface ViewState

    interface Intent
}