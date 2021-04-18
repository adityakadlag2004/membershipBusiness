package com.android.membershipbusiness.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.android.membershipbusiness.R
import com.android.membershipbusiness.di.DaggerFactoryComponent
import com.android.membershipbusiness.repo.UserRepo
import com.android.membershipbusiness.toast
import com.android.membershipbusiness.viewModels.UserDataViewModel
import com.android.mvvmdatabind2.di.modules.FactoryModule
import com.android.mvvmdatabind2.di.modules.RepositoryModule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_forgot_password.*
import java.util.regex.Matcher

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var viewModel: UserDataViewModel
    private   val TAG = "ForgotPasswordActivity"
    private lateinit var component: DaggerFactoryComponent
    var decision = 5
    private var currentuser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        init()
        email_edit_forgot.setText("")
        if (decision==0)
        {
            toolbar_forgotPassword.title = "Reset Password"
        }
        else if(decision==1)
        {
            email_edit_forgot.setText(currentuser!!.email.toString())
            toolbar_forgotPassword.title = "Verify Your Email"
            notice_link.text = getString(R.string.notice_user_verify)

        }

        btn_submit_forgot.setOnClickListener {
            val email = email_edit_forgot.text.toString()
            if (email.isNotEmpty()) {
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (decision == 0) {

                        toolbar_forgotPassword.title = "Reset Password"
                        mAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                            if (it.isSuccessful) {
                                notice_link.visibility = View.VISIBLE
                            }
                        }
                    } else if (decision == 1) {
                        currentuser!!.sendEmailVerification().addOnSuccessListener {
                            Log.d(TAG, "onCreate: Templete Sent")
                        }.addOnFailureListener {
                            Log.d(TAG, "onCreate: ${it.message}")
                        }
                        
                        notice_link.visibility = View.VISIBLE
                        viewModel.sendUserToLoginActivity()
                        finish()
                    }

                } else {
                    toast("Enter a valid Email Address")
                    notice_link.visibility = View.GONE
                }
            } else {
                toast("Enter Your Email")
                notice_link.visibility = View.GONE
            }
        }
    }


    private fun init() {
        mAuth = FirebaseAuth.getInstance()
        currentuser = mAuth.currentUser
        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(this))
            .factoryModule(FactoryModule(UserRepo(this)))
            .build() as DaggerFactoryComponent
        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(UserDataViewModel::class.java)

        decision = intent.getIntExtra("email", 0)
        Log.d(TAG, "init: deci $decision")
    }
}