package com.android.membershipbusiness.viewModels

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.membershipbusiness.repo.UserRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class UserDataViewModel(val repository: UserRepo) : ViewModel() {


    fun uploadToFirebase(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.uploadToFirebase(uri)
        }
    }

    fun sendUserToMainActivity() {
        repository.sendUserToMainActivity()
    }

    fun checkStage(): LiveData<String> {
        return repository.checkstage()
    }

//    fun updateUser(username:String?=null,contactnumber:String?=null)
//    {
//        repository.updateUser(username,contactnumber)
//    }


    fun checkUserBusinessData(): LiveData<String> {

        return repository.checkBusinessData()
    }

    fun sendtoAddBusinessDataActivity() {
        repository.sendUserToAddUserBusinessData()
    }

    fun uploadToFirebaseBusiness(imageUri: Uri) {
        repository.uploadToFirebaseBusiness(imageUri)
    }

    fun uploadPanCard(imageUri: Uri) {
        repository.uploadPan(imageUri)
    }

    fun uploadAddharCard(imageUri: Uri) {
        repository.uploadAddhar(imageUri)
    }


    fun uploadBusinessImages(imageList: ArrayList<Uri>) {
        repository.uploadBusinessImages(imageList)
    }

    fun getBusinessLogo(): MutableLiveData<String> {
        return repository.getBusinessLogo()
    }

    fun getBusinessName(): MutableLiveData<String> {
        return repository.getBusinessName()
    }

    fun getMemberShipCount(): MutableLiveData<String> {
        return repository.getMemberShipCount()
    }

    fun getTotalCustomers(): MutableLiveData<String> {
        return repository.getTotalCustomers()
    }

    fun getOwner(): MutableLiveData<String> {
        return repository.getOwnerBase()
    }

    fun getBusinessEmail(): MutableLiveData<String> {
        return repository.getBusinessEmailBase()
    }

    fun getContactNumber(): MutableLiveData<String> {
        return repository.getContactBase()
    }

    fun getAddress(): MutableLiveData<String> {
        return repository.getAddressBase()
    }

    fun addMembership(
        title: String,
        category: String? = null,
        desc: String,
        capacity: String,
        stTime: String,
        edTime: String,
        fees: String
    ) {

        repository.addMemberShip(title,category,desc,capacity,stTime,edTime,fees)

    }

}
