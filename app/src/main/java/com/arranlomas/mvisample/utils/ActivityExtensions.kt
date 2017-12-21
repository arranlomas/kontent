package com.arranlomas.mvisample.utils

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Created by arran on 21/12/2017.
 */
fun AppCompatActivity.showFragment(fragment: Fragment, fragmentContainer: Int) {
    val transaction = this.supportFragmentManager.beginTransaction()
    transaction.replace(fragmentContainer, fragment)
    transaction.addToBackStack(null)
    transaction.commit()
}