package com.example.fcast.data

import android.content.Context
import com.example.fcast.data.Responce.ResponseData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "c7b2a582ab13478994f43132192805"

//HTTP: http://api.apixu.com/v1/current.json?key=c7b2a582ab13478994f43132192805&q=Paris

interface WeatherApiService {
    @GET("current.json")
    fun getCurrentWeather(@Query("q") location:String,
                          @Query("lang") languageCode:String = "eng"
    ): Deferred<ResponseData>

    companion object {
        operator fun invoke(context: Context):WeatherApiService{
            val requesrInterceptor = Interceptor{ chain ->
                val url = chain.request().url().newBuilder().addQueryParameter("key", API_KEY).build()
                val request = chain.request().newBuilder().url(url).build()

                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requesrInterceptor)
                .setCookieStore(context)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://api.apixu.com/v1/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService::class.java)

        }

    }
}