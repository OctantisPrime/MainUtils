package com.octantis.prime.android.util.utilsmain.run.init

import com.octantis.prime.android.util.utilsmain.run.AesUtil
import com.octantis.prime.android.util.utilsmain.run.http.HttpUtil

object MainInit {
    fun initAes(key: String): MainInit {
        AesUtil.initAesKey(key)
        return this
    }

    fun initHttp(ok: Int, reLogin: Int): MainInit {
        HttpUtil.httpUtilInit(ok, reLogin)
        return this
    }
}