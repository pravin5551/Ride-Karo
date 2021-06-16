package com.froyo.ridekaro.views.navDrawerFragments

import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.froyo.ridekaro.R
import kotlinx.android.synthetic.main.fragment_power_pass.*


class NotificationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // create ContextThemeWrapper from the original Activity Context with the custom theme
        val contextThemeWrapper: Context = ContextThemeWrapper(activity, R.style.Theme_Notification)


        // clone the inflater using the ContextThemeWrapper
        val localInflater = inflater.cloneInContext(contextThemeWrapper)


        // inflate the layout using the cloned inflater, not default inflater
        return localInflater.inflate(R.layout.fragment_notifications, container, false)


    }




}