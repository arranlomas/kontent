package com.arranlomas.kontent.commons.objects

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

open class KontentViewModel<A : KontentAction, R : KontentResult, S : KontentViewState>(
        override val actionProcessor: ObservableTransformer<A, R>,
        override val defaultState: S,
        override val reducer: BiFunction<S, R, S>,
        override val postProcessor: ((S) -> S)? = null)
    : KontentContract.ViewModel<A, R, S> {
    override val stateSubject: BehaviorSubject<S> = BehaviorSubject.create()
    override var intentFilter: ObservableTransformer<A, A>? = null

    override fun <T : A> attachView(intents: Observable<A>, initialIntent: Class<T>): Observable<S> {
        if (intentFilter == null) intentFilter = getInitialIntentFilter(initialIntent)
        return super.attachView(intents, initialIntent)
    }
}