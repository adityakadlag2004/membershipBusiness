package com.android.membershipbusiness.fragments.business

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import android.widget.ViewSwitcher
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import com.android.membershipbusiness.R
import com.android.membershipbusiness.di.DaggerFactoryComponent
import com.android.membershipbusiness.repo.UserRepo
import com.android.membershipbusiness.viewModels.UserDataViewModel
import com.android.mvvmdatabind2.di.modules.FactoryModule
import com.android.mvvmdatabind2.di.modules.RepositoryModule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_business_info3.*
import kotlinx.android.synthetic.main.fragment_business_info3.view.*
import kotlinx.android.synthetic.main.fragment_businessinfo2.view.*


class BusinessInfo3 : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var viewModel: UserDataViewModel
    private lateinit var component: DaggerFactoryComponent
    private var currentuser: FirebaseUser? = null
    lateinit var imageList: ArrayList<Uri>
    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        currentuser = mAuth.currentUser
        imageList = ArrayList()

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_business_info3, container, false)

        view.addAllImages.setOnClickListener {
            val galleryIntent = Intent()
            galleryIntent.apply {
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                action = Intent.ACTION_GET_CONTENT
                type = "image/*"
                startActivityForResult(galleryIntent, 2)
            }
        }
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
        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(view!!.context))
            .factoryModule(FactoryModule(UserRepo(view.context)))
            .build() as DaggerFactoryComponent
        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(UserDataViewModel::class.java)
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            view.context,
            R.layout.daysdrop,
            types
        )



        view.business_type.setAdapter(adapter)

        view.images.setFactory(
            ViewSwitcher.ViewFactory
            {
                return@ViewFactory ImageView(view.context)
            })

        view.back_btn.setOnClickListener {
            if (position > 0) {
                position--
                images.setImageURI(imageList[position])
            } else {
                Toast.makeText(view.context, "No previous Images", Toast.LENGTH_SHORT).show()
            }

        }
        view.front_btn.setOnClickListener {
            if (position < imageList.size - 1) {
                position++
                images.setImageURI(imageList.get(position))
            } else {
                Toast.makeText(view.context, "No More Images", Toast.LENGTH_SHORT).show()
            }

        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data!!.clipData != null) {
            val count = data.clipData!!.itemCount
            nav_rel_layout.visibility = View.VISIBLE
            imageList.clear()
            for (i in 0
                    until count) {
                val imageUri = data.clipData!!.getItemAt(i).uri
                imageList.add(imageUri)

            }

            images.setImageURI(imageList[0])

        } else {
            imageList.clear()
            if (data != null && data.data != null) {
                val uris = data.data
                uris?.let { imageList.add(it) }
            }

        }
    }

}