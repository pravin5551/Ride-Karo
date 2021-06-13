package com.froyo.ridekaro.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.froyo.ridekaro.R
import kotlinx.android.synthetic.main.activity_second_screen.*

class SecondScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_screen)
        ivSplashScreenImage.setOnClickListener {
            val intent=Intent(this,LanguageScreenActivity::class.java)
            startActivity(intent)
        }


    }
}