package com.froyo.ridekaro.helper

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper {


    companion object{
        private var sharedPreferences: SharedPreferences? = null
        private val PREF_NAME = "users"

        fun getSharedPreferences(context: Context): SharedPreferences? {
            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            }
            return sharedPreferences
        }
        fun writeIntToPreference(key: String?, value: Int) {
            val editor = sharedPreferences!!.edit()
            editor.putInt(key, value)
            editor.apply()
        }

        fun writeBooleanToPreference(key: String?, value: Boolean) {
            val editor = sharedPreferences!!.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        fun writeStringToPreference(key: String?, value: String?) {
            val editor = sharedPreferences!!.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun getIntFromPreference(key: String?): Int {
            return sharedPreferences!!.getInt(key, 0)
        }

        fun getStringFromPreference(key: String?): String? {
            return sharedPreferences!!.getString(key, "en")
        }

        fun getBooleanFromPreference(key: String?): Boolean {
            return sharedPreferences!!.getBoolean(key, true)
        }

        fun getLoginBooleanFromPreference(key: String?): Boolean {
            return sharedPreferences!!.getBoolean(key, false)
        }
    }

}