package com.android.membershipbusiness.fragments.business

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.android.membershipbusiness.R
import kotlinx.android.synthetic.main.fragment_businessinfo2.*
import kotlinx.android.synthetic.main.fragment_businessinfo2.view.*


class businessinfo2 : Fragment() {

    lateinit var daysAdapter :ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_businessinfo2, container, false)

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


}