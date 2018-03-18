package com.arranlomas.kontent_android_viewmodel.commons.objects

import android.arch.lifecycle.ViewModel
import com.arranlomas.kontent.commons.objects.KontentAction
import com.arranlomas.kontent.commons.objects.KontentContract
import com.arranlomas.kontent.commons.objects.KontentIntent
import com.arranlomas.kontent.commons.objects.KontentResult
import com.arranlomas.kontent.commons.objects.KontentViewState
import com.arranlomas.kontent.commons.objects.getInitialIntentFilter
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by arran on 20/02/2018.
 */
open class KontentAndroidViewModel<I : KontentIntent, A : KontentAction, R : KontentResult, S : KontentViewState>(
        override val intentToAction: (I) -> A,
        override val actionProcessor: ObservableTransformer<A, R>,
        override val defaultState: S,
        override val reducer: BiFunction<S, R, S>,
        override val postProcessor: ((S) -> S)? = null)
    : KontentContract.ViewModel<I, A, R, S>, ViewModel() {
    override val stateSubject: BehaviorSubject<S> = BehaviorSubject.create()
    override var intentFilter: ObservableTransformer<I, I>? = null

    override fun <T : I> attachView(intents: Observable<I>, initialIntent: Class<T>): Observable<S> {
        if (intentFilter == null) intentFilter = getInitialIntentFilter(initialIntent)
        return super.attachView(intents, initialIntent)
    }
}