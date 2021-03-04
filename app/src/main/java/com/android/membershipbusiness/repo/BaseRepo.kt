package com.android.membershipbusiness.repo

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
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
    var username2Base = MutableLiveData<String>()
    var emailBase = MutableLiveData<String>()
    var profileImageBase = MutableLiveData<String>()
    var phone2 = MutableLiveData<String>()
    var userdataBase = MutableLiveData<String>()
    var count2 = MutableLiveData<String>()
    var curUser = mAuthBase.currentUser

    fun checkUserHasData(): MutableLiveData<String> {
        val user = mAuthBase.currentUser
        if (user != null) {
            myRefBase.child(user.uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(Constants.BUSINESS_NAME) && snapshot.hasChild(Constants.BUSINESS_OWNER)) {
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
}