package com.octantis.prime.android.util.utilsmain.run

import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.WindowInsetsCompat

object EditUtils {
    private var beforePhone = ""

    /**
     * 聚焦 Edit 并且显示输入法
     * @param editText EditText
     * @param activity Activity
     */
    fun showInput(editText: EditText, activity: Activity) {
        editText.post {
            editText.requestFocus()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                editText.windowInsetsController?.show(WindowInsetsCompat.Type.ime())
            } else {
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editText, 0)
            }
        }
    }

    /**
     * 格式化为普通手机号
     * @param phone String
     * @return String
     */
    fun toPhoneType(phone: String): String {
        var lp = getNumber(phone)
        if (lp.startsWith("0057")) {
            lp = lp.substring(4)
        } else if (lp.startsWith("057")) {
            lp = lp.substring(3)
        } else if (lp.startsWith("57")) {
            lp = lp.substring(2)
        } else if (lp.startsWith("+57")) {
            lp = lp.substring(3)
        } else if (lp.startsWith("+ 57")) {
            lp = lp.substring(4)
        } else if (lp.startsWith("+")) {
            lp = lp.substring(1)
        }
        return lp
    }

    /**
     * 提取数字
     * @param body String
     * @return String
     */
    private fun getNumber(body: String): String {
        return body.replace("[^0-9]".toRegex(), "")
    }

    /**
     * 基础手机号样式
     * xxx xxx xxxx
     * @param editText EditText
     */
    fun phoneType(editText: EditText) {
        val phoneTw: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Not
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Phone Head
                if (s.toString().trim().startsWith("0057") ||
                    s.toString().trim().startsWith("057") ||
                    s.toString().trim().startsWith("57") ||
                    s.toString().trim().startsWith("+57") ||
                    s.toString().trim().startsWith("+")
                ) {
                    editText.setText("")
                }
                // Phone Type
                if ((s.toString().length == 4 || s.toString().length == 8) && !s.toString()
                        .endsWith(" ")
                ) {
                    val ss = StringBuilder(s.toString())
                    ss.insert(ss.length - 1, " ")
                    editText.setText(ss.toString())
                    editText.setSelection(ss.length)
                }
                if (s.toString().endsWith(" ")) {
                    val ss = s.toString().trim { it <= ' ' }
                    editText.setText(ss)
                    editText.setSelection(ss.length)
                }


            }

            override fun afterTextChanged(s: Editable) {
                // Not
            }
        }
        editText.addTextChangedListener(phoneTw)
    }

    /**
     * 手机号转联系人手机号码
     * @param editText EditText
     */
    fun phoneTypeOfContact(editText: EditText) {
        val phoneTw: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                beforePhone = s.toString()
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Phone Head
                if (s.toString().trim().startsWith("0057") ||
                    s.toString().trim().startsWith("057") ||
                    s.toString().trim().startsWith("57") ||
                    s.toString().trim().startsWith("+57") ||
                    s.toString().trim().startsWith("+")
                ) {
                    editText.setText("")
                }
                // Phone Type
                if ((s.toString().length == 4 || s.toString().length == 8) && !s.toString()
                        .endsWith(" ")
                ) {
                    val ss = StringBuilder(s.toString())
                    ss.insert(ss.length - 1, " ")
                    editText.setText(ss.toString())
                    editText.setSelection(ss.length)
                }

                if (s.toString().endsWith(" ")) {
                    val ss = s.toString().trim { it <= ' ' }
                    editText.setText(ss)
                    editText.setSelection(ss.length)
                }
                val beforeLength = (beforePhone.replace(" ", "")).length
                val textLength = (s.toString().replace(" ", "")).length

                if (beforeLength in 10..<textLength) {
                    val ss = StringBuilder(s.toString())
                    ss.deleteCharAt(ss.length - 1)
                    editText.setText(ss.toString())
                    editText.setSelection(ss.length)
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Not
            }
        }
        editText.addTextChangedListener(phoneTw)
    }

    /**
     * 基础银行卡格式
     * @param editText EditText
     */
    fun bankType(editText: EditText) {
        val phoneTw: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Not
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Not
                if (s.toString().length > 1 && s.toString().length % 5 == 0 && !s.toString()
                        .endsWith(" ")
                ) {
                    val ss = java.lang.StringBuilder(s.toString())
                    ss.insert(ss.length - 1, " ")
                    editText.setText(ss.toString())
                    editText.setSelection(ss.length)
                }
                if (s.toString().endsWith(" ")) {
                    val ss = s.toString().trim { it <= ' ' }
                    editText.setText(ss)
                    editText.setSelection(ss.length)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        }
        editText.addTextChangedListener(phoneTw)
    }
}