package com.android.membershipbusiness.fragments.business

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.membershipbusiness.R
import kotlinx.android.synthetic.main.fragment_businessinfo1.view.*


class businessinfo1 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_businessinfo1, container, false)

        view.btn_continue_business1.setOnClickListener {
            val manager=activity!!.supportFragmentManager
            val frag2=businessinfo2()
            manager.beginTransaction().replace(R.id.frame_addBusiness_data,frag2)
                .addToBackStack(null)
                .commit()
        }
        return view
    }


}