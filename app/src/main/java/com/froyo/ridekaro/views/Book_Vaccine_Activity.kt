package com.froyo.ridekaro.views

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.froyo.ridekaro.R
import kotlinx.android.synthetic.main.activity_book_vaccine.*

class Book_Vaccine_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_vaccine)


        if (WebViewwallstreetjournal != null) {
            val webSettings = WebViewwallstreetjournal!!.settings
            webSettings.javaScriptEnabled = true
            WebViewwallstreetjournal!!.webViewClient = WebViewClient()
            WebViewwallstreetjournal!!.loadUrl("https://selfregistration.cowin.gov.in/")

            //https://www.wsj.com/
            WebViewwallstreetjournal!!.webViewClient = object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

                    wsjProgressBar.visibility = View.VISIBLE
                    super.onPageStarted(view, url, favicon)

                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    wsjProgressBar.visibility = View.GONE
                    super.onPageFinished(view, url)

                }
            }
        }
    }

    override fun onBackPressed() {
        if (WebViewwallstreetjournal!!.canGoBack()) {
            WebViewwallstreetjournal!!.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
