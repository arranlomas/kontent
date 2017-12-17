package com.arranlomas.mvisample.signup.functions

import com.arranlomas.mvisample.signup.objects.SignupResults
import com.arranlomas.mvisample.signup.objects.SignupViewState
import io.reactivex.functions.BiFunction

/**
 * Created by arran on 5/12/2017.
 */
internal val signupReducer = BiFunction <SignupViewState, SignupResults, SignupViewState> {
    previousState: SignupViewState, result: SignupResults ->
    when (result) {
        is SignupResults.SignupCtaSucess -> previousState.copy(loading = false, emailError = null, passwordError = null, usernameError = null, networkError = null, isLoggedIn = true)
        SignupResults.SignupCtaInFlight -> previousState.copy(loading = true)
        is SignupResults.SignupCtaError -> previousState.copy(loading = false,
                emailError = result.emailError,
                passwordError = result.passwordError,
                usernameError = result.usernameError,
                networkError = result.networkError)
    }
}