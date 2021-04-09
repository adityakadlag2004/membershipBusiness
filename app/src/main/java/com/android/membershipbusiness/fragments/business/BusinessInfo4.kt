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
import kotlinx.android.synthetic.main.activity_add_owner_data.*
import kotlinx.android.synthetic.main.activity_add_owner_data.profileImage_Data
import kotlinx.android.synthetic.main.fragment_business_info4.*
import kotlinx.android.synthetic.main.fragment_business_info4.view.*
import java.net.URI


class BusinessInfo4 : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var viewModel: UserDataViewModel
    private lateinit var component: DaggerFactoryComponent
    private var currentuser: FirebaseUser? = null
    lateinit var panUri: Uri
    lateinit var addharUri: Uri
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.getReference(Constants.USERS)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        currentuser = mAuth.currentUser
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_business_info4, container, false)
        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(view!!.context))
            .factoryModule(FactoryModule(UserRepo(view.context)))
            .build() as DaggerFactoryComponent
        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(UserDataViewModel::class.java)

        view.addPan.setOnClickListener {
            val galleryIntent = Intent()
            galleryIntent.apply {
                action = Intent.ACTION_GET_CONTENT
                type = "image/*"
                startActivityForResult(galleryIntent, 1)
            }
        }
        view.addAddhar.setOnClickListener {
            val galleryIntent = Intent()
            galleryIntent.apply {
                action = Intent.ACTION_GET_CONTENT
                type = "image/*"
                startActivityForResult(galleryIntent, 2)
            }
        }
        view.btn_continue_business4.setOnClickListener {
            view.progress_bar_pan.visibility = View.VISIBLE
            val upiID = view.upi_id.text.toString()

            if (upiID.isNotEmpty()) {
                val pan = panUri.toString()
                if (pan.isNotEmpty()) {
                    if (addharUri.toString().isNotEmpty()) {
                        viewModel.uploadPanCard(panUri)
                        viewModel.uploadAddharCard(addharUri)
                        myRef.child(currentuser!!.uid).child(Constants.PERSONAL_DETAILS).child(Constants.UPIID).setValue(upiID)
                        myRef.child(currentuser!!.uid).child(Constants.DATAADDED)
                            .setValue("inreview").addOnCompleteListener {
                                viewModel.sendUserToMainActivity()
                                activity!!.finish()
                            }
                    } else {
                        view.progress_bar_pan.visibility = View.GONE
                        Toast.makeText(view.context, "Add Your Addhar Card", Toast.LENGTH_SHORT)
                            .show()

                    }
                } else {
                    view.progress_bar_pan.visibility = View.GONE
                    Toast.makeText(view.context, "Add Your PAN Card", Toast.LENGTH_SHORT).show()
                }
            } else {
                view.progress_bar_pan.visibility = View.GONE
                Toast.makeText(view.context, "Add Your UPI ID", Toast.LENGTH_SHORT).show()
            }

        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            panUri = data.data!!
            panView.setImageURI(panUri)
        } else if (requestCode == 2 && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            addharUri = data.data!!
            addharView.setImageURI(addharUri)
        }
    }

}