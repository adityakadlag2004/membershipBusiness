package com.android.membershipbusiness.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager.widget.ViewPager
import com.android.membershipbusiness.R
import com.android.membershipbusiness.adapter.ViewPagerAdapter
import com.android.membershipbusiness.auth.LoginActivity
import com.android.membershipbusiness.auth.RegisterActivity
import com.android.membershipbusiness.fragments.intro.Intro1
import com.android.membershipbusiness.fragments.intro.intro2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_intro.*

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
        checkUser()
        btn_intro_signIn.setOnClickListener {
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
        }

        btn_intro_SignUp.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }

        tabLayout.setupWithViewPager(viewPagerIntro as ViewPager)

        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, 0)
        viewPagerAdapter.addFragment(frag1)
        viewPagerAdapter.addFragment(frag2)
        viewPagerIntro.adapter = viewPagerAdapter


    }

    private fun checkUser() {
        mAuth = FirebaseAuth.getInstance()
        currentuser = mAuth.currentUser

        if (currentuser != null) {
            if (currentuser!!.isEmailVerified)
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