package com.android.membershipbusiness.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProviders
import com.android.membershipbusiness.R
import com.android.membershipbusiness.di.DaggerFactoryComponent
import com.android.membershipbusiness.repo.MainRepository
import com.android.membershipbusiness.repo.UserRepo
import com.android.membershipbusiness.viewModels.MainViewModel
import com.android.membershipbusiness.viewModels.UserDataViewModel
import com.android.mvvmdatabind2.di.modules.FactoryModule
import com.android.mvvmdatabind2.di.modules.RepositoryModule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_add_membership.*

class AddMembership : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var viewModel: UserDataViewModel
    private lateinit var component: DaggerFactoryComponent
    private var currentuser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_membership)

        init()

        btn_continue_data_mem.setOnClickListener {
            val title = membership_title.text.toString()
            val category = membership_category.text.toString()
            val desc = membership_desc.text.toString()
            val capacity = membership_Person_capacity.text.toString()
            val st_time = startTime_mem.text.toString()
            val ed_time = endtime_mem.text.toString()
            val fees = membership_Fees.text.toString()
            if (title.isNotEmpty()) {
                if (desc.isNotEmpty()) {
                    if (capacity.isNotEmpty()) {
                        if (st_time.isNotEmpty() && ed_time.isNotEmpty()) {
                            if (fees.isNotEmpty())
                            {viewModel.addMembership(title,category,desc,capacity,st_time,ed_time,fees)}
                            else{
                                Toast.makeText(this, "Enter the Fees", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "Fill The Timing", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this, "Fill Person Capacity", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Fill The Description", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fill The Title", Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun init() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        mAuth = FirebaseAuth.getInstance()
        currentuser = mAuth.currentUser
        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(this))
            .factoryModule(FactoryModule(UserRepo(this)))
            .build() as DaggerFactoryComponent
        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(UserDataViewModel::class.java)

    }
}