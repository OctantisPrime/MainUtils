package com.octantis.prime.android.util.utilsmain.run.form

import com.octantis.prime.android.util.utilsmain.run.type.MML
import com.octantis.prime.android.util.utilsmain.run.type.MMM

class FormUtils {
    companion object {
        /**
         * 构建表单提交 Map
         * 根据表单返回的 info ，通过 ID 构建提交的 Map
         */
        fun buildFormSubMap(formData: MutableList<*>, contactCount: Int?): MMM {
            val formSubMap = mutableMapOf<String, Any>()
            for (i in 0 until formData.size) {
                val item = formData[i] as MutableMap<*, *>
                val keyName = item["id"] as String? ?: ""
                val type = item["type"] as String? ?: ""
                val keyList = keyName.split(".")

                // 联系人单独构建
                if (type == "contact") {
                    val contactBody = mutableListOf<MutableMap<String, Any>>()
                    for (j in 0 until (contactCount ?: 2)) {
                        val contactEmpty = mutableMapOf<String, Any>()
                        contactEmpty["name"] = ""
                        contactEmpty["phone"] = ""
                        contactEmpty["relation"] = ""
                        contactBody.add(contactEmpty)
                    }
                    formSubMap[keyList[0]] = contactBody
                } else {
                    var currentLevel = formSubMap
                    for (key in keyList) {
                        if (key != keyList.last()) {
                            currentLevel[key] = mutableMapOf<String, Any>()
                            currentLevel = currentLevel[key] as MMM
                        } else {
                            currentLevel[key] = ""
                        }
                    }
                }
            }
            return formSubMap
        }

        /**
         * 向表单提交Map插入数据
         * 通过 ID 与 Key 插入 Value
         * ( 目前只实现了三层 )
         */
        fun insertValueInFormMap(
            subFormInfo: MutableMap<String, Any>,
            idName: String,
            info: Any
        ): MutableMap<String, Any> {
            val idList = idName.split(".")
            when (idList.size) {
                1 -> {
                    subFormInfo[idList[0]] = info
                }

                2 -> {
                    val itemMap = subFormInfo[idList[0]] as MMM
                    itemMap[idList[1]] = info
                    subFormInfo[idList[0]] = itemMap
                }

                3 -> {
                    val itemMap = subFormInfo[idList[0]] as MMM
                    val childMap = itemMap[idList[1]] as MMM
                    childMap[idList[2]] = info
                    itemMap[idList[1]] = childMap
                    subFormInfo[idList[0]] = itemMap
                }
            }
            return subFormInfo
        }


        interface CheckFormBack {
            fun empty(msg: String)
            fun success(result: MMM?)
        }

        /**
         * 检测表单 Map
         *
         * 空判定以及必须提交判定，
         */
        fun checkFormMap(
            subFormInfo: MutableMap<String, Any>,
            formData: MutableList<*>,
            back: CheckFormBack
        ) {
            for (i in 0 until formData.size) {
                val item = formData[i] as MutableMap<*, *>
                val id = item["id"] as String? ?: ""
                val required = item["required"] as Boolean? ?: true
                val idList = id.split(".")
                when (idList.size) {
                    1 -> {

                        val value = subFormInfo[idList[0]]
                        if (idList[0] == "userEmergs") {
                            val contactBody = value as MML
                            for (j in contactBody.indices) {
                                (contactBody[j]["name"] as String?).apply {
                                    if (this.isNullOrEmpty()) {
                                        back.empty("Contacto de emergencia ${j + 1} Nombre completo está vacío")
                                    }
                                }
                                (contactBody[j]["phone"] as String?).apply {
                                    if (this.isNullOrEmpty()) {
                                        back.empty("Contacto de emergencia ${j + 1} Teléfono está vacío")
                                    }
                                }
                                (contactBody[j]["relation"] as String?).apply {
                                    if (this.isNullOrEmpty()) {
                                        back.empty("Contacto de emergencia ${j + 1} Relación está vacío")
                                    }
                                }
                            }
                        } else {
                            if (value == null || value.toString().isEmpty() && required) {
                                back.empty("${item["name"]} está vacío")
                            }
                        }
                    }

                    2 -> {
                        val oneBody = subFormInfo[idList[0]] as MutableMap<*, *>
                        val value = oneBody[idList[1]]
                        // PayDateType
                        if (idList[0] == "payDate" && idList[1] != "type" && (oneBody["type"] as String).isNotEmpty()) {
                            when (oneBody["type"] as String) {
                                "WEEK" -> {
                                    when (idList[1]) {
                                        "weekDay" -> {
                                            if (value == null || value.toString().isEmpty()) {
                                                back.empty("${item["name"]} está vacío")
                                            } else {
                                                oneBody.remove("monthDay")
                                                oneBody.remove("secondMonthDay")
                                                subFormInfo["payDate"] = oneBody
                                            }
                                        }

                                        "monthDay" -> {
                                            oneBody.remove("monthDay")
                                            subFormInfo["payDate"] = oneBody
                                        }

                                        "secondMonthDay" -> {
                                            oneBody.remove("secondMonthDay")
                                            subFormInfo["payDate"] = oneBody
                                        }
                                    }
                                }

                                "HALF_MONTH" -> {
                                    when (idList[1]) {
                                        "weekDay" -> {
                                            oneBody.remove("weekDay")
                                            subFormInfo["payDate"] = oneBody
                                        }

                                        "monthDay" -> {
                                            if (value == null || value.toString().isEmpty()) {
                                                back.empty("${item["name"]} está vacío")
                                            } else {
                                                oneBody.remove("weekDay")
                                                subFormInfo["payDate"] = oneBody
                                            }
                                        }

                                        "secondMonthDay" -> {
                                            if (value == null || value.toString().isEmpty()) {
                                                back.empty("${item["name"]} está vacío")
                                            } else {
                                                oneBody.remove("weekDay")
                                                subFormInfo["payDate"] = oneBody
                                            }
                                        }
                                    }
                                }

                                "NEXT_WEEK" -> {
                                    when (idList[1]) {
                                        "weekDay" -> {
                                            if (value == null || value.toString().isEmpty()) {
                                                back.empty("${item["name"]} está vacío")
                                            } else {
                                                oneBody.remove("monthDay")
                                                oneBody.remove("secondMonthDay")
                                                subFormInfo["payDate"] = oneBody
                                            }
                                        }

                                        "monthDay" -> {
                                            oneBody.remove("monthDay")
                                            subFormInfo["payDate"] = oneBody
                                        }

                                        "secondMonthDay" -> {
                                            oneBody.remove("secondMonthDay")
                                            subFormInfo["payDate"] = oneBody
                                        }
                                    }
                                }

                                "MONTH" -> {
                                    when (idList[1]) {
                                        "weekDay" -> {
                                            oneBody.remove("weekDay")
                                            subFormInfo["payDate"] = oneBody
                                        }

                                        "monthDay" -> {
                                            if (value == null || value.toString().isEmpty()) {
                                                back.empty("${item["name"]} está vacío")
                                            } else {
                                                oneBody.remove("weekDay")
                                                oneBody.remove("secondMonthDay")
                                                subFormInfo["payDate"] = oneBody
                                            }
                                        }

                                        "secondMonthDay" -> {
                                            oneBody.remove("secondMonthDay")
                                            subFormInfo["payDate"] = oneBody
                                        }
                                    }
                                }
                            }

                        } else if (id == "socialAccounts.whatsapp") {
                            if (value == null || value.toString().isEmpty()) {
                                back.empty("${item["name"]} está vacío")
                            }
                            if (!(value.toString().trim().length == 10
                                        || value.toString().trim().length == 12
                                        || value.toString().trim().length == 14
                                        )
                            ) {
                                back.empty("${item["name"]} Formato incorrecto")
                            }
                        } else {
                            if (value == null || value.toString().isEmpty() && required) {
                                back.empty("${item["name"]} está vacío")
                            }
                        }
                    }

                    3 -> {
                        val oneBody = subFormInfo[idList[0]] as MutableMap<*, *>
                        val twoBody = oneBody[idList[1]] as MutableMap<*, *>
                        val value = twoBody[idList[2]]
                        if (value == null || value.toString().isEmpty() && required) {
                            back.empty("${item["name"]} está vacío")
                        }
                    }
                }
            }
            back.success(subFormInfo)
        }
    }
}