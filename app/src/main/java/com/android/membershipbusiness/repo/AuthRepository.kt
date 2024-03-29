package com.android.membershipbusiness.repo

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.android.membershipbusiness.activities.MainActivity
import com.android.membershipbusiness.auth.LoginActivity
import com.android.membershipbusiness.other.Constants
import com.android.membershipbusiness.other.Constants.USERS
import com.android.membershipbusiness.other.Constants.USER_EMAIL
import com.android.membershipbusiness.other.Constants.USER_ID
import com.android.membershipbusiness.other.Constants.USER_NAME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AuthRepository(context: Context) : BaseRepo(context) {
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference(USERS)
    private var mAuth = FirebaseAuth.getInstance()


    fun login(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            if (mAuth.currentUser!!.isEmailVerified) {
                                Toast.makeText(context, "Signed In as $email", Toast.LENGTH_SHORT)
                                    .show()
                                Intent(context, MainActivity::class.java).also {
                                    it.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    context.startActivity(it)
                                }
                            } else {

                                Toast.makeText(
                                    context,
                                    "First Verify Your Email",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                sendUserToMainActivity()
                            }
                        } else {
                            Log.d(ContentValues.TAG, "login: Login Failed :- ${task.exception}")
                        }
                    }
            } else {
                Toast.makeText(context, "Enter a valid Email Address", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Fill The Fields", Toast.LENGTH_SHORT).show()
        }

    }


    fun register(email: String, password: String) {

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener {
                        val user = mAuth.currentUser
                        if (user != null) {
                            myRef.child(user.uid).child(USER_EMAIL).setValue(email)
                            myRef.child(user.uid).child(USER_NAME).setValue(
                                user.displayName ?: " "
                            )
                            myRef.child(user.uid).child(USER_ID).setValue(user.uid)
                            myRef.child(user.uid).child(Constants.BUSINESS_DETAILS).child(
                                Constants.MEMBERSHIP_COUNT
                            )
                                .addValueEventListener(
                                    object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.exists()) {
                                                if (snapshot.value != null) {
                                                    Log.d(TAG, "onDataChange: has data")
                                                }
                                            } else {
                                                myRef.child(user.uid)
                                                    .child(Constants.BUSINESS_DETAILS).child(
                                                        Constants.MEMBERSHIP_COUNT
                                                    )
                                                    .setValue(0)
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Log.d(TAG, "onCancelled: cancel")
                                        }

                                    })
                        }

                        Intent(context, LoginActivity::class.java).also {
                            context.startActivity(it)
                        }
                    }

                } else {
                    Intent(context, LoginActivity::class.java).also {
                        context.startActivity(it)
                    }
                    Log.d(ContentValues.TAG, "login: Login Failed :- ${task.exception}")
                }
            }
    }

}


