package com.arranlomas.kontent.commons.objects

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.BehaviorSubject

interface KontentContract {

    interface View<A : KontentAction, R : KontentResult, S : KontentViewState> {
        fun render(state: S)
        var viewModel: KontentContract.ViewModel<A, R, S>
        val subscriptions: CompositeDisposable
        var onErrorAction: ((Throwable) -> Unit)?

        val intentSubscriber: DisposableObserver<S>

        fun setup(viewModel: KontentContract.ViewModel<A, R, S>, onErrorAction: ((Throwable) -> Unit)? = null) {
            this.viewModel = viewModel
            this.onErrorAction = onErrorAction
        }

        fun <T : A> attachIntents(actions: Observable<A>, initialActions: Class<T>) {
            viewModel.attachView(actions, initialActions)
                    .subscribeWith(intentSubscriber)
                    .addDisposable()
        }

        fun attachIntents(actions: Observable<A>) {
            viewModel.attachView(actions)
                    .subscribeWith(intentSubscriber)
                    .addDisposable()
        }

        private fun Disposable.addDisposable() {
            subscriptions.add(this)
        }
    }

    abstract class BaseSubscriber<A>(val onErrorAction: ((Throwable) -> Unit)?, val showLoading: Boolean = true) : DisposableObserver<A>() {
        override fun onError(e: Throwable) {
            onErrorAction?.invoke(e) ?: e.printStackTrace()
        }

        override fun onStart() {
        }

        override fun onComplete() {
        }
    }

    interface ViewModel<A : KontentAction, R : KontentResult, S : KontentViewState> {
        val stateSubject: BehaviorSubject<S>

        var intentFilter: ObservableTransformer<A, A>?

        val actionProcessor: ObservableTransformer<A, R>
        val defaultState: S
        val reducer: BiFunction<S, R, S>
        val postProcessor: (Function1<S, S>)?

        fun <T : A> attachView(intents: Observable<A>, initialIntent: Class<T>): Observable<S> {
            return attachViewMethod(intents,
                    stateSubject,
                    actionProcessor,
                    reducer,
                    { getLastState() },
                    postProcessor,
                    intentFilter)
        }

        fun attachView(intents: Observable<A>): Observable<S> {
            return attachViewMethod(intents,
                    stateSubject,
                    actionProcessor,
                    reducer,
                    { getLastState() },
                    postProcessor,
                    null)
        }

        fun getLastState(): S = stateSubject.value ?: defaultState
    }
}

fun <I, T : I> getInitialIntentFilter(clazz: Class<T>): ObservableTransformer<I, I> {
    var emittedCount = 0
    return ObservableTransformer {
        it.filter { (clazz.isInstance(it) && emittedCount < 1) || !clazz.isInstance(it) }
                .map {
                    if (clazz.isInstance(it)) emittedCount++
                    it
                }
    }
}

internal fun <A, R, S> attachViewMethod(intents: Observable<A>,
                                        subscriber: BehaviorSubject<S>,
                                        actionProcessor: ObservableTransformer<A, R>,
                                        reducer: BiFunction<S, R, S>,
                                        getLastState: () -> S,
                                        postProcessor: (Function1<S, S>)?,
                                        intentFilter: ObservableTransformer<A, A>?): Observable<S> {
    val obs = intentFilter.let { intents.compose(intentFilter) } ?: intents
    obs.applyMvi(actionProcessor, reducer, getLastState, postProcessor)
            .filterOnComplete()
            .subscribe(subscriber)
    return subscriber
}

internal fun <A, R, S> Observable<A>.applyMvi(
        actionProcessor: ObservableTransformer<A, R>,
        reducer: BiFunction<S, R, S>,
        getLastState: () -> S,
        postProcessor: (Function1<S, S>)? = null
): Observable<S> {
    return this
            .compose(actionProcessor)
            .scan(getLastState.invoke(), reducer)
            .map { postProcessor?.invoke(it) ?: it }
}

internal fun <S> Observable<S>.filterOnComplete(): Observable<S> {
    return this.materialize()
            .filter { !it.isOnComplete }
            .dematerialize<S>()
}