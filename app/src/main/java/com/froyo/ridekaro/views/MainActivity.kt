package com.froyo.ridekaro.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.froyo.ridekaro.R
import com.froyo.ridekaro.helper.PreferenceHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PreferenceHelper.getSharedPreferences(this)

        if (PreferenceHelper.getBooleanFromPreference("loginCheck")) {
            val intent = Intent(this, FirstScreenActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, SecondScreenActivity::class.java)
            startActivity(intent)
        }
    }
}


