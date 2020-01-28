package com.examwhitelabel

import android.app.Application
import com.examwhitelabel.di.DataModule
import com.examwhitelabel.di.UtilModule
import com.examwhitelabel.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@Suppress("unused")
class TheApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@TheApplication)
            modules(ViewModelModule.module + DataModule.module + UtilModule.module)
        }
    }
}
