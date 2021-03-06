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
    var username2Base = MutableLiveData<String>()
    var emailBase = MutableLiveData<String>()
    var profileImageBase = MutableLiveData<String>()
    var applicationStatus = MutableLiveData<String>()
    var phone2 = MutableLiveData<String>()
    var count2 = MutableLiveData<String>()
    var curUser = mAuthBase.currentUser


    fun sendUserToMainActivity() {
        Intent(context, MainActivity::class.java).also {
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(it)
        }
    }

    fun checkUserHasData(): MutableLiveData<String> {
        val user = mAuthBase.currentUser
        if (user != null) {
            myRefBase.child(user.uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(Constants.USER_NAME) && snapshot.hasChild(Constants.USER_PHONENUMBER)) {
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
                    if (snapshot.hasChild(Constants.BUSINESS_NAME) && snapshot.hasChild(Constants.BUSINESS_OWNER)) {
                        bdataCheck.value = "yes"
                    } else {
                        bdataCheck.value = "no"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(ContentValues.TAG, "onCancelled: ${error.message}")
                }
            })
        }
        return bdataCheck
    }

    fun getUsername(): MutableLiveData<String> {
        val user = mAuthBase.currentUser
        if (user != null && username2Base.value.isNullOrEmpty()) {
            myRefBase.child(mAuthBase.currentUser!!.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            username2Base.value =
                                snapshot.child(Constants.USER_NAME).value.toString()

                            Log.d(ContentValues.TAG, "onDataChange: Repo$username2Base")
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(ContentValues.TAG, "onCancelled: Fail")
                    }
                })
        }

        Log.d(ContentValues.TAG, "onDataChange: Last Repo$username2Base ")
        return username2Base
    }


    fun getEmail(): MutableLiveData<String> {
        val user = mAuthBase.currentUser
        if (user != null && username2Base.value.isNullOrEmpty()) {
            myRefBase.child(mAuthBase.currentUser!!.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            emailBase.value =
                                snapshot.child(Constants.USER_EMAIL).value.toString()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(ContentValues.TAG, "onCancelled: Fail")
                    }
                })
        }

        Log.d(ContentValues.TAG, "onDataChange: Last Repo$emailBase ")
        return emailBase
    }

    fun getImage(): MutableLiveData<String> {
        val user = mAuthBase.currentUser
        if (user != null && username2Base.value.isNullOrEmpty()) {
            myRefBase.child(mAuthBase.currentUser!!.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            profileImageBase.value =
                                snapshot.child(Constants.USER_PROFILE_IMAGE).value.toString()
                            Log.d(ContentValues.TAG, "onDataChange: Repo$username2Base")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(ContentValues.TAG, "onCancelled: Fail")
                    }
                })
        }

        Log.d(ContentValues.TAG, "onDataChange: Last Repo$username2Base ")
        return profileImageBase
    }

    fun checkApplicationStatus(): LiveData<String> {
        return applicationStatus
    }
}