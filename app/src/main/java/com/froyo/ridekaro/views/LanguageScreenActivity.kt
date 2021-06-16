package com.froyo.ridekaro.views

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.froyo.ridekaro.R
import kotlinx.android.synthetic.main.activity_language_screen.*
import java.util.*

class LanguageScreenActivity : AppCompatActivity() {

    lateinit var locale: Locale


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_screen)

        val languages = resources.getStringArray(R.array.language)
        val arrayAdapter = ArrayAdapter(this, R.layout.item_layout_language, languages)
        autoCompleteTextView.setText(arrayAdapter.getItem(0).toString(), false)
        autoCompleteTextView.setAdapter(arrayAdapter)
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            when (languages[position]) {
                "English" -> setLocal("en")
                "हिंदी (Hindi)" -> setLocal("hi")
                "ಕನ್ನಡ (Kannada)" -> setLocal("kn")
                "తెలుగు (Telugu)" -> setLocal("te")
                "தமிழ் (Tamil)" -> setLocal("ta")
            }

        }

        btnNext.setOnClickListener {


           // val intent = Intent(this, HomeActivity::class.java)

            val intent = Intent(this, OTPValidation::class.java)

            startActivity(intent)
        }


    }

    fun setLocal(localeName: String) {

        locale = Locale(localeName)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf, dm)

    }


}