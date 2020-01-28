package com.examwhitelabel.di

import com.examwhitelabel.data.database.TheDatabaseProvider
import com.examwhitelabel.data.networking.TheNetworkingProvider
import com.examwhitelabel.data.websocket.TheWebSocketProvider
import org.koin.dsl.module

object DataModule {

    val module = module {
        single { TheDatabaseProvider.provideTheDatabaseInterface(get()) }
        single { TheNetworkingProvider.provideOkHttpClient() }
        single { TheNetworkingProvider.provideGson() }
        single { TheNetworkingProvider.provideTheNetworkingInterface(get(), get()) }
        single { TheWebSocketProvider.provideTheWebSocketInterface(get(), get()) }
    }
}
