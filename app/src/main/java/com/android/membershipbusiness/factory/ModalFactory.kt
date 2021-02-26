package com.android.membershipbusiness.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.membershipbusiness.repo.AuthRepository
import com.android.membershipbusiness.repo.BaseRepo
import com.android.membershipbusiness.repo.MainRepository
import com.android.membershipbusiness.viewModels.AuthViewModel
import com.android.membershipbusiness.viewModels.MainViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ModalFactory(private val repository: BaseRepo) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository as MainRepository) as T
            }
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository as AuthRepository) as T
            }
            else -> {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}