package com.examwhitelabel.data.websocket

import com.google.gson.Gson
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import okhttp3.OkHttpClient

object TheWebSocketProvider {

    fun provideTheWebSocketInterface(okHttpClient: OkHttpClient, gson: Gson): TheWebSocketInterface = Scarlet.Builder()
        .webSocketFactory(okHttpClient.newWebSocketFactory(URL))
        .addMessageAdapterFactory(GsonMessageAdapter.Factory(gson))
        .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
        .build().create()

    private const val URL = "ws://10.0.2.2:8080"
}
