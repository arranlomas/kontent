package com.arranlomas.kontent.commons.functions

import com.arranlomas.kontent.commons.objects.KontentViewState

/**
 * Created by arran on 8/02/2018.
 */
fun <S : KontentViewState>KontentPostProcessor(processor: (S) -> S): Function1<S, S> = {
    processor.invoke(it)
}