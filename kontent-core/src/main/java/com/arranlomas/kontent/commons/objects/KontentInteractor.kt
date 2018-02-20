package com.arranlomas.kontent.commons.objects

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

open class KontentInteractor<I : KontentIntent, A : KontentAction, R : KontentResult, S : KontentViewState>(
        intentToAction: (I) -> A,
        actionProcessor: ObservableTransformer<A, R>,
        defaultState: S,
        private val reducer: BiFunction<S, R, S>,
        postProcessor: (Function1<S, S>)? = null) : KontentContract.Interactor<I, S> {

    val intentsSubject: BehaviorSubject<I> = BehaviorSubject.create()
    val stateSubject: BehaviorSubject<S> = BehaviorSubject.create()

    private var processor: (Observable<I>) -> Observable<S>

    override fun attachView(intents: Observable<I>): Observable<S> {
        intents.subscribe(intentsSubject)
        processor.invoke(intents)
                .subscribe(stateSubject)
        return stateSubject
    }

    override fun getLastState(): S? = stateSubject.value

    init {
        this.processor = { intents ->
            intents
                    .map { intent -> intentToAction.invoke(intent) }
                    .compose(actionProcessor)
                    .scan(defaultState, reducer)
                    .map { postProcessor?.invoke(it) ?: it }
        }
    }
}