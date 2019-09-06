package com.example.myapplication

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*
import android.content.DialogInterface
import android.os.Handler
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {
    private val url = "https://m.povar.ru/"
    private val banner1 = "adfox_154322856689062874"
    private val banner2 = "yandex_rtb_R-A-59182-10"
    private val banner3 = "aswift_0_expand"


    private lateinit var mInterstitialAd: InterstitialAd
    var mywebview: WebView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val settings = webView.settings
        settings.javaScriptEnabled = true

        MobileAds.initialize(this, "ca-app-pub-9915769615752633~2652689747")
        mInterstitialAd = InterstitialAd(this)

        mInterstitialAd.adUnitId = "ca-app-pub-9915769615752633/9276204155"
        //mInterstitialAd.loadAd(AdRequest.Builder().build())
          val request = AdRequest.Builder().addTestDevice("28E47C5FC59254754487A411B26EB9B6").build()
        mInterstitialAd.loadAd(request)

        mywebview = findViewById<WebView>(R.id.webView)
        mywebview!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }

            // Set web view client
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                // Page loading started
                // Do something
            }

            override fun onPageFinished(view: WebView, url: String) {
                webView.loadUrl("javascript:(function() { "
                        + "document.getElementById("+banner1+").style.display='none';})()"
                        + "document.getElementById("+banner2+").style.display='none';})()"
                        + "document.getElementById("+banner3+").style.display='none';})()")

            }
        }

        this.button.setOnClickListener {
            this.textView.text = "PROGRAM STARTS"

            this.textView.text = "WEBVIEW STARTS"
            webView.loadUrl(url)

            this.textView.text = "ADMOB STARTS"

            val handler = Handler()
            val dialogBuilder = AlertDialog.Builder(this)

            // set message of alert dialog
            dialogBuilder.setMessage("GET SUBSCRIPTION OR SEE ADVERTISING. ADVERTASING IS STARTING AFTER 10 SENONDS")
                // if the dialog is cancelable
                .setCancelable(false)

                // negative button text and action
                .setPositiveButton("GET SUBSCRIPTION", DialogInterface.OnClickListener { dialog, id ->
                    handler.removeCallbacksAndMessages(null)
                    dialog.cancel()
                })
            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("WARNING")
            // show alert dialog
            alert.show()


            handler.postDelayed({
                if (mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                    alert.dismiss()
                    this.textView.text = "ADMOB ENDED"
                } else {
                    this.textView.text = "ADVERTISING HAS NOT DOWNLOADED"
                }
            }, 10000)


        }
    }
}

