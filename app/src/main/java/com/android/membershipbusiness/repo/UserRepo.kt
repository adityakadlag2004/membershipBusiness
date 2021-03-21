package com.android.membershipbusiness.repo

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import com.android.membershipbusiness.other.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.ArrayList

class UserRepo(val contextUser: Context) : BaseRepo(contextUser) {
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.getReference(Constants.USERS)
    private var mAuth = FirebaseAuth.getInstance()
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

    fun uploadBusinessImages(imageList: ArrayList<Uri>) {
        currentuser=mAuth.currentUser
        if (currentuser!=null)
        {
          for (i in 0 until imageList.size)
          {
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


}
