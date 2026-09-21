package com.example.phim.network

import com.example.phim.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//singleton object shared throughout app
//basically handles networking with TMDB
object TmdbClient {

    //root of every TMDB request
    //combine with query from TmdbApi.kt
    private const val BASE_URL =
        "https://api.themoviedb.org/3/"

    private val httpClient =
        //OkHttp handles HTTP networking under retrofit
        //interceptor is what allows modifications to an http request
        //chain.request() == original request
        OkHttpClient.Builder().addInterceptor { chain ->
                val request =
                    chain
                        .request()
                        //new builder creates a modified version of it
                        //the new version will include the API token, expect the response in json format
                        //and then build
                        .newBuilder()
                        .addHeader(
                            "Authorization",
                            "Bearer ${BuildConfig.TMDB_ACCESS_TOKEN}"
                        )
                        .addHeader(
                            "accept",
                            "application/json"
                        )
                        .build()
            //now actually continue with this request
                chain.proceed(request)
            }
            .build()

    //actual object that my repository will use
    //lazy == dont build immediately, build only when api is called for the first time
    //this is so that if TMDB is never called, not make an unnecessary object
    val api: TmdbApi by lazy {

        //using httpClient from earlier,
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            //tells retrofit how to change query result into kotlin object
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(TmdbApi::class.java)
    }
}