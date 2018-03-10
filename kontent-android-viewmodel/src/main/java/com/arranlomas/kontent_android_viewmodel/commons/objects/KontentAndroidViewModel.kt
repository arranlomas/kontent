package com.arranlomas.kontent_android_viewmodel.commons.objects

import android.arch.lifecycle.ViewModel
import com.arranlomas.kontent.commons.objects.*
import com.arranlomas.kontent.extensions.notOfType
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
        private val initialIntent: I?) : KontentContract.ViewModel<I, S>, ViewModel() {

    private val intentFilter: ObservableTransformer<I, I>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                if (initialIntent == null) return@publish shared
                Observable.merge<I>(
                        shared.ofType(initialIntent::class.java).take(1),
                        shared.notOfType(initialIntent::class.java)
                )
            }
        }


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
                    .compose(intentFilter)
                    .map { intent -> intentToAction.invoke(intent) }
                    .compose(actionProcessor)
                    .scan(getLastState(), reducer)
                    .map { postProcessor?.invoke(it) ?: it }
        }
    }
}