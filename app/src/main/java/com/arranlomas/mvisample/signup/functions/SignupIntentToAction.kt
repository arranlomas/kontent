package com.arranlomas.mvisample.signup.functions

import com.arranlomas.mvisample.signup.objects.SignupActions
import com.arranlomas.mvisample.signup.objects.SignupIntent

/**
 * Created by arran on 5/12/2017.
 */

internal fun signupIntentToAction(intent: SignupIntent): SignupActions = when (intent) {
    is SignupIntent.SignupCta -> SignupActions.Signup(intent.email, intent.username, intent.password)
}