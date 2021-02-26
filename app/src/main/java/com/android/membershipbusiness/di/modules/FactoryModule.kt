package com.android.mvvmdatabind2.di.modules


import com.android.membershipbusiness.factory.ModalFactory
import com.android.membershipbusiness.repo.BaseRepo
import dagger.Module
import dagger.Provides

@Module
class FactoryModule constructor(var baseRepo: BaseRepo) {

    @Provides
    fun provideModalFactory(): ModalFactory {
        return ModalFactory(baseRepo)
    }

    @Provides
    fun providesRepository(): BaseRepo {
        return baseRepo
    }

}