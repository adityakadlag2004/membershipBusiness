package com.android.membershipbusiness.fragments.other

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.android.membershipbusiness.R
import com.android.membershipbusiness.activities.AddMembership
import com.android.membershipbusiness.di.DaggerFactoryComponent
import com.android.membershipbusiness.repo.UserRepo
import com.android.membershipbusiness.viewModels.UserDataViewModel
import com.android.mvvmdatabind2.di.modules.FactoryModule
import com.android.mvvmdatabind2.di.modules.RepositoryModule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class Home : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var viewModel: UserDataViewModel
    private lateinit var component: DaggerFactoryComponent
    private var currentuser: FirebaseUser? = null
    var state = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        currentuser = mAuth.currentUser
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        mAuth = FirebaseAuth.getInstance()
        currentuser = mAuth.currentUser
        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(view.context))
            .factoryModule(FactoryModule(UserRepo(view.context)))
            .build() as DaggerFactoryComponent
        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(UserDataViewModel::class.java)

        view.add_memberships.setOnClickListener {
            Intent(view.context, AddMembership::class.java).also {
                startActivity(it)
            }
        }
        viewModel.checkUserBusinessData().observe(viewLifecycleOwner, { data ->
            if (data == "notall") {
                view.add_Business.visibility = View.VISIBLE
                view.main_notice_lottie.visibility = View.GONE
                view.add_memberships.visibility = View.GONE
                state = "notall"

            } else if (data == "inreview") {
                state = "inreview"
                view.main_notice_lottie.visibility = View.VISIBLE
                view.add_Business.visibility = View.VISIBLE
                view.main_notice1.text = view.resources.getString(R.string.applicationNotice)
                view.add_Business_notice.text =
                    view.resources.getString(R.string.subNoticeApplication)
                view.add_Business_btn.visibility=View.GONE
                view.add_memberships.visibility = View.VISIBLE

            } else {
                view.main_notice_lottie.visibility = View.GONE
                view.add_Business.visibility = View.GONE
                view.add_memberships.visibility = View.GONE

            }
        })
        view.add_Business.setOnClickListener {
            if (state != "inreview") {
                viewModel.sendtoAddBusinessDataActivity()
            }
        }


        view.add_Business_btn.setOnClickListener {
            if (state != "inreview") {
                viewModel.sendtoAddBusinessDataActivity()
            }
        }
        return view
    }


}