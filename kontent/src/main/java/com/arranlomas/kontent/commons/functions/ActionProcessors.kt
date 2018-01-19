package com.arranlomas.kontent.commons.functions

import com.arranlomas.kontent.commons.KontentAction
import com.arranlomas.kontent.commons.KontentResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/**
 * Created by arran on 19/01/2018.
 */
fun <T: KontentAction, R: KontentResult> KontentSimpleProcessor(mapFunction: (T) -> Observable<R>):
        ObservableTransformer<T, R> = ObservableTransformer { action ->
    action.switchMap {
        mapFunction.invoke(it)
    }
}