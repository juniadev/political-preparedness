package com.example.android.politicalpreparedness.network

import okhttp3.OkHttpClient
import java.io.FileInputStream
import java.util.Properties

class CivicsHttpClient: OkHttpClient() {

    companion object {

        fun getClient(): OkHttpClient {
            val localProperties = Properties()
            localProperties.load(FileInputStream("local.properties"))
            val apiKey = localProperties.getProperty("civicsApiKey")

            return Builder()
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val url = original
                                .url()
                                .newBuilder()
                                .addQueryParameter("key", apiKey)
                                .build()
                        val request = original
                                .newBuilder()
                                .url(url)
                                .build()
                        chain.proceed(request)
                    }
                    .build()
        }
    }
}