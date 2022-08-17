package com.endlessloopsoftwares.myapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.endlessloopsoftwares.myapplication.R
import com.endlessloopsoftwares.myapplication.fragments.SignUpFragment
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // getting the current firebase auth instance of the user
        val currFirebaseAuthInst = FirebaseAuth.getInstance()

        // applying the sign up fragment to the login_activity_fragment
        // to let the user display the sign up part
        supportFragmentManager.beginTransaction()
            .replace(R.id.login_activity_fragment, SignUpFragment(currFirebaseAuthInst, this)).commit()
    }
}