package com.froyo.ridekaro.views.navDrawerFragments

import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.froyo.ridekaro.R
import kotlinx.android.synthetic.main.fragment_power_pass.*


class PowerPassFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // create ContextThemeWrapper from the original Activity Context with the custom theme
        val contextThemeWrapper: Context = ContextThemeWrapper(activity, R.style.Theme_Notification)


        // clone the inflater using the ContextThemeWrapper
        val localInflater = inflater.cloneInContext(contextThemeWrapper)


        // inflate the layout using the cloned inflater, not default inflater
        return localInflater.inflate(R.layout.fragment_power_pass, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnRegister.setOnClickListener {

            btnRegister.setVisibility(View.GONE)
            Toast.makeText(activity, "Register for pass", Toast.LENGTH_SHORT).show()

        }
    }


}