package com.octantis.prime.android.util.utilsmain.run.http

import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.JsonUtils
import com.octantis.prime.android.util.utilsmain.run.AesUtil
import com.octantis.prime.android.util.utilsmain.run.type.MMM

object HttpUtil {
    private var OK = 0
    private var RELOGIG = 1012
    private const val FAIL_MESSAGE = "Server request failed"

    fun httpUtilInit(ok: Int, reLogin: Int) {
        this.OK = ok
        this.RELOGIG = reLogin
    }

    interface StatusBack {
        fun ok(back: MMM)
        fun reLogin(msg: String)
        fun error(msg: String)
    }

    fun dealEvent(resultInfo: String, inf: StatusBack) {
        var result: String? = resultInfo.trim()
        // 清除两端异常
        if (result != null) {
            if (!result.startsWith("{")) {
                result = result.replace("\"", "")
                result = AesUtil.decrypt(result)
            }
        }

        if (result != null) {
            // 判断 Status
            val status = JsonUtils.getInt(result, "status")
            val message = if (result.contains("message")) JsonUtils.getString(
                result,
                "message"
            ) else ""
            when (status) {
                OK -> {
                    inf.ok(JSON.parse(result) as MMM)
                }

                RELOGIG -> {
                    inf.reLogin(message)
                }

                else -> {
                    inf.error(message)
                }
            }
        } else {
            inf.error(FAIL_MESSAGE)
        }
    }
}