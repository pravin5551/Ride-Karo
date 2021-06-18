package com.froyo.ridekaro.views.navDrawerFragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.froyo.ridekaro.R
import kotlinx.android.synthetic.main.activity_get_vaccinated.*

class GetVaccinated : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_vaccinated)

        webView.loadUrl("https://www.cowin.gov.in/home")
    }
}