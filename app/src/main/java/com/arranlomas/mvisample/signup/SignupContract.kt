package com.arranlomas.mvisample.signup

import com.arranlomas.kontent.commons.KontentContract
import com.arranlomas.mvisample.signup.objects.SignupIntent
import com.arranlomas.mvisample.signup.objects.SignupViewState

/**
 * Created by arran on 5/12/2017.
 */
internal interface SignupContract {
    interface Interactor : KontentContract.Interactor<SignupViewState, SignupIntent>
}