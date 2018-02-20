package com.arranlomas.kontent.commons.functions

import com.arranlomas.kontent.commons.objects.KontentResult
import com.arranlomas.kontent.commons.objects.KontentViewState
import io.reactivex.functions.BiFunction

fun <R : KontentResult, S : KontentViewState> KontentReducer(reducerFunction: (R, S) -> S)
        = BiFunction { previousState: S, result: R -> reducerFunction.invoke(result, previousState) }