package com.froyo.ridekaro.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.froyo.ridekaro.R
import com.froyo.ridekaro.helper.PreferenceHelper
import kotlinx.android.synthetic.main.activity_second_screen.*

class SecondScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_screen)


        PreferenceHelper.getSharedPreferences(this)

        val flag = PreferenceHelper.getBooleanFromPreference("languageBoolean")

        ivSplashScreenImage.setOnClickListener {
            if (flag){
                val intent = Intent(this, LanguageScreenActivity::class.java)
                startActivity(intent)
            }else {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }


        val lang = PreferenceHelper.getStringFromPreference("languagePreferenceString")


    }
}