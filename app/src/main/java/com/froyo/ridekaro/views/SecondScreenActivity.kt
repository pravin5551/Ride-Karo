package com.froyo.ridekaro.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.froyo.ridekaro.R
import com.froyo.ridekaro.helper.PreferenceHelper

class SecondScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_screen)


        PreferenceHelper.getSharedPreferences(this)

        val flag = PreferenceHelper.getBooleanFromPreference("languageBoolean")

        Handler().postDelayed(Runnable {
            if (flag) {
                val intent = Intent(this, LanguageScreenActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }, 2000)


    }

}