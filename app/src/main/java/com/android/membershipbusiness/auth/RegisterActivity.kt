package com.android.membershipbusiness.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.android.membershipbusiness.R
import com.android.membershipbusiness.activities.MainActivity
import com.android.membershipbusiness.databinding.ActivityRegisterBinding
import com.android.membershipbusiness.di.DaggerFactoryComponent
import com.android.membershipbusiness.other.Constants
import com.android.membershipbusiness.repo.AuthRepository
import com.android.membershipbusiness.viewModels.AuthViewModel
import com.android.mvvmdatabind2.di.modules.FactoryModule
import com.android.mvvmdatabind2.di.modules.RepositoryModule
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel: AuthViewModel
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val TAG = "RegisterActivity"
    val RC_SIGN_IN = 120
    private lateinit var component: DaggerFactoryComponent
    private var verifiedboolean = false
    private var currentuser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        mAuth = FirebaseAuth.getInstance()


        component = DaggerFactoryComponent.builder()
            .factoryModule(FactoryModule(AuthRepository(this)))
            .repositoryModule(RepositoryModule(this))
            .build() as DaggerFactoryComponent
        viewModel =
            ViewModelProviders.of(this, component.getFactory()).get(AuthViewModel::class.java)
        val binding = DataBindingUtil
            .setContentView<ActivityRegisterBinding>(this, R.layout.activity_register)
            .apply {
                this.lifecycleOwner = this@RegisterActivity
                this.viewmodel = viewModel
            }


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnGoogleRegister.setOnClickListener {
            signIn()
        }
        binding.txtReg.setOnClickListener {
            Intent(this, LoginActivity::class.java).also { startActivity(it) }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        currentuser = mAuth.currentUser
        if (currentuser != null) {
            verifiedboolean = currentuser!!.isEmailVerified
            if (verifiedboolean) {
                Intent(this, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(it)
                }
            }
        } else {
            Log.d(TAG, "onStart:Not Verified ")
        }
    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(Constants.USERS)
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = mAuth.currentUser
                    if (user != null) {
                        myRef.child(user.uid).child(Constants.USER_EMAIL).setValue(user.email)
                        myRef.child(user.uid).child(Constants.MEMBERSHIP_COUNT)
                            .addValueEventListener(
                                object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            if (snapshot.value != null) {
                                                Log.d(TAG, "onDataChange: has data")
                                            }
                                        }
                                        else{
                                            myRef.child(user.uid).child(Constants.MEMBERSHIP_COUNT)
                                                .setValue("0")
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Log.d(TAG, "onCancelled: cancel")
                                    }

                                })



                        myRef.child(user.uid).child(Constants.USER_NAME).setValue(
                            user.displayName ?: " "
                        )
                        myRef.child(user.uid).child(Constants.USER_ID).setValue(user.uid)
                    }
                    Intent(this, MainActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

}