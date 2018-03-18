package com.arranlomas.kotentdaggersupport

import com.arranlomas.kontent.commons.objects.KontentAction
import com.arranlomas.kontent.commons.objects.KontentContract
import com.arranlomas.kontent.commons.objects.KontentIntent
import com.arranlomas.kontent.commons.objects.KontentResult
import com.arranlomas.kontent.commons.objects.KontentViewState
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

/**
 * Created by arran on 23/01/2018.
 */
abstract class KontentDaggerSupportActivity<I : KontentIntent, A: KontentAction, R: KontentResult, S : KontentViewState>
    : KontentContract.View<I, A, R, S>, DaggerAppCompatActivity() {

    private val subscriptions = CompositeDisposable()
    private lateinit var viewModel: KontentContract.ViewModel<I, A, R, S>
    private lateinit var intents: Observable<I>
    private var onErrorAction: ((Throwable) -> Unit)? = null

    override fun setup(viewModel: KontentContract.ViewModel<I, A, R, S>, onErrorAction: ((Throwable) -> Unit)?) {
        this.viewModel = viewModel
        this.onErrorAction = onErrorAction
    }

    fun <T : I> attachIntents(intents: Observable<I>, initialIntent: Class<T>) {
        this.intents = intents
        viewModel.attachView(intents, initialIntent)
                .subscribeWith(intentSubscriber)
                .addDisposable()
    }

    fun attachIntents(intents: Observable<I>) {
        this.intents = intents
        viewModel.attachView(intents)
                .subscribeWith(intentSubscriber)
                .addDisposable()
    }

    private val intentSubscriber: DisposableObserver<S> = object : BaseSubscriber<S>() {
        override fun onNext(state: S) {
            render(state)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }

    private fun Disposable.addDisposable() {
        subscriptions.add(this)
    }

    abstract inner class BaseSubscriber<A>(val showLoading: Boolean = true) : DisposableObserver<A>() {
        override fun onError(e: Throwable) {
            onErrorAction?.invoke(e) ?: e.printStackTrace()
        }

        override fun onStart() {
        }

        override fun onComplete() {
        }
    }
}