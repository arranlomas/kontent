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

        val actionsSubscriber: DisposableObserver<S>

        fun setup(viewModel: KontentContract.ViewModel<A, R, S>, onErrorAction: ((Throwable) -> Unit)? = null) {
            this.viewModel = viewModel
            this.onErrorAction = onErrorAction
        }

        fun <T : A> attachActions(actions: Observable<A>, initialActions: Class<T>) {
            viewModel.attachView(actions, initialActions)
                    .subscribeWith(actionsSubscriber)
                    .addDisposable()
        }

        fun attachActions(actions: Observable<A>) {
            viewModel.attachView(actions)
                    .subscribeWith(actionsSubscriber)
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

        var actionFilter: ObservableTransformer<A, A>?

        val actionProcessor: ObservableTransformer<A, R>
        val defaultState: S
        val reducer: BiFunction<S, R, S>
        val postProcessor: (Function1<S, S>)?

        fun <T : A> attachView(actions: Observable<A>, initialAction: Class<T>): Observable<S> {
            return attachViewMethod(actions,
                    stateSubject,
                    actionProcessor,
                    reducer,
                    { getLastState() },
                    postProcessor,
                    actionFilter)
        }

        fun attachView(actions: Observable<A>): Observable<S> {
            return attachViewMethod(actions,
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

fun <I, T : I> getInitialActionFilter(clazz: Class<T>): ObservableTransformer<I, I> {
    var emittedCount = 0
    return ObservableTransformer {
        it.filter { (clazz.isInstance(it) && emittedCount < 1) || !clazz.isInstance(it) }
                .map {
                    if (clazz.isInstance(it)) emittedCount++
                    it
                }
    }
}

internal fun <A, R, S> attachViewMethod(actions: Observable<A>,
                                        subscriber: BehaviorSubject<S>,
                                        actionProcessor: ObservableTransformer<A, R>,
                                        reducer: BiFunction<S, R, S>,
                                        getLastState: () -> S,
                                        postProcessor: (Function1<S, S>)?,
                                        actionFilter: ObservableTransformer<A, A>?): Observable<S> {
    val obs = actionFilter.let { actions.compose(actionFilter) } ?: actions
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