package com.froyo.ridekaro.views.navDrawerFragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.froyo.ridekaro.R
import com.froyo.ridekaro.helper.PreferenceHelper
import com.froyo.ridekaro.language2Activity
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment(R.layout.fragment_settings) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        activity?.actionBar?.hide()
        super.onViewCreated(view, savedInstanceState)
        PreferenceHelper.getSharedPreferences(requireContext())

        ibLanguage.setOnClickListener {
            val intent = Intent(activity, language2Activity::class.java)
            startActivity(intent)
        }

        val value = PreferenceHelper.getStringFromPreference("languagePreferenceString")
        when (value) {

            "hi" -> {
                tvLanguageChange.text = "Hindi"
            }
            "en" -> {
                tvLanguageChange.text = "English"
            }
            "kn" -> {
                tvLanguageChange.text = "Kannada"
            }
            "ta" -> {
                tvLanguageChange.text = "Tamil"
            }
            "te" -> {
                tvLanguageChange.text = "Telugu"
            }

        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_support_button, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.supportButton -> {
                Toast.makeText(requireContext(), "clicked", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}