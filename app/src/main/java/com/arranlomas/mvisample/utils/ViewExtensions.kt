package com.arranlomas.mvisample.utils

import android.view.View

/**
 * Created by arran on 21/12/2017.
 */
fun View.setVisible(visible: Boolean) {
    if (visible) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}