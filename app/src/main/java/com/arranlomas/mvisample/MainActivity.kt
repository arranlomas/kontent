package com.arranlomas.mvisample

import android.app.ProgressDialog
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.arranlomas.kontent.commons.functions.KontentActionProcessor
import com.arranlomas.kontent.commons.functions.KontentMasterProcessor
import com.arranlomas.kontent.commons.functions.KontentReducer
import com.arranlomas.kontent.commons.functions.KontentSimpleActionProcessor
import com.arranlomas.kontent.commons.objects.KontentAction
import com.arranlomas.kontent.commons.objects.KontentResult
import com.arranlomas.kontent.commons.objects.KontentViewState
import com.arranlomas.kontent_android_viewmodel.commons.objects.KontentAndroidViewModel
import com.arranlomas.kotentdaggersupport.KontentDaggerSupportActivity
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity :
        KontentDaggerSupportActivity<MainActions, MainResults, MainViewState>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressDialog = ProgressDialog(this)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        super.setup(viewModel, { it.printStackTrace() })
        super.attachIntents(intents(), MainActions.LoadPreviousScore::class.java)
    }

    private fun intents() = Observable.merge(incrementTeamA(), incrementTeamB(), initialIntent())

    private fun initialIntent(): Observable<MainActions> = Observable.just(MainActions.LoadPreviousScore())

    private fun incrementTeamA(): Observable<MainActions> = RxView.clicks(teamAButton)
            .map { MainActions.IncrementTeamA() }

    private fun incrementTeamB(): Observable<MainActions> = RxView.clicks(teamBButton)
            .map { MainActions.IncrementTeamB() }

    override fun render(state: MainViewState) {
        teamAScore.text = state.teamAScore.toString()
        teamBScore.text = state.teamBScore.toString()

        if (state.loading) progressDialog.show()
        else progressDialog.hide()
    }
}

class MainViewModel @Inject constructor(scoreRepository: IScoreRepository) :
        KontentAndroidViewModel<MainActions, MainResults, MainViewState>(
                actionProcessor = actionProcessor(scoreRepository),
                reducer = reducer,
                defaultState = MainViewState()
        )

fun actionProcessor(scoreRepository: IScoreRepository) = KontentMasterProcessor<MainActions, MainResults> { actionObservable ->
    Observable.merge(
            actionObservable.ofType(MainActions.IncrementTeamA::class.java)
                    .compose(KontentSimpleActionProcessor { Observable.just(MainResults.IncrementTeamA()) }),
            actionObservable.ofType(MainActions.IncrementTeamB::class.java)
                    .compose(KontentSimpleActionProcessor { Observable.just(MainResults.IncrementTeamB()) }),
            actionObservable.ofType(MainActions.LoadPreviousScore::class.java)
                    .compose(loadPreviousScoreProcessor(scoreRepository))
    )
}

fun loadPreviousScoreProcessor(scoreRepository: IScoreRepository) = KontentActionProcessor<MainActions.LoadPreviousScore, MainResults, Pair<Int, Int>>(
        action = {
            Observable.combineLatest(scoreRepository.getTeamAScore(), scoreRepository.getTeamBScore(), BiFunction<Int, Int, Pair<Int, Int>> { teamA, teamB ->
                teamA to teamB
            })
        },
        success = { MainResults.LoadPreviousScoreSuccess(it.first, it.second) }
        , error = { MainResults.LoadPreviousScoreError(it) },
        loading = MainResults.LoadPreviousScoreLoading()
)

val reducer = KontentReducer<MainResults, MainViewState>({ result, previousState ->
    when (result) {
        is MainResults.IncrementTeamA -> previousState.copy(teamAScore = previousState.teamAScore + 1)
        is MainResults.IncrementTeamB -> previousState.copy(teamBScore = previousState.teamBScore + 1)
        is MainResults.LoadPreviousScoreLoading -> previousState.copy(loading = true, error = null)
        is MainResults.LoadPreviousScoreError -> previousState.copy(loading = false, error = result.error)
        is MainResults.LoadPreviousScoreSuccess -> previousState.copy(loading = false, error = null, teamAScore = result.teamAScore, teamBScore = result.teamBScore)
    }
})

sealed class MainActions : KontentAction() {
    class LoadPreviousScore : MainActions()
    class IncrementTeamA : MainActions()
    class IncrementTeamB : MainActions()
}

sealed class MainResults : KontentResult() {
    class LoadPreviousScoreLoading : MainResults()
    data class LoadPreviousScoreError(val error: Throwable) : MainResults()
    data class LoadPreviousScoreSuccess(val teamAScore: Int, val teamBScore: Int) : MainResults()
    class IncrementTeamA : MainResults()
    class IncrementTeamB : MainResults()
}

data class MainViewState(val loading: Boolean = false, val error: Throwable? = null, val teamAScore: Int = 0, val teamBScore: Int = 0) : KontentViewState()
