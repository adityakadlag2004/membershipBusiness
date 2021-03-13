package com.android.membershipbusiness.fragments.business

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.android.membershipbusiness.R
import kotlinx.android.synthetic.main.fragment_business_info3.view.*
import kotlinx.android.synthetic.main.fragment_businessinfo2.view.*
import kotlinx.android.synthetic.main.fragment_businessinfo2.view.startDay


class BusinessInfo3 : Fragment() {

    lateinit var typeAdapter: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_business_info3, container, false)
        val types = arrayOf(
            "Gym",
            "Yoga Classes",
            "Dance Studio",
            "Meditation",
            "Health Club",
            "Library",
            "Swimming",
            "Tuition",
            "Laugh Club",
            "Massage Center",
            "If not available Type Yourself"
        )
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            view.context,
            R.layout.daysdrop,
            types
        )

        view.business_type.setAdapter(adapter)

        return view
    }


}