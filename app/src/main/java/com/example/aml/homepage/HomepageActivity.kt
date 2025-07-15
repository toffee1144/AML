package com.example.aml.homepage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aml.R
import androidx.appcompat.app.AppCompatDelegate


class HomepageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_homepage)

        // Load HomepageFragment as the default fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.homepageContainer, HomepageFragment())
                .commit()
        }
    }
}
