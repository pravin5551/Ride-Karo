package com.froyo.ridekaro.views

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.froyo.ridekaro.R
import com.froyo.ridekaro.helper.KEY_LOGIN_WITH_OAUTH
import com.froyo.ridekaro.helper.PreferenceHelper
import com.froyo.ridekaro.helper.USER_PHONE_LOGIN
import kotlinx.android.synthetic.main.activity_language_screen.*
import java.util.*

class LanguageScreenActivity : AppCompatActivity() {

    lateinit var locale: Locale


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_screen)

        PreferenceHelper.getSharedPreferences(this)

        setLocal("en", 0)

        val languages = resources.getStringArray(R.array.language)
        val arrayAdapter = ArrayAdapter(this, R.layout.item_layout_language, languages)
        autoCompleteTextView.setText(arrayAdapter.getItem(0).toString(), false)
        autoCompleteTextView.setAdapter(arrayAdapter)
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            when (languages[position]) {
                "English" -> setLocal("en", 0)
                "हिंदी (Hindi)" -> setLocal("hi", 1)
                "ಕನ್ನಡ (Kannada)" -> setLocal("kn", 2)
                "తెలుగు (Telugu)" -> setLocal("te", 3)
                "தமிழ் (Tamil)" -> setLocal("ta", 4)
            }

        }

        btnNext.setOnClickListener {

            if (PreferenceHelper.getLoginBooleanFromPreference(KEY_LOGIN_WITH_OAUTH) ||
                PreferenceHelper.getLoginBooleanFromPreference(USER_PHONE_LOGIN)
            ) {
//                val intent = Intent(this, HomeActivity::class.java)
                val intent = Intent(this, OTPValidation::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, OTPValidation::class.java)
                startActivity(intent)
            }

        }


    }

    private fun setLocal(localeName: String, position: Int) {

        locale = Locale(localeName)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf, dm)

        PreferenceHelper.writeIntToPreference("languagePreference", position)
        PreferenceHelper.writeStringToPreference("languagePreferenceString", localeName)
        PreferenceHelper.writeBooleanToPreference("languageBoolean", false)
    }


}