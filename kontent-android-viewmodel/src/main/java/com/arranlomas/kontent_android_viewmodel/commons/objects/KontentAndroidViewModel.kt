package com.arranlomas.kontent_android_viewmodel.commons.objects

import android.arch.lifecycle.ViewModel
import com.arranlomas.kontent.commons.objects.*
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by arran on 20/02/2018.
 */

open class KontentAndroidViewModel<I : KontentIntent, A : KontentAction, R : KontentResult, S : KontentViewState>(
        private val intentToAction: (I) -> A,
        private val actionProcessor: ObservableTransformer<A, R>,
        private val defaultState: S,
        private val reducer: BiFunction<S, R, S>,
        private val postProcessor: (Function1<S, S>)? = null) : KontentContract.ViewModel<I, S>, ViewModel() {

    private val stateSubject: BehaviorSubject<S> = BehaviorSubject.create()

    private var intentFilter: ObservableTransformer<I, I>? = null

    override fun <T : I> attachView(intents: Observable<I>, initialIntent: Class<T>): Observable<S> {
        if(intentFilter == null) intentFilter = getInitialIntentFilter(initialIntent)
        intents.compose(intentFilter)
                .applyMvi()
                .subscribe(stateSubject)
        return stateSubject
    }

    override fun attachView(intents: Observable<I>): Observable<S> {
        intents.applyMvi()
                .subscribe(stateSubject)
        return stateSubject
    }
    override fun getLastState(): S? = stateSubject.value ?: defaultState

    private fun Observable<I>.applyMvi(): Observable<S> {
        return this.map { intent -> intentToAction.invoke(intent) }
                .compose(actionProcessor)
                .scan(getLastState(), reducer)
                .map { postProcessor?.invoke(it) ?: it }
    }
}