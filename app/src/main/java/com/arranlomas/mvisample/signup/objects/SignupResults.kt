package com.arranlomas.mvisample.signup.objects

/**
 * Created by arran on 5/12/2017.
 */
internal sealed class SignupResults {
    data class SignupCtaSucess(val id: Long, val token: String) : SignupResults()
    object SignupCtaInFlight : SignupResults()
    data class SignupCtaError(
            val emailError: Int? = null,
            val usernameError: Int? = null,
            val passwordError: Int? = null,
            val networkError: Throwable? = null) : SignupResults()
}