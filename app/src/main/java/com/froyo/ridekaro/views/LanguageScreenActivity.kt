package com.froyo.ridekaro.views

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.froyo.ridekaro.R
import kotlinx.android.synthetic.main.activity_language_screen.*

class LanguageScreenActivity : AppCompatActivity() {



    @RequiresApi(api= Build.VERSION_CODES.JELLY_BEAN_MR1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_screen)

       val languages=resources.getStringArray(R.array.language)
        val arrayAdapter=ArrayAdapter(this, R.layout.item_layout_language,languages)
        autoCompleteTextView.setText(arrayAdapter.getItem(0).toString(),false)
        autoCompleteTextView.setAdapter(arrayAdapter)

    }




}