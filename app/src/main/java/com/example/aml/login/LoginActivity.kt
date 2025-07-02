package com.example.aml.login

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.example.aml.R
import com.example.aml.login.LoginFragment
import com.example.aml.login.SignUpFragment

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
