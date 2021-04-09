package com.android.membershipbusiness.activities.data

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.android.membershipbusiness.R
import com.android.membershipbusiness.di.DaggerFactoryComponent
import com.android.membershipbusiness.other.Constants
import com.android.membershipbusiness.repo.UserRepo
import com.android.membershipbusiness.viewModels.UserDataViewModel
import com.android.mvvmdatabind2.di.modules.FactoryModule
import com.android.mvvmdatabind2.di.modules.RepositoryModule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_owner_data.*

class AddOwnerData : AppCompatActivity() {

    var mAuth = FirebaseAuth.getInstance()
    var imageUri: Uri? = null
    var currentuser: FirebaseUser? = null
    lateinit var viewModel: UserDataViewModel
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference(Constants.USERS)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_owner_data)
        mAuth = FirebaseAuth.getInstance()
        currentuser = mAuth.currentUser

        val component: DaggerFactoryComponent = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(this))
            .factoryModule(FactoryModule(UserRepo(this)))
            .build() as DaggerFactoryComponent
        viewModel =
            ViewModelProviders.of(this, component.getFactory()).get(UserDataViewModel::class.java)

        change_photo.setOnClickListener {
            val galleryIntent = Intent()
            galleryIntent.apply {
                action = Intent.ACTION_GET_CONTENT
                type = "image/*"
                startActivityForResult(galleryIntent, 2)
            }
        }

        btn_continue_data.setOnClickListener {
            if (addName_data.text!!.isNotEmpty() && addPhone_data.text.isNotEmpty()) {
                progress_bar_data.visibility = View.VISIBLE
                myRef.child(currentuser!!.uid).child(Constants.PERSONAL_DETAILS).child(Constants.USER_NAME)
                    .setValue(addName_data.text.toString())
                myRef.child(currentuser!!.uid).child(Constants.PERSONAL_DETAILS).child(Constants.USER_PHONENUMBER)
                    .setValue(addPhone_data.text.toString())
                myRef.child(currentuser!!.uid).child(Constants.BUSINESS_CUSTOMERS_COUNT).setValue("0")
                myRef.child(currentuser!!.uid).child(Constants.STAGE).setValue("stage0")
                myRef.child(currentuser!!.uid).child(Constants.DATAADDED).setValue("notall")

                if (imageUri != null) {
                    viewModel.uploadProfileImage(imageUri!!)
                    progress_bar_data.visibility = View.VISIBLE
                } else {
                    myRef.child(currentuser!!.uid).child(Constants.USER_PROFILE_IMAGE)
                        .setValue(Constants.DEFAULT_IMAGE_PROFILE)
                    viewModel.sendUserToMainActivity()
                }
            } else {
                Toast.makeText(this, "Fill The Fields", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data!!
            profileImage_Data.setImageURI(imageUri)
        }
    }


}