package com.examwhitelabel.di

import com.examwhitelabel.util.TheDispatchers
import com.examwhitelabel.util.TheDispatchersProductionImpl
import org.koin.dsl.module

object UtilModule {
    val module = module {
        single<TheDispatchers> { TheDispatchersProductionImpl }
    }
}
