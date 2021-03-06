package com.android.membershipbusiness.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.membershipbusiness.repo.MainRepository

class MainViewModel(val repository: MainRepository) :ViewModel(){

    fun checkUserHasData(): LiveData<String>
    {
        return repository.checkUserHasData()
    }

    fun sendUsertoAddOwnerDataActivity() {
        repository.sendUserToAddUserData()
    }

    fun sendtoAddBusinessDataActivity() {
        repository.sendUserToAddUserBusinessData()
    }
    fun checkUserBusinessData() :LiveData<String> {
        return repository.checkBusinessData()
    }

    fun checkUserApplicationStatus(): LiveData<String> {
        return  repository.checkApplicationStatus()
    }

}