package com.arranlomas.kontent.commons.functions

import com.arranlomas.kontent.commons.objects.mvi.KontentResult
import com.arranlomas.kontent.commons.objects.mvi.KontentViewState
import io.reactivex.functions.BiFunction

/**
 * Created by arran on 19/01/2018.
 */
fun <R : KontentResult, S : KontentViewState> KontentReducer(reducerFunction: (R, S) -> S)
        = BiFunction { previousState: S, result: R -> reducerFunction.invoke(result, previousState) }