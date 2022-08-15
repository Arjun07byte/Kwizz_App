package com.endlessloopsoftwares.myapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.endlessloopsoftwares.myapplication.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mySharedPreferences = getSharedPreferences(
            "com.endlessloopsoftwares.myapplication",
            MODE_PRIVATE
        )

        // change this to login activity
        when(mySharedPreferences.getString("Login State","Intro Left")){
            "Done Intro" -> startActivity(Intent(this, LoginActivity::class.java))
            "Done Login" -> startActivity(Intent(this,HomeActivity::class.java))
            else -> startActivity(Intent(this,IntroActivity::class.java))
        }
        finish()
    }
}