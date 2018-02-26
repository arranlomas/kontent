package com.arranlomas.mvisample

import com.arranlomas.kontent.extensions.composeIo
import io.reactivex.Observable
import java.util.*
import java.util.concurrent.TimeUnit

interface IScoreRepository {
    fun getTeamAScore(): Observable<Int>
    fun getTeamBScore(): Observable<Int>
}

class ScoreRepository : IScoreRepository {
    private fun getRandomNumber(): Observable<Int> = Observable.just(Random().nextInt(20))
            .delay(2, TimeUnit.SECONDS)
            .composeIo()

    override fun getTeamAScore(): Observable<Int> = getRandomNumber()

    override fun getTeamBScore(): Observable<Int> = getRandomNumber()

}