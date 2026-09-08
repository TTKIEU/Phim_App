package com.example.phim.network

import com.example.phim.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TmdbClient {

    private const val BASE_URL =
        "https://api.themoviedb.org/3/"

    private val httpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->

                val request =
                    chain
                        .request()
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

                chain.proceed(request)
            }
            .build()

    val api: TmdbApi by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(TmdbApi::class.java)
    }
}