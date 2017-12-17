package com.arranlomas.mvisample.repository

import io.reactivex.Observable

/**
 * Created by arran on 6/12/2017.
 */
internal interface IAuthApi {
    //This would typically be a retrofit implementation
    fun signup(email: String, username: String, password: String): Observable<AuthResponse>
}