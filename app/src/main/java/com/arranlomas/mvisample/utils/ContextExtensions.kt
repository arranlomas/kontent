package com.arranlomas.mvisample.utils

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup

inline fun Context.inflateLayout(@LayoutRes layoutResId: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false)
        = LayoutInflater.from(this).inflate(layoutResId, parent, attachToRoot)
