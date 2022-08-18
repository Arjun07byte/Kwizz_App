package com.endlessloopsoftwares.myapplication.fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.endlessloopsoftwares.myapplication.R
import com.endlessloopsoftwares.myapplication.activities.HomeActivity
import com.endlessloopsoftwares.myapplication.activities.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignInFragment(
    private val currAuthInst: FirebaseAuth,
    private val currActivity: LoginActivity
) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    private lateinit var myGoogleSignInClient: GoogleSignInClient
    private lateinit var currView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // changing the fragment when user clicks on the register with
        // email button
        val registerEmailButton: MaterialButton = view.findViewById(R.id.register_email_button)
        registerEmailButton.setOnClickListener {
            currActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.login_activity_fragment, SignUpFragment(currAuthInst, currActivity))
                .commit()
        }

        // getting all the views concerned in the sign in fragment
        val signInButton: MaterialButton = view.findViewById(R.id.sign_in_button)
        val emailView: EditText = view.findViewById(R.id.e_mail_edit_text_sign_in)
        val passwordView: EditText = view.findViewById(R.id.password_edit_text_sign_in)
        val googleSignInButton: MaterialButton = view.findViewById(R.id.sign_in_google_button)
        currView = view

        // setting up the On Click Listener for the sign in button
        // to login the user with entered credentials
        signInButton.setOnClickListener {
            val emailEntered = emailView.text.toString()
            val passwordEntered = passwordView.text.toString()
            if (emailEntered.isNotEmpty() && passwordEntered.isNotEmpty()) {
                currAuthInst.signInWithEmailAndPassword(
                    emailEntered,
                    passwordEntered
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // editing the shared preferences and changing the current
                        // login state of the user
                        val mySharedPreferences: SharedPreferences =
                            currActivity.getSharedPreferences(
                                "com.endlessloopsoftwares.myapplication",
                                AppCompatActivity.MODE_PRIVATE
                            )
                        mySharedPreferences.edit().putString("Login State", "Done Login").apply()
                        startActivity(Intent(currActivity, HomeActivity::class.java))
                        currActivity.finish()
                    } else {
                        Snackbar.make(
                            view,
                            it.exception!!.message.toString(),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Snackbar.make(
                    view,
                    "Please Enter E-mail and Password",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        // setting up the signInWithGoogle feature
        setUpGoogleSignInVar()
        googleSignInButton.setOnClickListener { callGoogleSignIn() }
    }

    private fun setUpGoogleSignInVar() {
        val myGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        myGoogleSignInClient = GoogleSignIn.getClient(currActivity, myGoogleSignInOptions)
    }

    private fun callGoogleSignIn() {
        val signInIntent = myGoogleSignInClient.signInIntent
        myStartActivityForResult.launch(signInIntent)
    }

    private val myStartActivityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val receivedData = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
            try {
                val accountSignedIn = receivedData.result
                authGoogleIdWithFirebase(accountSignedIn.idToken)
            } catch (exceptionOccurred: ApiException) {
                Snackbar.make(
                    currView,
                    "Unable to Sign In $exceptionOccurred",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun authGoogleIdWithFirebase(idToken: String?) {
        val userCredentials = GoogleAuthProvider.getCredential(idToken, null)
        currAuthInst.signInWithCredential(userCredentials)
            .addOnCompleteListener(currActivity) { taskReturned ->
                if (taskReturned.isSuccessful) {
                    val homeActivityIntent = Intent(currActivity, HomeActivity::class.java)
                    startActivity(homeActivityIntent)
                    currActivity.finish()
                } else {
                    Snackbar.make(currView, "Sign In failed", Snackbar.LENGTH_SHORT).show()
                }
            }
    }
}