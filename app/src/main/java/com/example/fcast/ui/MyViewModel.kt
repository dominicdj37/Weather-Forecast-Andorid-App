package com.example.fcast.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.example.fcast.data.Responce.ResponseData
import com.example.fcast.data.WeatherApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyViewModel : ViewModel(){

    var responseLiveData = MutableLiveData<ResponseData>()
    var errorMessage = MutableLiveData<String>()

     fun requestWeather(city: String,context: Context) {

        var requestAPI = WeatherApiService(context)

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = requestAPI.getCurrentWeather(city).await()
                responseLiveData.postValue(response)
            }catch (e: Exception){
                e.printStackTrace()
                errorMessage.postValue(e.message.toString())
            }


        }

    }
}