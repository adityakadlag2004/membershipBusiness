package com.android.membershipbusiness.repo

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import com.android.membershipbusiness.other.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.ArrayList

class UserRepo(val contextUser: Context) : BaseRepo(contextUser) {
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.getReference(Constants.USERS)
    private var mAuth = FirebaseAuth.getInstance()
    var valueUpdated=false
    private var storage = FirebaseStorage.getInstance()

    private var storageRef: StorageReference = storage.getReference(Constants.USERS)
    private var currentuser: FirebaseUser? = null
    fun uploadToFirebase(uri: Uri) {
        currentuser = mAuth.currentUser
        if (currentuser != null) {
            val fileReference: StorageReference = storageRef.child(currentuser!!.uid)
                .child(Constants.USER_PROFILE_IMAGE)

            fileReference.putFile(uri)
                .addOnSuccessListener {
                    fileReference.downloadUrl.addOnSuccessListener {
                        myRef.child(currentuser!!.uid).child(Constants.USER_PROFILE_IMAGE)
                            .setValue(it.toString())
                        sendUserToMainActivity()
                    }
                }
                .addOnFailureListener {
                    Log.d(
                        ContentValues.TAG,
                        "uploadToFirebase: Failed Uploading"
                    )
                }
        }
    }

    fun uploadToFirebaseBusiness(uri: Uri) {
        currentuser = mAuth.currentUser
        if (currentuser != null) {
            val fileReference: StorageReference = storageRef.child(currentuser!!.uid)
                .child(Constants.BUSiNESS_LOGO)

            fileReference.putFile(uri)
                .addOnSuccessListener {
                    fileReference.downloadUrl.addOnSuccessListener {
                        myRef.child(currentuser!!.uid).child(Constants.BUSiNESS_LOGO)
                            .setValue(it.toString())
                    }
                }
                .addOnFailureListener {
                    Log.d(
                        ContentValues.TAG,
                        "uploadToFirebase: Failed Uploading"
                    )
                }
        }
    }

    fun uploadBusinessImages(imageList: ArrayList<Uri>) {
        currentuser = mAuth.currentUser
        if (currentuser != null) {
            for (i in 0 until imageList.size) {
                val fileReference: StorageReference = storageRef.child(currentuser!!.uid)
                    .child(Constants.BUSINESS_GALLERY).child(i.toString())

                fileReference.putFile(imageList[i])
                    .addOnSuccessListener {
                        fileReference.downloadUrl.addOnSuccessListener {
                            myRef.child(currentuser!!.uid).child(Constants.BUSINESS_GALLERY).push()
                                .setValue(it.toString())
                        }
                    }
                    .addOnFailureListener {
                        Log.d(
                            ContentValues.TAG,
                            "uploadToFirebase: Failed Uploading"
                        )
                    }
            }
        }
    }

    fun uploadPan(imageUri: Uri) {
        currentuser = mAuth.currentUser
        if (currentuser != null) {
            val fileReference: StorageReference = storageRef.child(currentuser!!.uid)
                .child(Constants.PERSONAL_DETAILS).child(Constants.PAN)

            fileReference.putFile(imageUri)
                .addOnSuccessListener {
                    fileReference.downloadUrl.addOnSuccessListener {
                        myRef.child(currentuser!!.uid).child(Constants.PAN)
                            .setValue(it.toString())
                    }
                }
                .addOnFailureListener {
                    Log.d(
                        ContentValues.TAG,
                        "uploadToFirebase: Failed Uploading"
                    )
                }
        }
    }

    fun uploadAddhar(imageUri: Uri) {
        currentuser = mAuth.currentUser
        if (currentuser != null) {
            val fileReference: StorageReference = storageRef.child(currentuser!!.uid)
                .child(Constants.PERSONAL_DETAILS).child(Constants.ADDHAR)

            fileReference.putFile(imageUri)
                .addOnSuccessListener {
                    fileReference.downloadUrl.addOnSuccessListener {
                        myRef.child(currentuser!!.uid).child(Constants.ADDHAR)
                            .setValue(it.toString())
                    }
                }
                .addOnFailureListener {
                    Log.d(
                        ContentValues.TAG,
                        "uploadToFirebase: Failed Uploading"
                    )
                }
        }
    }

    fun addMemberShip(
        title: String,
        category: String? = null,
        desc: String,
        capacity: String,
        stTime: String,
        edTime: String,
        fees: String
    ) {

        val user = mAuth.currentUser

        var hash = HashMap<String, String>()
        hash["title"] = title
        hash["category"] = category ?: ""
        hash["desc"] = desc
        hash["capacity"] = capacity
        hash["stTime"] = stTime
        hash["edTime"] = edTime
        hash["fees"] = fees

        if (user != null) {
            myRef.child(user.uid).child(Constants.MEMBERSHIPS).push().setValue(hash)

            myRef.child(user.uid).child(Constants.MEMBERSHIP_COUNT)
                .addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                if (snapshot.value != null&& !valueUpdated) {
                                    addmemCount(snapshot.value.toString().toInt())
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.d(TAG, "onCancelled: cancel")
                        }

                    })

            sendUserToMainActivity()
        }
    }

    private fun addmemCount(value: Int) {
        val user = mAuth.currentUser
        val final = value + 1
        if (user != null) {
            myRef.child(user.uid).child(Constants.MEMBERSHIP_COUNT).setValue(final)
            valueUpdated=true
        }

    }
}



