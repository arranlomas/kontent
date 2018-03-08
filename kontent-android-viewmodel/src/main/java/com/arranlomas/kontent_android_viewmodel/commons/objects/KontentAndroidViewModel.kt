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
        intentToAction: (I) -> A,
        actionProcessor: ObservableTransformer<A, R>,
        private val defaultState: S,
        private val reducer: BiFunction<S, R, S>,
        postProcessor: (Function1<S, S>)? = null,
        initialIntentPredicate: ((I) -> Boolean)? = null) : KontentContract.ViewModel<I, S>, ViewModel() {

    private var initialIntentSent = false

    val stateSubject: BehaviorSubject<S> = BehaviorSubject.create()

    private var processor: (Observable<I>) -> Observable<S>

    override fun attachView(intents: Observable<I>): Observable<S> {
        processor.invoke(intents)
                .subscribe(stateSubject)
        return stateSubject
    }

    override fun getLastState(): S? = stateSubject.value ?: defaultState

    init {
        this.processor = { intents ->
            intents
                    .map { initialIntentLogic(it, initialIntentPredicate) }
                    .filter { it.intent != null }
                    .map { it.intent!! }
                    .map { intent -> intentToAction.invoke(intent) }
                    .compose(actionProcessor)
                    .scan(getLastState(), reducer)
                    .map { postProcessor?.invoke(it) ?: it }
        }
    }

    private fun initialIntentLogic(intent: I, initialIntentPredicate: ((I) -> Boolean)?): InitialIntentOptional<I> {
        if (initialIntentPredicate == null) return InitialIntentOptional(intent)

        val isInitialIntent = initialIntentPredicate.invoke(intent)
        return if (isInitialIntent && initialIntentSent) {
            //Was initial intent and initial intent has already been sent so just force emit last ViewState to view
            getLastState()?.let { stateSubject.onNext(it) }
            InitialIntentOptional(null)
        } else if (isInitialIntent && !initialIntentSent) {
            // was the initial intent but was the first one so should emit
            initialIntentSent = true
            InitialIntentOptional(intent)
        } else {
            //was some other intent so should emit
            InitialIntentOptional(intent)
        }
    }

    private data class InitialIntentOptional<out I>(val intent: I?)
}