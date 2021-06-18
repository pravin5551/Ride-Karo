package com.froyo.ridekaro

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import com.froyo.ridekaro.helper.PreferenceHelper
import com.froyo.ridekaro.views.HomeActivity
import com.froyo.ridekaro.views.OTPValidation
import com.froyo.ridekaro.views.navDrawerFragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_language2.*
import kotlinx.android.synthetic.main.activity_language_screen.*
import kotlinx.android.synthetic.main.activity_language_screen.autoCompleteTextView
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*

class language2Activity : AppCompatActivity() {

    lateinit var locale: Locale

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language2)

        PreferenceHelper.getSharedPreferences(this)

        val languages = resources.getStringArray(R.array.language)
        val arrayAdapter = ArrayAdapter(this, R.layout.item_layout_language, languages)
        autoCompleteTextView2.setText(arrayAdapter.getItem(0).toString(), false)
        autoCompleteTextView2.setAdapter(arrayAdapter)
        autoCompleteTextView2.setOnItemClickListener { parent, view, position, id ->
            when (languages[position]) {
                "English" -> setLocal("en", 0)
                "हिंदी (Hindi)" -> setLocal("hi", 1)
                "ಕನ್ನಡ (Kannada)" -> setLocal("kn", 2)
                "తెలుగు (Telugu)" -> setLocal("te", 3)
                "தமிழ் (Tamil)" -> setLocal("ta", 4)
            }

        }



        btnContinue.setOnClickListener {

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }




    }

    fun setLocal(localeName: String, position: Int) {

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