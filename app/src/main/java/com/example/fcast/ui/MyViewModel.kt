package com.example.fcast.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.example.fcast.data.Responce.ResponseData
import com.example.fcast.data.WeatherApiService
import kotlinx.coroutines.*
import java.lang.StringBuilder

class MyViewModel : ViewModel(){


    var responseLiveData = MutableLiveData<ResponseData>()
    var errorMessage = MutableLiveData<String>()
    var timevariable =MutableLiveData<Int>()
     var job = SupervisorJob()

     fun requestWeather(city: String,context: Context) {
         job =  CoroutineScope(Dispatchers.Default).launch {
             startTimer()

         }

        var requestAPI = WeatherApiService(context)

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = requestAPI.getCurrentWeather(city).await()
                responseLiveData.postValue(response)
                job.cancel()

            }catch (e: Exception){
                e.printStackTrace()
                errorMessage.postValue(e.message.toString())
                job.cancel()

            }


        }

    }

    suspend fun startTimer(){
        var count = 0
        timevariable.postValue(count)
        while (count<Int.MAX_VALUE){
            timevariable.postValue(count++)
        }
    }

}
