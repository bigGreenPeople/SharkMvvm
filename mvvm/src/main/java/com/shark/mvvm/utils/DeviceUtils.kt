package com.shark.mvvm.utils

import android.content.Context
import android.content.res.Configuration

fun isTablet(context: Context): Boolean {
    return context.resources
        .configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
}