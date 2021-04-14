package com.android.membershipbusiness.fragments.other

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.android.membershipbusiness.R
import com.android.membershipbusiness.di.DaggerFactoryComponent
import com.android.membershipbusiness.other.Constants
import com.android.membershipbusiness.repo.UserRepo
import com.android.membershipbusiness.viewModels.UserDataViewModel
import com.android.mvvmdatabind2.di.modules.FactoryModule
import com.android.mvvmdatabind2.di.modules.RepositoryModule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class Settings : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var viewModel: UserDataViewModel
    private lateinit var component: DaggerFactoryComponent
    private var currentuser: FirebaseUser? = null

    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.getReference(Constants.USERS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        currentuser = mAuth.currentUser

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(view!!.context))
            .factoryModule(FactoryModule(UserRepo(view.context)))
            .build() as DaggerFactoryComponent
        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(UserDataViewModel::class.java)

        viewModel.getBusinessLogo().observe(viewLifecycleOwner, {
            if (it != null) {
                Picasso.get().load(it.toUri())
                    .placeholder(R.drawable.user_image)
                    .error(R.drawable.user_image)
                    .into(view.settings_business_logo)
            }

        })



        view.toolbar_profile.setNavigationOnClickListener {
            if (it.id==R.id.settings_profile)
            {
                Toast.makeText(view.context, "Yeah", Toast.LENGTH_SHORT).show()
            }
        }


        view.logoutText.setOnClickListener {
            mAuth.signOut()
            viewModel.sendUserToLoginActivity()
            activity!!.finish()
        }
        viewModel.getBusinessName().observe(viewLifecycleOwner, {
            if (it != "null") {
                view.setting_business_name.text = it.toString()
            }
        })

        viewModel.getMemberShipCount().observe(viewLifecycleOwner, {
            if (it == null) {
                view.memberShipCount_settings.text = "0"
            }
            view.memberShipCount_settings.text = it.toString()
        })

        viewModel.getTotalCustomers().observe(viewLifecycleOwner, {
            view.settings_total_cus.text = it.toString()
        })

        viewModel.getOwner().observe(viewLifecycleOwner, {
            if (it != "null")
                view.usernameFrag.text = it.toString()
        })


        viewModel.getBusinessEmail().observe(viewLifecycleOwner, {
            if (it != "null")
                view.emailFrag.text = it.toString()
        })


        viewModel.getContactNumber().observe(viewLifecycleOwner, {
            if (it != "null")
                view.phoneFrag.text = it.toString()
        })

        viewModel.getAddress().observe(viewLifecycleOwner, {
            if (it != "null")
                view.memCount.text = it.toString()
        })

        return view
    }


}