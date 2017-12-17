package com.arranlomas.mvisample.signup.functions

import android.util.Patterns.EMAIL_ADDRESS
import com.arranlomas.mvisample.R
import io.reactivex.Observable

/**
 * Created by arran on 6/12/2017.
 */
internal fun String.isValidEmail(): Boolean {
    if (this.isEmpty()) {
        return false
    } else {
        return EMAIL_ADDRESS.matcher(this).matches()
    }
}


internal data class SignupFieldValidator(
        val emailError: Int? = null,
        val passwordError: Int? = null,
        val usernameError: Int? = null
) {
    fun isOk(): Boolean = emailError == null && passwordError == null && usernameError == null
}

internal fun validateFields(email: String,
                            username: String,
                            password: String): Observable<SignupFieldValidator> {
    val emailError = if (email.isValidEmail()) null else R.string.signup_invalid_email
    val passwordError = if (password.isEmpty()) R.string.signup_invalid_password else null
    val usernameError = if (username.isEmpty()) R.string.signup_invalid_username else null
    return Observable.just(SignupFieldValidator(emailError, passwordError, usernameError))
}
