package com.mentari.sinuscan.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.mentari.sinuscan.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val buttonSubmit = findViewById<AppCompatButton>(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            openLoginFragment()
        }
    }

    private fun openLoginFragment() {
        val fragment = LoginFragment() // Replace with your fragment class name
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                android.R.anim.slide_in_left, android.R.anim.slide_out_right
            )
            .replace(R.id.loginContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}
