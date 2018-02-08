package com.arranlomas.kontent.commons.objects.mvi

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

open class KontentInteractor<I : KontentIntent, A : KontentAction, R : KontentResult, S : KontentViewState>(
        intentToAction: (I) -> A,
        actionProcessor: ObservableTransformer<A, R>,
        defaultState: S,
        private val reducer: BiFunction<S, R, S>) : KontentContract.Interactor<I, S> {

    val intentsSubject: BehaviorSubject<I> = BehaviorSubject.create()
    val stateSubject: BehaviorSubject<S> = BehaviorSubject.create()

    private var processor: (Observable<I>) -> Observable<S>
    override fun attachView(intents: Observable<I>): Observable<S> {
        intents.subscribe(intentsSubject)
        processor.invoke(intents)
                .subscribe(stateSubject)
        return stateSubject
    }

    init {
        this.processor = { intents ->
            intents
                    .map { intent -> intentToAction.invoke(intent) }
                    .compose(actionProcessor)
                    .scan(defaultState, reducer)
        }
    }

    override fun getLastState(): Observable<S> = Observable.just(stateSubject.value)

}