# Kontent

This small library is created to help reduce the boilerplate involved in using Model-View-Intent

The inspiration for this library and the articles comes from this [Hannes Dorfmann’s series](https://medium.com/r/?url=http%3A%2F%2Fhannesdorfmann.com%2Fandroid%2Fmosby3-mvi-1), [Benoît Quenaudon’s talk at Droidcon NYC 2017](https://medium.com/r/?url=https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DPXBXcHQeDLE) and this excellent Article from [Benoît Quenaudon](https://proandroiddev.com/the-contract-of-the-model-view-intent-architecture-777f95706c1e)


## What is MVI

This little readme isn't enough to cover what mvi is. But in a nutshell it is a pattern that helps create a reactive functional architecture

![](https://cdn-images-1.medium.com/max/800/1*WIeFDslCFadszobitk37Cw.png)

## Overview

This library is heavily dependant on the RxJava 2, Kotlin, Android's ViewModel and Dagger 2


## Usage

A full example can be found in the app folder of this repository. Below is a simple example of the View and the ViewModel

### MainActivity
```
class MainActivity : KontentActivity<MainIntent, MainViewState>(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressDialog = ProgressDialog(this)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        super.setup(viewModel, { it.printStackTrace() })
        super.attachIntents(intents())
    }

    private fun intents() = Observable.merge(incrementTeamA(), incrementTeamB(), initialIntent())

    private fun initialIntent(): Observable<MainIntent> = Observable.just(MainIntent.LoadPreviousScore())

    private fun incrementTeamA(): Observable<MainIntent> = RxView.clicks(teamAButton)
            .map { MainIntent.IncrementTeamA() }

    private fun incrementTeamB(): Observable<MainIntent> = RxView.clicks(teamBButton)
            .map { MainIntent.IncrementTeamB() }

    override fun render(state: MainViewState) {
        teamAScore.text = state.teamAScore.toString()
        teamBScore.text = state.teamBScore.toString()

        if (state.loading) progressDialog.show()
        else progressDialog.hide()
    }
}
```

### ViewModel
```
class MainViewModel @Inject constructor(scoreRepository: IScoreRepository) : KontentAndroidViewModel<MainIntent, MainActions, MainResults, MainViewState>(
        intentToAction = { intent -> intentToAction(intent) },
        actionProcessor = actionProcessor(scoreRepository),
        reducer = reducer,
        defaultState = MainViewState(),
        initialIntentPredicate = { it is MainIntent.LoadPreviousScore }
)
```


