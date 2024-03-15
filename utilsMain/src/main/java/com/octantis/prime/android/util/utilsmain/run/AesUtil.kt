package com.octantis.prime.android.util.utilsmain.run

import android.annotation.SuppressLint
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object AesUtil {
    private const val HEX = "0123456789ABCDEF"
    private var AesKey = ""

    fun initAesKey(key: String) {
        this.AesKey = key
    }

    fun encrypt(cleartext: String): String? {
        try {
            val rawKey: ByteArray = AesKey.toByteArray()
            val result = encrypt(rawKey, cleartext.toByteArray())
            return toHex(result)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @SuppressLint("GetInstance")
    @Throws(Exception::class)
    private fun encrypt(raw: ByteArray, clear: ByteArray): ByteArray {
        val sKeySpec =
            SecretKeySpec(raw, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec)
        return cipher.doFinal(clear)
    }

    fun decrypt(encrypted: String): String? {
        try {
            val rawKey: ByteArray = AesKey.toByteArray()
            val enc = fromHexToBytes(encrypted)
            val result = decrypt(rawKey, enc)
            return String(result)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @SuppressLint("GetInstance")
    @Throws(Exception::class)
    private fun decrypt(raw: ByteArray, encrypted: ByteArray): ByteArray {
        val sKeySpec =
            SecretKeySpec(raw, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec)
        return cipher.doFinal(encrypted)
    }

    private fun fromHexToBytes(hexString: String): ByteArray {
        val len = hexString.length / 2
        val result = ByteArray(len)
        for (i in 0 until len) result[i] = Integer.valueOf(
            hexString.substring(2 * i, 2 * i + 2),
            16
        ).toByte()
        return result
    }

    private fun toHex(buf: ByteArray?): String {
        if (buf == null) return ""
        val result = StringBuffer(2 * buf.size)
        for (i in buf.indices) {
            appendHex(result, buf[i])
        }
        return result.toString()
    }


    private fun appendHex(sb: StringBuffer, b: Byte) {
        sb.append(HEX[b.toInt() shr 4 and 0x0f]).append(HEX[b.toInt() and 0x0f])
    }
}