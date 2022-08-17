package com.endlessloopsoftwares.myapplication.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.endlessloopsoftwares.myapplication.R
import com.endlessloopsoftwares.myapplication.activities.HomeActivity
import com.endlessloopsoftwares.myapplication.activities.LoginActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment(
    private val currAuthInst: FirebaseAuth,
    private val currActivity: LoginActivity
) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val signInEmailButton: MaterialButton = view.findViewById(R.id.sign_in_email_button)
        signInEmailButton.setOnClickListener {
            currActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.login_activity_fragment, SignInFragment(currAuthInst, currActivity))
                .commit()
        }

        // getting all the views concerned in the sign in fragment
        val signInButton: MaterialButton = view.findViewById(R.id.sign_up_button)
        val emailView: EditText = view.findViewById(R.id.e_mail_edit_text_sign_up)
        val passwordView: EditText = view.findViewById(R.id.password_edit_text_sign_up)

        // setting up the On Click Listener for the sign up button
        // to login the user with entered credentials
        signInButton.setOnClickListener {
            val emailEntered = emailView.text.toString()
            val passwordEntered = passwordView.text.toString()
            if (emailEntered.isNotEmpty() && passwordEntered.isNotEmpty()) {
                currAuthInst.createUserWithEmailAndPassword(
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
    }
}