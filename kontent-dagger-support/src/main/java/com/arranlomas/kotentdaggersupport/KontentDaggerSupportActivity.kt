package com.arranlomas.kotentdaggersupport

import com.arranlomas.kontent.commons.objects.KontentAction
import com.arranlomas.kontent.commons.objects.KontentContract
import com.arranlomas.kontent.commons.objects.KontentIntent
import com.arranlomas.kontent.commons.objects.KontentResult
import com.arranlomas.kontent.commons.objects.KontentViewState
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

/**
 * Created by arran on 23/01/2018.
 */
abstract class KontentDaggerSupportActivity<I : KontentIntent, A : KontentAction, R : KontentResult, S : KontentViewState>
    : KontentContract.View<I, A, R, S>, DaggerAppCompatActivity() {

    override lateinit var viewModel: KontentContract.ViewModel<I, A, R, S>
    override val subscriptions: CompositeDisposable = CompositeDisposable()
    override var onErrorAction: ((Throwable) -> Unit)? = null

    override val intentSubscriber: DisposableObserver<S> = object : KontentContract.BaseSubscriber<S>(onErrorAction) {
        override fun onNext(state: S) {
            render(state)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }
}