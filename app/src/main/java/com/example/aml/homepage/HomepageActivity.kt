package com.example.aml.homepage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aml.R

class HomepageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Load HomepageFragment as the default fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.homepageContainer, HomepageFragment())
                .commit()
        }
    }
}
