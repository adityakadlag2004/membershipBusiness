package com.android.membershipbusiness.activities.data

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.android.membershipbusiness.R
import com.android.membershipbusiness.di.DaggerFactoryComponent
import com.android.membershipbusiness.fragments.business.BusinessInfo1
import com.android.membershipbusiness.fragments.business.BusinessInfo3
import com.android.membershipbusiness.fragments.business.BusinessInfo4
import com.android.membershipbusiness.fragments.business.Businessinfo2
import com.android.membershipbusiness.other.Constants
import com.android.membershipbusiness.repo.UserRepo
import com.android.membershipbusiness.viewModels.UserDataViewModel
import com.android.mvvmdatabind2.di.modules.FactoryModule
import com.android.mvvmdatabind2.di.modules.RepositoryModule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class AddBusinessData : AppCompatActivity() {
    var mAuth = FirebaseAuth.getInstance()
    var imageUri: Uri? = null
    var currentuser: FirebaseUser? = null
    lateinit var viewModel: UserDataViewModel
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference(Constants.USERS)
    var frame1 = BusinessInfo1()
    var frame2 = Businessinfo2()
    var frame3 = BusinessInfo3()
    var frame4=BusinessInfo4()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_business_data)

        mAuth = FirebaseAuth.getInstance()
        currentuser = mAuth.currentUser

        val component: DaggerFactoryComponent = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(this))
            .factoryModule(FactoryModule(UserRepo(this)))
            .build() as DaggerFactoryComponent
        viewModel =
            ViewModelProviders.of(this, component.getFactory()).get(UserDataViewModel::class.java)

        viewModel.checkStage().observe(this,{
            if (it=="stage0")
            {
                setCurrentFragment(frame1)
            }
            else if(it=="stage1"){
                setCurrentFragment(frame2)
            }
            else if(it=="stage2"){
                setCurrentFragment(frame3)
            }
            else if(it=="stage3"){
                setCurrentFragment(frame4)
            }
        })
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_addBusiness_data, fragment)
            commit()
        }
}