package com.android.membershipbusiness.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProviders
import com.android.membershipbusiness.R
import com.android.membershipbusiness.di.DaggerFactoryComponent
import com.android.membershipbusiness.other.Membership
import com.android.membershipbusiness.repo.UserRepo
import com.android.membershipbusiness.toast
import com.android.membershipbusiness.viewModels.UserDataViewModel
import com.android.mvvmdatabind2.di.modules.FactoryModule
import com.android.mvvmdatabind2.di.modules.RepositoryModule
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
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


        starttime_mem.setOnClickListener {
            var time: String
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setTitleText("Select Starting Time")
                    .setMinute(10)
                    .build()

            val manager = supportFragmentManager
            picker.show(manager, "tag")

            picker.addOnPositiveButtonClickListener {

                if (picker.hour > 12) {
                    val h=picker.hour-12
                    time = h.toString() + ":" + picker.minute.toString() + "PM"

                } else {
                    time = picker.hour.toString() + ":" + picker.minute.toString() + "AM"
                }


                starttime_mem.text = time
            }

        }
        endtime_mem.setOnClickListener {
            var time: String
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setTitleText("Select Starting Time")
                    .setMinute(10)
                    .build()

            picker.addOnPositiveButtonClickListener {
                if (picker.hour > 12) {
                    val h=picker.hour-12
                    time = h.toString() + ":" + picker.minute.toString() + "PM"

                } else {
                    time = picker.hour.toString() + ":" + picker.minute.toString() + "AM"
                }
                endtime_mem.text = time
            }
            val manager = supportFragmentManager
            picker.show(manager, "tag")
        }

        btn_continue_data_mem.setOnClickListener {
            val title = membership_title.text.toString()
            val category = membership_category.text.toString()
            val desc = membership_desc.text.toString()
            val capacity = membership_Person_capacity.text.toString()
            val st_time = starttime_mem.text.toString()
            val duration = membership_duration.text.toString()
            val ed_time = endtime_mem.text.toString()
            val fees = membership_Fees.text.toString()

            val mem=Membership(title,category, desc, capacity, st_time, duration, ed_time, fees)
            if (title.isNotEmpty()) {
                if (desc.isNotEmpty()) {
                    if (capacity.isNotEmpty()) {
                        if (st_time.isNotEmpty() && ed_time.isNotEmpty()) {
                            if (fees.isNotEmpty()) {
                                if (duration.isNotEmpty()) {
                                    viewModel.addMembership(mem)
                                } else {
                                    toast("Duration Missing")
                                }

                            } else {
                                toast("Enter the Fees")
                            }
                        } else {
                            toast("Fill The Timing")
                        }

                    } else {
                        toast("Fill Person Capacity")
                    }
                } else {
                    toast("Fill The Description")
                }
            } else {
                toast("Title Missing")
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