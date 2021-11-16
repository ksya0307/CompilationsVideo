package com.example.clientvideos.api

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface VideosAPI {

    @Headers("Content-Type: application/json")
    @GET("login")
    fun auth(@Query("login") login: String, @Query("password") password: String): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET("videos")
    fun getAllVideos(): Call<List<Video>>

    @Headers("Content-Type: application/json")
    @GET("videos/preview")
    fun getPreview(@Query("title") title:String): Call<ResponseBody>

    @GET("videos/getVideo")
    fun getVideo(@Query("title") title:String):Call<ResponseBody>

}
class RetrofitInstance {
    companion object {
        private const val BASE_URL: String = "https://a1545fcc7c02.ngrok.io"

        private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        private val client: OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).apply {
            this.addInterceptor(interceptor)
        }.build()


        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

        }

    }
}
