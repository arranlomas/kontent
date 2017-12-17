package com.arranlomas.mvisample.signup.functions

import com.arranlomas.kontent.extensions.composeIo
import com.arranlomas.mvisample.repository.IAuthRepository
import com.arranlomas.mvisample.signup.objects.SignupActions
import com.arranlomas.mvisample.signup.objects.SignupResults
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/**
 * Created by arran on 5/12/2017.
 */
internal fun signupActionProcessor(authRepository: IAuthRepository): ObservableTransformer<SignupActions, SignupResults> =
        ObservableTransformer { action: Observable<SignupActions> ->
            action.publish { shared ->
                shared.ofType(SignupActions.Signup::class.java).compose(signupCtaProcessor(authRepository))
            }
        }


private fun signupCtaProcessor(authRepository: IAuthRepository): ObservableTransformer<SignupActions.Signup, SignupResults> = ObservableTransformer<SignupActions.Signup, SignupResults> { action: Observable<SignupActions.Signup> ->
    action.switchMap { (email, username, password) ->
        validateFields(email, username, password)
                .flatMap { validator ->
                    if (validator.isOk()) return@flatMap authRepository.signup(email, username, password)
                            .map { SignupResults.SignupCtaSucess(it.userId, it.token) as SignupResults }
                            .onErrorReturn { SignupResults.SignupCtaError(networkError = it) }
                    else return@flatMap Observable.just(SignupResults.SignupCtaError(emailError = validator.emailError, usernameError = validator.usernameError, passwordError = validator.passwordError))
                }
                .onErrorResumeNext(Observable.empty())
                .onErrorReturn { SignupResults.SignupCtaError(networkError = it) }
                .composeIo()
                .startWith(SignupResults.SignupCtaInFlight)
    }
}