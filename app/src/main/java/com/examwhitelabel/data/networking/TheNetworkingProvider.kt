package com.examwhitelabel.data.networking

import com.examwhitelabel.data.TheItem
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

object TheNetworkingProvider {

    private interface TheNetworkingInterfaceAnnotated : TheNetworkingInterface {

        @POST("/item")
        override suspend fun addItem(@Body item: TheItem)

        @DELETE("/item/{id}")
        override suspend fun deleteItem(@Path("id") itemId: String)

        @GET("/item")
        override suspend fun getItems(): List<TheItem>

        @PUT("/item")
        override suspend fun updateItem(@Body item: TheItem)
    }

    fun provideOkHttpClient() = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()

    fun provideGson() = Gson()

    fun provideTheNetworkingInterface(okHttpClient: OkHttpClient, gson: Gson): TheNetworkingInterface = Retrofit.Builder()
        .baseUrl(URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create<TheNetworkingInterfaceAnnotated>()

    private const val URL = "http://10.0.2.2:8080"
}
