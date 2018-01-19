package com.arranlomas.kontent.commons.functions

import com.arranlomas.kontent.commons.objects.mvi.KontentAction
import com.arranlomas.kontent.commons.objects.mvi.KontentResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

fun <T: KontentAction, R: KontentResult> KontentSimpleProcessor(mapFunction: (T) -> Observable<R>):
        ObservableTransformer<T, R> = ObservableTransformer { action ->
    action.switchMap {
        mapFunction.invoke(it)
    }
}