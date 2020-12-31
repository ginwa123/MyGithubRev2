package com.m.ginwa.core.utils

object DataMapper {
    fun <T, I> mapping(input: List<T>, change: (T) -> I): List<I> = input.map { change.invoke(it) }
}