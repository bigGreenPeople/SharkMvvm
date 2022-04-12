package com.shark.mvvm.utils

import java.util.*

object StringUtils {
    fun underscoreName(name: String): String {
        if (name.isEmpty()) return ""
        return name.replace("([a-z]|[0-9])([A-Z])".toRegex(), "$1_$2").lowercase(Locale.getDefault())
    }
}