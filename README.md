# Kontent

This small library is created to help reduce the boilerplate involved in using Model-View-Intent

This library of base components takes inspiration from [Benoît Quenaudon’s excelent talk at Droidcon NYC 2017](https://medium.com/r/?url=https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DPXBXcHQeDLE) and the [Hannes Dorfmann’s series](https://medium.com/r/?url=http%3A%2F%2Fhannesdorfmann.com%2Fandroid%2Fmosby3-mvi-1).

### Example and Usage
(Small example)[https://github.com/arranlomas/kontent/tree/e51aa745e94c3bb93442b8eef348bd01a2635bc6/app/src/main/java/com/arranlomas/mvisample] - Most basic single page score counting application (more details about how it works in (here)[https://github.com/arranlomas/kontent#usage]

(Large(ish) example)[https://github.com/arranlomas/Trickl] - Multipage application with many dependencies published to the appstore.

### What this library gives you
* Simplicity
* Confirguration changes are handled
* Extremely easy to test
* Increased seperation of concerns
* Increase code reusability
* Explicit state
* Code that is easy to reaad


## What is MVI

This little readme isn't enough to cover what mvi is. But in a nutshell it is a pattern that helps create a reactive functional architecture. There is lots of great articles but I recomend starting [here](https://proandroiddev.com/the-contract-of-the-model-view-intent-architecture-777f95706c1e)

![](https://cdn-images-1.medium.com/max/800/1*WIeFDslCFadszobitk37Cw.png)

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
        initialIntentPredicate = { intent -> intent is MainIntent.LoadPreviousScore }
)
```

### Reducer
```
val reducer = KontentReducer<MainResults, MainViewState>({ result, previousState ->
    when (result) {
        is MainResults.IncrementTeamA -> previousState.copy(teamAScore = previousState.teamAScore + 1)
        is MainResults.IncrementTeamB -> previousState.copy(teamBScore = previousState.teamBScore + 1)
        is MainResults.LoadPreviousScoreLoading -> previousState.copy(loading = true, error = null)
        is MainResults.LoadPreviousScoreError -> previousState.copy(loading = false, error = result.error)
        is MainResults.LoadPreviousScoreSuccess -> previousState.copy(loading = false, error = null, teamAScore = result.teamAScore, teamBScore = result.teamBScore)
    }
})
```

### Other compoments
[Action Processor](https://github.com/arranlomas/kontent/blob/da23563095eca746798e60bcbf8f707bc8abbe0a/app/src/main/java/com/arranlomas/mvisample/MainActivity.kt#L65)

[Load previous score](https://github.com/arranlomas/kontent/blob/da23563095eca746798e60bcbf8f707bc8abbe0a/app/src/main/java/com/arranlomas/mvisample/MainActivity.kt#L76)

[Fake repository](https://github.com/arranlomas/kontent/blob/da23563095eca746798e60bcbf8f707bc8abbe0a/app/src/main/java/com/arranlomas/mvisample/ScoreRepository.kt#L13)

## Things of note here

#### Dagger
ViewModelFactory is injected into view, the viewmodel is then initialized from that factory (this is so they can be injected with dependencies, you can use default factory if no the ViewModel has no dependencies) Check [here](https://github.com/arranlomas/DaggerViewModelHelper) for more info.

#### Intents
when you call super.attachIntents(intent()) this is providing the list of actions you want to perform. Do start sending intents to the view model we cal super.attachIntent(intents()) (this must be called after super.setup(viewModel) so that the view model is attached to the view)

#### Intent to Action (Intent Interpretor in the diagram above)
this is a function that converts the intents to actions, this adds a layer of abstraction so the ViewModel can be reused if nescessary

#### Action Processor (Processor in the diagram above)
this is a function that processes the actions and does something, this could be going off to make a network reuqest or performing some kind of validation, this then produces a result

#### Reducer 
this is a function that takes the result, combines it with a stored previous state and emits a new view state

#### Default State
this is the starting state of the view, usually not loading, no models and no errors etc.

#### initial intent predicate
This is a function that checks to see if the intent was the initial intent sent. Not all views with have an initial intent. But most pages that load data on opening will have an Initial Intent of some kind (see initialIntent() function in the example above)

#### Render
The view has one overriden function that is render. This takes a view state and works out how to presenter it to the user.

### Base Classes
[AndroidVideModel](https://github.com/arranlomas/kontent/blob/da23563095eca746798e60bcbf8f707bc8abbe0a/kontent-android-viewmodel/src/main/java/com/arranlomas/kontent_android_viewmodel/commons/objects/KontentAndroidViewModel.kt#L13)

[ViwModel](https://github.com/arranlomas/kontent/blob/da23563095eca746798e60bcbf8f707bc8abbe0a/kontent-core/src/main/java/com/arranlomas/kontent/commons/objects/KontentViewModel.kt#L8)

[Base ActionProcessor](https://github.com/arranlomas/kontent/blob/da23563095eca746798e60bcbf8f707bc8abbe0a/kontent-core/src/main/java/com/arranlomas/kontent/commons/functions/ActionProcessors.kt#L9)

[Master ActionProcess](https://github.com/arranlomas/kontent/blob/master/kontent-core/src/main/java/com/arranlomas/kontent/commons/functions/MasterActionProcessors.kt) - Used to map actions to the action processor for the type of action.

[Reducer](https://github.com/arranlomas/kontent/blob/da23563095eca746798e60bcbf8f707bc8abbe0a/kontent-core/src/main/java/com/arranlomas/kontent/commons/functions/Reducer.kt#L7)

[Activity](https://github.com/arranlomas/kontent/blob/da23563095eca746798e60bcbf8f707bc8abbe0a/kontent-core/src/main/java/com/arranlomas/kontent/commons/objects/KontentActivity.kt#L7)

[DaggerActivity](https://github.com/arranlomas/kontent/blob/da23563095eca746798e60bcbf8f707bc8abbe0a/kontent-dagger/src/main/java/com/arranlomas/daggerkontent/KontentDaggerActivity.kt#L15)

[DaggerSupportActivity](https://github.com/arranlomas/kontent/blob/da23563095eca746798e60bcbf8f707bc8abbe0a/kontent-dagger-support/src/main/java/com/arranlomas/kotentdaggersupport/KontentDaggerSupportActivity.kt#L14)

[Fragment](https://github.com/arranlomas/kontent/blob/da23563095eca746798e60bcbf8f707bc8abbe0a/kontent-core/src/main/java/com/arranlomas/kontent/commons/objects/KontentFragment.kt#L7)

[DaggerFragment](https://github.com/arranlomas/kontent/blob/da23563095eca746798e60bcbf8f707bc8abbe0a/kontent-dagger/src/main/java/com/arranlomas/daggerkontent/KontentDaggerFragment.kt#L9)

[DaggerSupportFragment](https://github.com/arranlomas/kontent/blob/da23563095eca746798e60bcbf8f707bc8abbe0a/kontent-dagger-support/src/main/java/com/arranlomas/kotentdaggersupport/KontentDaggerSupportFragment.kt#L11)


## Fair Warning
This library is heavily dependant on the RxJava 2, Kotlin and Android's ViewModel.

This library uses inheritence, I would suggest adding an extra abstraction between `kontent` classes and your implementations with something like a `BaseActivity` that extends `KontentActivty`.

