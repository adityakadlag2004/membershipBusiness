package com.android.membershipbusiness.viewModels

import androidx.lifecycle.MutableLiveData
import com.android.membershipbusiness.repo.AuthRepository
import com.android.membershipbusiness.repo.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(val repository: AuthRepository) {

    val email: MutableLiveData<String> = MutableLiveData()


    val password: MutableLiveData<String> = MutableLiveData()

    fun login() = CoroutineScope(Dispatchers.IO).launch {
        repository.login(email.value.toString(), password.value.toString())
    }

    fun register() = CoroutineScope(Dispatchers.IO).launch {
        repository.register(email.value.toString(), password.value.toString())
    }

    fun forgotPassword(email: String) {
        repository.forgotPassword(email)
    }
}
