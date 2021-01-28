package com.m.ginwa.core.utils

import java.util.*

enum class DisplayTheme {
    SYSTEM,
    DARK,
    LIGHT;

    override fun toString(): String {
        return name[0].toString() + name.substring(1).toLowerCase(Locale.ROOT)
    }
}