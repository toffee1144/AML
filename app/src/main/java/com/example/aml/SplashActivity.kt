package com.example.aml

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aml.homepage.HomepageActivity
import com.example.aml.login.LoginActivity
import com.example.aml.utility.SessionManager

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // IMPORTANT: No setContentView! We're using the theme-based splash.
        super.onCreate(savedInstanceState)

        // Check token and navigate accordingly
        val token = SessionManager.getToken(this)

        if (!token.isNullOrEmpty()) {
            // Token exists → Skip login
            startActivity(Intent(this, HomepageActivity::class.java))
        } else {
            // No token → Show login
            startActivity(Intent(this, LoginActivity::class.java))
        }

        finish() // Close SplashActivity so it's not in the back stack
    }
}
