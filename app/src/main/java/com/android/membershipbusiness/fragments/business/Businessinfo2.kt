package com.android.membershipbusiness.fragments.business

import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.android.membershipbusiness.R
import com.android.membershipbusiness.di.DaggerFactoryComponent
import com.android.membershipbusiness.other.Constants
import com.android.membershipbusiness.repo.UserRepo
import com.android.membershipbusiness.viewModels.UserDataViewModel
import com.android.mvvmdatabind2.di.modules.FactoryModule
import com.android.mvvmdatabind2.di.modules.RepositoryModule
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_membership.*
import kotlinx.android.synthetic.main.fragment_businessinfo2.view.*


class Businessinfo2 : Fragment() {
    var mAuth = FirebaseAuth.getInstance()
    var imageUri: Uri? = null
    lateinit var manager: FragmentManager
    var currentuser: FirebaseUser? = null
    lateinit var viewModel: UserDataViewModel
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference(Constants.USERS)
//    lateinit var daysAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_businessinfo2, container, false)
        mAuth = FirebaseAuth.getInstance()
        currentuser = mAuth.currentUser

        val component: DaggerFactoryComponent = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(view.context))
            .factoryModule(FactoryModule(UserRepo(view.context)))
            .build() as DaggerFactoryComponent
        viewModel =
            ViewModelProviders.of(this, component.getFactory()).get(UserDataViewModel::class.java)

        view.starttime.setOnClickListener {
            var time = ""
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setTitleText("Select Starting Time")
                    .setMinute(10)
                    .build()

            var ampm = "AM"
            val manager = activity!!.supportFragmentManager

            picker.show(manager, "tag")

            picker.addOnPositiveButtonClickListener {

                if (picker.hour > 12) {
                    val h = picker.hour - 12
                    time = h.toString() + ":" + picker.minute.toString() + "PM"

                } else {
                    time = picker.hour.toString() + ":" + picker.minute.toString() + "AM"
                }


                view.starttime.text = time
            }

        }
        view.endtime.setOnClickListener {
            var time = "AM"
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setTitleText("Select Starting Time")
                    .setMinute(10)
                    .build()

            picker.addOnPositiveButtonClickListener {
                if (picker.hour > 12) {
                    val h = picker.hour - 12
                    time = h.toString() + ":" + picker.minute.toString() + "PM"

                } else {
                    time = picker.hour.toString() + ":" + picker.minute.toString() + "AM"
                }
                view.endtime.text = time
            }
            val manager = activity!!.supportFragmentManager
            picker.show(manager, "tag")
        }
        view.btn_continue_business2.setOnClickListener {
            val contact = view.add_Business_contact.text.toString()
            val email = view.add_Business_email_business2.text.toString()
            val startTime = view.starttime.text.toString()
            val endTime = view.endtime.text.toString()
            val startDay = view.startDay.text.toString()
            val endDay = view.endDay.text.toString()
            val uid = mAuth.currentUser!!.uid
            if (contact.isNotEmpty() && email.isNotEmpty() && startTime.isNotEmpty() && endTime.isNotEmpty()
                && startDay.isNotEmpty() && endDay.isNotEmpty()
            ) {
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    myRef.child(uid).child(Constants.BUSINESS_DETAILS).child(Constants.BUSINESS_CONTACTNUMBER).setValue(contact)
                    myRef.child(uid).child(Constants.BUSINESS_DETAILS).child(Constants.BUSINESS_EMAIL).setValue(email)
                    myRef.child(uid).child(Constants.BUSINESS_DETAILS).child(Constants.BUSINESS_STARTTIME).setValue(startTime)
                    myRef.child(uid).child(Constants.BUSINESS_DETAILS).child(Constants.BUSINESS_ENDTIME).setValue(endTime)
                    myRef.child(uid).child(Constants.BUSINESS_DETAILS).child(Constants.BUSINESS_STARTDAY).setValue(startDay)
                    myRef.child(uid).child(Constants.BUSINESS_DETAILS).child(Constants.BUSINESS_ENDDAY).setValue(endDay)
                    myRef.child(uid).child(Constants.STAGE).setValue("stage2")
                        .addOnSuccessListener {
                            val frag3 = BusinessInfo3()
                            manager.beginTransaction().replace(R.id.frame_addBusiness_data, frag3)
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right)
                                .commit()
                        }
                } else {
                    Toast.makeText(view.context, "Enter a Valid Email Address", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(view.context, "", Toast.LENGTH_SHORT).show()
            }
        }
        val days = arrayOf(
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday",
            "Sunday"
        )
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            view.context,
            R.layout.daysdrop,
            days
        )

        view.startDay.setAdapter(adapter)
        view.endDay.setAdapter(adapter)
        return view
    }

    override fun onStart() {
        super.onStart()
        manager = activity!!.supportFragmentManager

    }

}