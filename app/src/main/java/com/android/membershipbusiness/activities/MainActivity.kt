package com.android.membershipbusiness.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.android.membershipbusiness.R
import com.android.membershipbusiness.di.DaggerFactoryComponent
import com.android.membershipbusiness.fragments.other.Customer
import com.android.membershipbusiness.fragments.other.Home
import com.android.membershipbusiness.fragments.other.Settings
import com.android.membershipbusiness.repo.MainRepository
import com.android.membershipbusiness.viewModels.MainViewModel
import com.android.mvvmdatabind2.di.modules.FactoryModule
import com.android.mvvmdatabind2.di.modules.RepositoryModule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var viewModel: MainViewModel
    private lateinit var component: DaggerFactoryComponent
    private var currentuser: FirebaseUser? = null
    private val homeFragment = Home()
    private val customerFrag = Customer()
    private val profileFrag = Settings()
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        viewModel.isUserVerified().observe(this, {
            if (it=="no")
            {

                viewModel.sendUserToVerifyActivity(1)
            }
            else{
                viewModel.checkUserHasData().observe(this, {
                    if (it == "no") {
                        viewModel.sendUsertoAddOwnerDataActivity()
                        finish()
                    }

                })
            }
        })










        bottomNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.miHome -> {
                    setCurrentFragment(homeFragment)
                    toolBar_main.title = "Home"
                }

                R.id.miCustomers -> {
                    setCurrentFragment(customerFrag)
                    toolBar_main.title = "Customers"

                }

                R.id.miProfile -> {
                    setCurrentFragment(profileFrag)
                    toolBar_main.visibility= View.GONE

                }

            }
            true
        }
    }

    private fun init() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        mAuth = FirebaseAuth.getInstance()
        currentuser = mAuth.currentUser
        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(this))
            .factoryModule(FactoryModule(MainRepository(this)))
            .build() as DaggerFactoryComponent
        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(MainViewModel::class.java)

        setCurrentFragment(homeFragment)

    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_main, fragment)
            commit()
        }
}