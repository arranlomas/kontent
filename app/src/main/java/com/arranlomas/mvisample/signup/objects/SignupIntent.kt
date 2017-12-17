package com.arranlomas.mvisample.signup.objects

import com.arranlomas.kontent.commons.KontentContract

/**
 * Created by arran on 5/12/2017.
 */
internal sealed class SignupIntent : KontentContract.Intent {
    data class SignupCta(val email: String, val username: String, val password: String) : SignupIntent()
}
