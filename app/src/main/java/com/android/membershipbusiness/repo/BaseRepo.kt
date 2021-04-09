package com.android.membershipbusiness.repo

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.membershipbusiness.activities.MainActivity
import com.android.membershipbusiness.activities.data.AddBusinessData
import com.android.membershipbusiness.activities.data.AddOwnerData
import com.android.membershipbusiness.auth.ForgotPasswordActivity
import com.android.membershipbusiness.auth.LoginActivity
import com.android.membershipbusiness.other.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

abstract class BaseRepo(val context: Context) {
    private var mAuthBase = FirebaseAuth.getInstance()
    var databaseBase = FirebaseDatabase.getInstance()
    var myRefBase = databaseBase.getReference(Constants.USERS)
    var userdataBase = MutableLiveData<String>()
    var bdataCheck = MutableLiveData<String>()
    var businessName = MutableLiveData<String>()
    var owner = MutableLiveData<String>()
    var profileImageBase = MutableLiveData<String>()
    var applicationStatus = MutableLiveData<String>()
    var membershipCount = MutableLiveData<String>()
    var totalCustomer = MutableLiveData<String>()
    var businessEmail = MutableLiveData<String>()
    var stage = MutableLiveData<String>()
    var address = MutableLiveData<String>()
    var userverified = MutableLiveData<String>()
    var contactNumber = MutableLiveData<String>()

    var curUser = mAuthBase.currentUser


    fun sendUserToMainActivity() {
        Intent(context, MainActivity::class.java).also {
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(it)
        }
    }


    fun checkUserHasDataPersonalData(): MutableLiveData<String> {
        val user = mAuthBase.currentUser
        if (user != null) {
            myRefBase.child(user.uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(Constants.PERSONAL_DETAILS)) {
                        userdataBase.value = "yes"
                    } else {
                        userdataBase.value = "no"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(ContentValues.TAG, "onCancelled: ${error.message}")
                }
            })
        }
        return userdataBase
    }

    fun sendUserToAddUserData() {
        Intent(context, AddOwnerData::class.java).also {
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(it)

        }
    }

    fun sendUserToAddUserBusinessData() {
        Intent(context, AddBusinessData::class.java).also {
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(it)

        }
    }

    fun checkBusinessData(): MutableLiveData<String> {

        val user = mAuthBase.currentUser

        if (user != null) {
            myRefBase.child(user.uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    bdataCheck.value = snapshot.child(Constants.DATAADDED).value.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(ContentValues.TAG, "onCancelled: ${error.message}")
                }
            })
        }
        return bdataCheck
    }

    fun checkstage(): MutableLiveData<String> {

        val user = mAuthBase.currentUser

        if (user != null) {
            myRefBase.child(user.uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    stage.value = snapshot.child(Constants.STAGE).value.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(ContentValues.TAG, "onCancelled: ${error.message}")
                }
            })
        }
        return stage
    }


    fun getOwnerBase(): MutableLiveData<String> {
        val user = mAuthBase.currentUser
        if (user != null && totalCustomer.value.isNullOrEmpty()) {
            myRefBase.child(mAuthBase.currentUser!!.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            owner.value =
                                snapshot.child(Constants.BUSINESS_OWNER).value.toString()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(ContentValues.TAG, "onCancelled: Fail")
                    }
                })
        }

        Log.d(ContentValues.TAG, "onDataChange: Last Repo$owner ")
        return owner
    }

    fun getBusinessLogo(): MutableLiveData<String> {
        val user = mAuthBase.currentUser
        if (user != null && businessName.value.isNullOrEmpty()) {
            myRefBase.child(mAuthBase.currentUser!!.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            profileImageBase.value =
                                snapshot.child(Constants.BUSiNESS_LOGO).value.toString()
                            Log.d(ContentValues.TAG, "onDataChange: Repo$businessName")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(ContentValues.TAG, "onCancelled: Fail")
                    }
                })
        }

        Log.d(ContentValues.TAG, "onDataChange: Last Repo$businessName ")
        return profileImageBase
    }

    fun checkApplicationStatus(): LiveData<String> {
        return applicationStatus
    }

    @JvmName("getBusinessName1")
    fun getBusinessName(): MutableLiveData<String> {
        val user = mAuthBase.currentUser
        if (user != null && businessName.value.isNullOrEmpty()) {
            myRefBase.child(mAuthBase.currentUser!!.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            businessName.value =
                                snapshot.child(Constants.BUSINESS_NAME).value.toString()

                            Log.d(ContentValues.TAG, "onDataChange: Repo$businessName")
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(ContentValues.TAG, "onCancelled: Fail")
                    }
                })
        }

        Log.d(ContentValues.TAG, "onDataChange: Last Repo$businessName ")
        return businessName
    }

    fun getMemberShipCount(): MutableLiveData<String> {
        val user = mAuthBase.currentUser
        if (user != null && membershipCount.value.isNullOrEmpty()) {
            myRefBase.child(mAuthBase.currentUser!!.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            membershipCount.value =
                                snapshot.child(Constants.BUSINESS_DETAILS).child(Constants.MEMBERSHIP_COUNT).value.toString()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(ContentValues.TAG, "onCancelled: Fail")
                    }
                })
        }

        Log.d(ContentValues.TAG, "onDataChange: Last Repo$membershipCount ")
        return membershipCount

    }

    fun getTotalCustomers(): MutableLiveData<String> {
        val user = mAuthBase.currentUser
        if (user != null && totalCustomer.value.isNullOrEmpty()) {
            myRefBase.child(mAuthBase.currentUser!!.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            totalCustomer.value =
                                snapshot.child(Constants.BUSINESS_CUSTOMERS_COUNT).value.toString()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(ContentValues.TAG, "onCancelled: Fail")
                    }
                })
        }

        Log.d(ContentValues.TAG, "onDataChange: Last Repo$totalCustomer ")
        return totalCustomer
    }

    fun getBusinessEmailBase(): MutableLiveData<String> {
        val user = mAuthBase.currentUser
        if (user != null && totalCustomer.value.isNullOrEmpty()) {
            myRefBase.child(mAuthBase.currentUser!!.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            businessEmail.value =
                                snapshot.child(Constants.BUSINESS_EMAIL).value.toString()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(ContentValues.TAG, "onCancelled: Fail")
                    }
                })
        }

        Log.d(ContentValues.TAG, "onDataChange: Last Repo$businessEmail ")
        return businessEmail
    }

    fun getContactBase(): MutableLiveData<String> {
        val user = mAuthBase.currentUser
        if (user != null && totalCustomer.value.isNullOrEmpty()) {
            myRefBase.child(mAuthBase.currentUser!!.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            contactNumber.value =
                                snapshot.child(Constants.USER_PHONENUMBER).value.toString()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(ContentValues.TAG, "onCancelled: Fail")
                    }
                })
        }

        Log.d(ContentValues.TAG, "onDataChange: Last Repo$contactNumber ")
        return contactNumber
    }

    fun getAddressBase(): MutableLiveData<String> {
        val user = mAuthBase.currentUser
        if (user != null && totalCustomer.value.isNullOrEmpty()) {
            myRefBase.child(mAuthBase.currentUser!!.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            address.value =
                                snapshot.child(Constants.BUSINESS_ADDRESS).value.toString()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(ContentValues.TAG, "onCancelled: Fail")
                    }
                })
        }

        Log.d(ContentValues.TAG, "onDataChange: Last Repo$address ")
        return address
    }

    fun sendUserToLoginActivity() {
        Intent(context, LoginActivity::class.java).also {
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(it)
        }
    }

    fun sendUserToForgotPassWordActivity(i:Int?=null) {
        Intent(context, ForgotPasswordActivity::class.java).also {
            it.putExtra("email",i?:0)
            context.startActivity(it)
        }
    }

    fun isUserEmailVerified(): LiveData<String> {
        val user = mAuthBase.currentUser
        if (user!=null)
        {
          if (user.isEmailVerified)
          {
             userverified.value="yes"
          }
          else
          {
           userverified.value="no"
          }
        }
        return userverified
    }
}