package com.endlessloopsoftwares.myapplication.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.endlessloopsoftwares.myapplication.R

class IntroActivity : AppCompatActivity() {
    private lateinit var nextButton: Button
    private lateinit var heading: TextView
    private lateinit var subHeading: TextView
    private lateinit var lottieAnimView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        nextButton = findViewById(R.id.nextButton)
        heading = findViewById(R.id.intro_heading)
        subHeading = findViewById(R.id.intro_subheading)
        lottieAnimView = findViewById(R.id.lottie_view)

        nextButton.setOnClickListener { changeIntro() }
    }

    private fun changeIntro() {
        if (nextButton.text == getString(R.string.next_text)) {
            heading.text = getString(R.string.share_layout_intro_text)
            subHeading.text = getString(R.string.share_layout_intro_subheading)
            nextButton.text = getString(R.string.get_started_text)
            lottieAnimView.setAnimation(R.raw.intro_share)
            lottieAnimView.playAnimation()
        } else {
            // editing the shared preferences and changing the current
            // login state of the user
            val mySharedPreferences: SharedPreferences = getSharedPreferences(
                "com.endlessloopsoftwares.myapplication",
                MODE_PRIVATE
            )
            mySharedPreferences.edit().putString("Login State","Done Intro").apply()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }
}