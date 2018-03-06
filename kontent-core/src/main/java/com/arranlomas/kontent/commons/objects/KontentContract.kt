package com.arranlomas.kontent.commons.objects

import io.reactivex.Observable

interface KontentContract {

    interface View<I : KontentIntent, S : KontentViewState> {
        fun setup(viewModel: ViewModel<I, S>, onErrorAction: ((Throwable) -> Unit)? = null)
        fun setup(viewModel: ViewModel<I, S>)
        fun render(state: S)
        fun attachIntents(intents: Observable<I>)
    }

    interface ViewModel<I : KontentIntent, S : KontentViewState> {
        fun attachView(intents: Observable<I>): Observable<S>
        fun getLastState(): S?
    }
}