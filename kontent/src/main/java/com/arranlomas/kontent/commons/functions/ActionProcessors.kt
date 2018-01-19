package com.arranlomas.kontent.commons.functions

import com.arranlomas.kontent.commons.objects.mvi.KontentAction
import com.arranlomas.kontent.commons.objects.mvi.KontentResult
import com.arranlomas.kontent.extensions.composeIo
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

fun <T : KontentAction, R : KontentResult> KontentActionToResultProcessor(mapFunction: (Observable<T>) -> Observable<R>): ObservableTransformer<T, R> =
        ObservableTransformer { action: Observable<T> ->
            action.publish { shared ->
                mapFunction.invoke(shared)
            }
        }

fun <T : KontentAction, R : KontentResult> KontentSimpleProcessor(mapFunction: (T) -> Observable<R>): ObservableTransformer<T, R> = ObservableTransformer { action ->
    action.switchMap { action ->
        mapFunction.invoke(action)
    }
}

fun <T : KontentAction, R : KontentResult, O> KontentSimpleNetworkRequestProcessor(
        networkRequest: (T) -> Observable<O>,
        success: (O) -> R,
        error: (Throwable) -> R,
        loading: R
): ObservableTransformer<T, R> = ObservableTransformer { actionObservable ->
    actionObservable.switchMap { action ->
        networkRequest.invoke(action)
                .networkMapper(success, error, loading)
    }
}

private fun <T, R : KontentResult> Observable<T>.networkMapper(success: (T) -> R, error: (Throwable) -> R, loading: R): Observable<R> = compose<R>({ observable ->
    observable
            .map { success.invoke(it) }
            .onErrorReturn { error.invoke(it) }
            .composeIo()
            .startWith(loading)
})

