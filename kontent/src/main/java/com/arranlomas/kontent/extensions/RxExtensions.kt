package com.arranlomas.kontent.extensions

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by arran on 17/12/2017.
 */
fun <T> Observable<T>.composeIo(): Observable<T> = compose<T>({
    it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
})
