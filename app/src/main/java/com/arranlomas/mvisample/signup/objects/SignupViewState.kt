package com.arranlomas.mvisample.signup.objects

import com.arranlomas.kontent.commons.KontentContract

/**
 * Created by arran on 5/12/2017.
 */
internal data class SignupViewState(
        val emailError: Int? = null,
        val usernameError: Int? = null,
        val passwordError: Int? = null,
        val networkError: Throwable? = null,
        val loading: Boolean = false,
        val isLoggedIn: Boolean = false
) : KontentContract.ViewState {
    companion object Factory {
        fun default(): SignupViewState {
            return SignupViewState()
        }
    }
}