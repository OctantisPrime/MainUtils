package com.octantis.prime.android.util.mainutils

import android.app.Application
import com.octantis.prime.android.util.utilsmain.run.init.MainInit

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MainInit.initAes("4tt4Wdacntkc8J88").initHttp(0, 1012)
    }

}