package com.octantis.prime.android.util.utilsmain.run

import android.os.Handler
import android.os.Looper

object RunUtil {
    private val mainHandler = Handler(Looper.getMainLooper())
    fun m(runBody: Runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runBody.run()
        } else {
            mainHandler.post(runBody)
        }
    }

    fun t(runBody: Runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Thread {
                runBody.run()
            }.start()
        } else {
            runBody.run()
        }
    }
}