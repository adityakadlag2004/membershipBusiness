package com.android.membershipbusiness.activities.data

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.android.membershipbusiness.R
import com.android.membershipbusiness.fragments.business.businessinfo1
import com.android.membershipbusiness.fragments.business.businessinfo2

class AddBusinessData : AppCompatActivity() {
    var frame1=businessinfo1()
    var frame2= businessinfo2()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_business_data)

        setCurrentFragment(frame1)
    }
    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_addBusiness_data, fragment)
            commit()
        }
}