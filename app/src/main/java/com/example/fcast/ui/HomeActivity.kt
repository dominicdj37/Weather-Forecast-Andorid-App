package com.example.fcast.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.Target
import com.example.fcast.R
import com.example.fcast.data.Responce.ResponseData
import com.example.fcast.data.WeatherApiService
import kotlinx.android.synthetic.main.activity_main.*
import android.webkit.WebViewClient as WebViewClient1


class HomeActivity : AppCompatActivity() {

    private lateinit var myViewModel:MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initWebView()

        myViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)

        submitButton.setOnClickListener{


           if(!cityInput.text.toString().isEmpty())
               myViewModel.requestWeather(cityInput.text.toString(),applicationContext)
            else
               myViewModel.errorMessage.postValue("Please enter a city")

        }

        myViewModel.responseLiveData.observe(this, Observer {
            textViewWeather.text = it.toString()
            if (it != null) {
                updateDetails(it)
            }
        })
        myViewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
        })
    }

    private fun initWebView() {
        val settings = webView1.settings
        // Enable java script in web view

        // Enable and setup web view cache
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.setAppCachePath(cacheDir.path)


        // Enable zooming in web view
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = true


        // Zoom web view text
        settings.textZoom = 125


        // Enable disable images in web view
        settings.blockNetworkImage = false
        // Whether the WebView should load image resources
        settings.loadsImagesAutomatically = true


        // More web view settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.safeBrowsingEnabled = true  // api 26
        }
        //settings.pluginState = WebSettings.PluginState.ON
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.mediaPlaybackRequiresUserGesture = false


        // More optional settings, you can enable it by yourself
        settings.domStorageEnabled = true
        settings.setSupportMultipleWindows(true)
        settings.loadWithOverviewMode = true
        settings.allowContentAccess = true
        settings.setGeolocationEnabled(true)
        settings.allowUniversalAccessFromFileURLs = true
        settings.allowFileAccess = true

        // WebView settings
        webView1.fitsSystemWindows = true


        /*
            if SDK version is greater of 19 then activate hardware acceleration
            otherwise activate software acceleration
        */
        webView1.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        webView1.webViewClient = object: android.webkit.WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar1.visibility = View.GONE
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBar1.visibility = View.VISIBLE
            }

        }
        webView1.loadUrl("https://www.bbc.com/weather/1273874")
    }


    private fun updateDetails(it: ResponseData) {
        updateImage(it.current.condition.icon)
    }

    @SuppressLint("CheckResult")
    private fun updateImage(icon: String) {
        GlideApp.with(this).asBitmap()
            .load(Uri.parse("http:$icon")).fitCenter()
            .listener(object: RequestListener<Bitmap>{
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                    myViewModel.errorMessage.postValue("image could not be loaded ${e?.message.toString()}")
                    return false
                }
                override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                    return false
                }

            }).into(StatusImageView)
    }




}
