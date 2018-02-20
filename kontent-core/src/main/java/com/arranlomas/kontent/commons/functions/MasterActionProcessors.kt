package com.arranlomas.kontent.commons.functions

import com.arranlomas.kontent.commons.objects.KontentAction
import com.arranlomas.kontent.commons.objects.KontentResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

fun <T : KontentAction, R : KontentResult> KontentMasterProcessor(mapFunction: (Observable<T>) -> Observable<R>): ObservableTransformer<T, R> =
        ObservableTransformer { action: Observable<T> ->
            action.publish { shared ->
                mapFunction.invoke(shared)
            }
        }

fun <T : KontentAction, R : KontentResult> KontentMasterProcessorSingleAction(transformer: ObservableTransformer<T, R>): ObservableTransformer<T, R> = ObservableTransformer { action ->
    action.compose(transformer)
}