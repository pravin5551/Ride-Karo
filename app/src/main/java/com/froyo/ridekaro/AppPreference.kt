package com.froyo.ridekaro

import android.content.Context
import android.content.SharedPreferences

object AppPreference {


    private const val NAME = "Ride karo"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    private val FIRST_TIME: Boolean = true

    private val LANGUAGE = "En"


}