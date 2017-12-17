package com.arranlomas.mvisample.repository

import io.reactivex.Observable

/**
 * Created by arran on 6/12/2017.
 */
internal class AuthRepository(private val authApi: IAuthApi) : IAuthRepository {

    override fun signup(email: String, username: String, password: String): Observable<AuthResponse> {
        return authApi.signup(email, username, password)
    }
}