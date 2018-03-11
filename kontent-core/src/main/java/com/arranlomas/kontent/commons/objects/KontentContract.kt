package com.arranlomas.kontent.commons.objects

import io.reactivex.Observable

interface KontentContract {

    interface View<I : KontentIntent, S : KontentViewState> {
        fun setup(viewModel: ViewModel<I, S>, onErrorAction: ((Throwable) -> Unit)? = null)
        fun render(state: S)
    }

    interface ViewModel<I : KontentIntent, S : KontentViewState> {
        fun <T: I> attachView(intents: Observable<I>, initialIntent: Class<T>): Observable<S>
        fun attachView(intents: Observable<I>): Observable<S>
        fun getLastState(): S?
    }
}