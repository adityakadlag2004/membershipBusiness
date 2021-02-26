package com.android.membershipbusiness.di

import com.android.membershipbusiness.factory.ModalFactory
import com.android.mvvmdatabind2.di.modules.FactoryModule
import com.android.mvvmdatabind2.di.modules.RepositoryModule
import dagger.Component

@Component(modules = [RepositoryModule::class,FactoryModule::class])
interface FactoryComponent {

    fun getFactory(): ModalFactory
}