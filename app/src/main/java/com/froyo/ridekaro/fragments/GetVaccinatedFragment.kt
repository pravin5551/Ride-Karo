package com.froyo.ridekaro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.froyo.ridekaro.R
import kotlinx.android.synthetic.main.fragment_get_vaccinated.*

class GetVaccinatedFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_get_vaccinated, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        covidWeb.settings.javaScriptEnabled=true
        covidWeb.settings.useWideViewPort=true
        covidWeb.settings.loadWithOverviewMode=true
        covidWeb.loadUrl("https://www.cowin.gov.in/home/")
    }
}