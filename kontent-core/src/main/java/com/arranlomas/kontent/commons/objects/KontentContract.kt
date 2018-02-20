package com.arranlomas.kontent.commons.objects

import io.reactivex.Observable

interface KontentContract {

    interface View<I : KontentIntent, S : KontentViewState> {
        fun setup(interactor: Interactor<I, S>, onErrorAction: ((Throwable) -> Unit)? = null)
        fun setup(interactor: Interactor<I, S>)
        fun render(state: S)
        fun attachIntents(intents: Observable<I>)
    }

    interface Interactor<I : KontentIntent, S : KontentViewState> {
        fun attachView(intents: Observable<I>): Observable<S>
        fun getLastState(): S?
    }
}