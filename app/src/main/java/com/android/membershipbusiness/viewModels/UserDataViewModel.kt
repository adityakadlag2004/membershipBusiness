package com.android.membershipbusiness.viewModels

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.membershipbusiness.repo.UserRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserDataViewModel(val repository: UserRepo) : ViewModel() {


    fun uploadToFirebase(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.uploadToFirebase(uri)
        }
    }

    fun sendUserToMainActivity() {
        repository.sendUserToMainActivity()
    }

    fun checkStage():LiveData<String>
    {
       return repository.checkstage()
    }

//    fun updateUser(username:String?=null,contactnumber:String?=null)
//    {
//        repository.updateUser(username,contactnumber)
//    }

    fun getUsername(): LiveData<String> {
        val _username = repository.getUsername()
        Log.d(ContentValues.TAG, "onDataChange: viewmodel ${_username.value} ")
        return _username
    }

    fun getImage(): LiveData<String> {
        return repository.getImage()
    }

    fun checkUserBusinessData(): LiveData<String> {

        return repository.checkBusinessData()
    }

    fun sendtoAddBusinessDataActivity() {
        repository.sendUserToAddUserBusinessData()
    }

    fun uploadToFirebaseBusiness(imageUri: Uri) {
        repository.uploadToFirebaseBusiness(imageUri)
    }

}
