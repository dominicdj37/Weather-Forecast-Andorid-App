package com.example.fcast.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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


class HomeActivity : AppCompatActivity() {

    private lateinit var myViewModel:MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)

        submitButton.setOnClickListener{


           if(!cityInput.text.toString().isEmpty())
               myViewModel.requestWeather(cityInput.text.toString(),applicationContext)
            else
               myViewModel.errorMessage.postValue("Please enter a city")

        }

        myViewModel.responseLiveData.observe(this, Observer {
            if (it != null) {
                updateDetails(it)
            }
        })
        myViewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
        })
    }

    private fun updateDetails(it: ResponseData) {
        textViewWeather.text = it.toString()
        updateImage(it.current.condition.icon)
        tvCityName.text = it.location.name
        tvLocation.text = "${it.location.region}, ${it.location.country}."
        tvTemp.text =  it.current.tempC.toString()
        tvFeelsLike.text = "Feels like ${it.current.feelslikeC.toString()} degree celsius"
        tvHumidity.text = it.current.humidity.toString()
        tvWindSpeed.text = "${it.current.windKph.toString()} Kmph"
        tvWindHeading.text = it.current.windDir
        tvwindDegrees.text = it.current.windDegree.toString()
        tvStatus.text = it.current.condition.text


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
