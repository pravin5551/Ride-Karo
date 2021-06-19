package com.froyo.ridekaro.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.froyo.ridekaro.R
import com.froyo.ridekaro.helper.PreferenceHelper
import kotlinx.android.synthetic.main.fragment_language.*
import java.util.*


class LanguageFragment : Fragment(R.layout.fragment_language) {

    lateinit var locale: Locale

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { PreferenceHelper.getSharedPreferences(it) }

        val languages = resources.getStringArray(R.array.language)

        val arrayAdapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.item_layout_language,
                languages
            )
        }

        autoCompleteTextView2.setText(arrayAdapter?.getItem(0).toString(), false)
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
            val intent = Intent(context, HomeActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            // activity?.onBackPressed()
            //  activity?.recreate()

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