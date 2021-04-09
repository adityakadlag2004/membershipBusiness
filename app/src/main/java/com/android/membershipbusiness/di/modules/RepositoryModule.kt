package com.android.mvvmdatabind2.di.modules

import android.content.Context
import com.android.membershipbusiness.repo.AuthRepository
import com.android.membershipbusiness.repo.BaseRepo
import com.android.membershipbusiness.repo.MainRepository

import dagger.Module
import dagger.Provides

@Module
class RepositoryModule constructor(var context: Context) {

    @Provides
    fun provideRepository(): BaseRepo {
        return AuthRepository(context = context)
    }

    @Provides
    fun provideMainRepository(): BaseRepo {
        return MainRepository(context)
    }



}