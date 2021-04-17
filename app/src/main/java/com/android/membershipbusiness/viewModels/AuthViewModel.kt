package com.android.membershipbusiness.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.membershipbusiness.repo.AuthRepository
import com.android.membershipbusiness.repo.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(val repository: AuthRepository) :ViewModel() {

    val email: MutableLiveData<String> = MutableLiveData()


    val password: MutableLiveData<String> = MutableLiveData()

    fun login(email:String,password:String) {
        repository.login(email, password)
    }

    fun register(email:String,password:String) {
        repository.register(email, password)
    }

    fun forgotPassword() {
        repository.sendUserToForgotPassWordActivity()
    }

    fun sendUsertoForgotPassAct() {
        repository.sendUserToForgotPassWordActivity()
    }
}
