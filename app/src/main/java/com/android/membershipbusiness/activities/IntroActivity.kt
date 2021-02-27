package com.android.membershipbusiness.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.android.membershipbusiness.R
import com.android.membershipbusiness.adapter.ViewPagerAdapter
import com.android.membershipbusiness.fragments.intro.Intro1
import com.android.membershipbusiness.fragments.intro.intro2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class IntroActivity : AppCompatActivity() {
    private val frag1 = Intro1()
    private val frag2 = intro2()
    private lateinit var mAuth: FirebaseAuth
    private var currentuser: FirebaseUser? = null
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }

    private fun checkUser() {
        mAuth = FirebaseAuth.getInstance()
        currentuser = mAuth.currentUser

        if (currentuser != null) {
            sendUserToMainActivity()
        }

    }
    private fun sendUserToMainActivity() {
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}