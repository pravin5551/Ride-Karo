package com.froyo.ridekaro.views

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.froyo.ridekaro.R
import kotlinx.android.synthetic.main.activity_language_screen.*

class LanguageScreenActivity : AppCompatActivity() {


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_screen)

        val languages = resources.getStringArray(R.array.language)
        val arrayAdapter = ArrayAdapter(this, R.layout.item_layout_language, languages)
        autoCompleteTextView.setText(arrayAdapter.getItem(0).toString(), false)
        autoCompleteTextView.setAdapter(arrayAdapter)

        btnNext.setOnClickListener {
            val intent = Intent(this, OTPValidation::class.java)
            startActivity(intent)


        }


    }


}