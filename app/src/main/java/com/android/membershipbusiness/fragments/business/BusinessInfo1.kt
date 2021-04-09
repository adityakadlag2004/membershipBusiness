package com.android.membershipbusiness.fragments.business

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.android.synthetic.main.fragment_businessinfo1.*
import kotlinx.android.synthetic.main.fragment_businessinfo1.view.*


class BusinessInfo1 : Fragment() {
    var mAuth = FirebaseAuth.getInstance()
    var imageUri: Uri? = null
    var currentuser: FirebaseUser? = null
    lateinit var viewModel: UserDataViewModel
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference(Constants.USERS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_businessinfo1, container, false)
        mAuth = FirebaseAuth.getInstance()
        currentuser = mAuth.currentUser

        val component: DaggerFactoryComponent = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(view.context))
            .factoryModule(FactoryModule(UserRepo(view.context)))
            .build() as DaggerFactoryComponent
        viewModel =
            ViewModelProviders.of(this, component.getFactory()).get(UserDataViewModel::class.java)
        view.add_Business_Logo.setOnClickListener {
            val galleryIntent = Intent()
            galleryIntent.apply {
                action = Intent.ACTION_GET_CONTENT
                type = "image/*"
                startActivityForResult(galleryIntent, 2)
            }
        }




        view.btn_continue_business1.setOnClickListener {
            if (add_Business_name.text!!.isNotEmpty() && add_Business_OwnerName.text!!.isNotEmpty()) {
                view.progress_bar_data_Busi.visibility = View.VISIBLE
                myRef.child(currentuser!!.uid).child(Constants.BUSINESS_DETAILS).child(Constants.BUSINESS_NAME)
                    .setValue(add_Business_name.text.toString())
                myRef.child(currentuser!!.uid).child(Constants.BUSINESS_DETAILS).child(Constants.BUSINESS_OWNER)
                    .setValue(add_Business_OwnerName.text.toString())
                myRef.child(currentuser!!.uid).child(Constants.BUSINESS_DETAILS).child(Constants.BUSINESS_CUSTOMERS_COUNT).setValue("0")
                myRef.child(currentuser!!.uid).child(Constants.STAGE).setValue("stage1")
                if (imageUri != null) {
                    viewModel.uploadToFirebaseBusiness(imageUri!!)
                    view.progress_bar_data_Busi.visibility = View.VISIBLE
                    val manager = activity!!.supportFragmentManager
                    val frag2 = Businessinfo2()
                    manager.beginTransaction().replace(R.id.frame_addBusiness_data, frag2)
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right)
                        .commit()
                } else {
                    myRef.child(currentuser!!.uid).child(Constants.BUSINESS_DETAILS).child(Constants.BUSiNESS_LOGO)
                        .setValue(Constants.DEFAULT_IMAGE_PROFILE)
                }
            } else {
                Toast.makeText(view.context, "Fill The Fields", Toast.LENGTH_SHORT).show()
            }

        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            imageUri = data.data!!
            profileImage_Data_Business.setImageURI(imageUri)
        }
    }

}