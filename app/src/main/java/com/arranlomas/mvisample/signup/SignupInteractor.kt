package com.arranlomas.mvisample.signup

import com.arranlomas.kontent.commons.KontentInteractor
import com.arranlomas.mvisample.repository.IAuthRepository
import com.arranlomas.mvisample.signup.functions.signupActionProcessor
import com.arranlomas.mvisample.signup.functions.signupIntentToAction
import com.arranlomas.mvisample.signup.functions.signupReducer
import com.arranlomas.mvisample.signup.objects.SignupIntent
import com.arranlomas.mvisample.signup.objects.SignupViewState

/**
 * Created by arran on 5/12/2017.
 */
internal class SignupInteractor(val authRepository: IAuthRepository) : SignupContract.Interactor, KontentInteractor<SignupViewState, SignupIntent>() {
    init {
        super.processor = { intents ->
            intents
                    .map { intent -> signupIntentToAction(intent) }
                    .compose(signupActionProcessor(authRepository))
                    .scan(SignupViewState.default(), signupReducer)
        }
    }
}
