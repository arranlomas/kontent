package com.arranlomas.mvisample.signup.objects

/**
 * Created by arran on 5/12/2017.
 */
internal sealed class SignupActions {
    data class Signup(val email: String, val username: String, val password: String) : SignupActions()
}