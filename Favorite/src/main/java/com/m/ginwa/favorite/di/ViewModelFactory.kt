@file:Suppress("unused")

package com.m.ginwa.favorite.di

import androidx.lifecycle.ViewModelProvider
import com.m.ginwa.core.domain.usecase.GithubUseCase
import javax.inject.Inject


class ViewModelFactory @Inject constructor(private val useCase: GithubUseCase) :
    ViewModelProvider.NewInstanceFactory()