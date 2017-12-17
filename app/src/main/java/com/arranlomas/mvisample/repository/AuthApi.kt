package com.arranlomas.mvisample.repository

import io.reactivex.Observable

/**
 * Created by arran on 17/12/2017.
 */
class AuthApi: IAuthApi{
    override fun signup(email: String, username: String, password: String): Observable<AuthResponse> {
        return Observable.just(AuthResponse(1, "token"))
    }

}