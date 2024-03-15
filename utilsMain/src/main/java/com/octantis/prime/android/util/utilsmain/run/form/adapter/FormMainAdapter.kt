package com.octantis.prime.android.util.utilsmain.run.form.adapter

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.octantis.prime.android.util.utilsmain.R
import com.octantis.prime.android.util.utilsmain.run.type.MML
import com.octantis.prime.android.util.utilsmain.run.type.MMM

abstract class FormMainAdapter(private val context: Context, private val data: MML) :
    Adapter<FormMainAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var dataBack: DataInfoBack
    private lateinit var v: ViewDataBinding
    private lateinit var backListener: BackInfo
    private lateinit var buildListener: BackBuildMML
    private lateinit var bankNameListener: BackBankName

    /**
     * 接口
     */
    fun backInfo(select: BackInfo) {
        this.backListener = select
    }

    fun backBankName(bankNameListener: BackBankName) {
        this.bankNameListener = bankNameListener
    }

    fun backBuildMML(buildListener: BackBuildMML) {
        this.buildListener = buildListener
    }

    /**
     *
     */
    fun initView(viewBinding: ViewDataBinding) {
        this.v = viewBinding
    }

    fun setData(data: DataInfoBack) {
        this.dataBack = data
    }

    abstract fun initView(parent: ViewGroup): ViewDataBinding

    abstract fun bindingView(holder: ViewHolder, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(initView(parent).root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindingView(holder, position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface DataInfoBack {
        fun back(info: MMM)
    }


//    private fun setView(
//        itemData: MutableMap<String, Any>,
//        itemValue: String,
//        itemVisible: String,
//        position: Int,
//        rootView: View,
//        selectView: TextView,
//        iconView: ImageView,
//        titleView: TextView,
//        radioGroup: RadioGroup?,
//        leftButton: RadioButton,
//        rightButton: RadioButton,
//    ) {
//        val type = itemData["type"] as String? ?: ""
//        val id = itemData["id"] as String? ?: ""
//
//        if (id.contains("payDate")) {
//            if (id.contains("type")) {
//                rootView.setPadding(0, 5, 0, 0)
//            } else {
//                rootView.setPadding(0, 0, 0, 0)
//            }
//        }
//
//        when (type) {
//            /**
//             * 选择框
//             */
//            "select" -> {
//                if (id == "payDate.weekDay") {
//                    if (itemVisible == "V") {
//                        selectView.visibility = RecyclerView.VISIBLE
//                        iconView.visibility = RecyclerView.VISIBLE
//                        titleView.visibility = RecyclerView.VISIBLE
//                    } else if (itemVisible.isEmpty()) {
//                        selectView.visibility = RecyclerView.GONE
//                        iconView.visibility = RecyclerView.GONE
//                        titleView.visibility = RecyclerView.GONE
//                        setVisible(position, "G", false)
//                    } else {
//                        selectView.visibility = RecyclerView.GONE
//                        iconView.visibility = RecyclerView.GONE
//                        titleView.visibility = RecyclerView.GONE
//                    }
//                } else if (id == "userBank.accountType") {
//                    radioGroup?.visibility = RecyclerView.VISIBLE
//                    iconView.visibility = RecyclerView.VISIBLE
//                    titleView.visibility = RecyclerView.VISIBLE
//                    val showTitle = "Seleccione el tipo de cuenta bancaria"
//                    titleView.text = showTitle
//                    val opts = itemData["options"] as MutableList<*>?
//                    val left = opts?.get(0) as MMM
//                    val right = opts[1] as MMM
//                    backListener.backInfo(id, left["id"] as String)
//                    leftButton.setOnCheckedChangeListener { _, b ->
//                        if (b) {
//                            backListener.backInfo(id, left["id"] as String)
//                        }
//                    }
//
//                    rightButton.setOnCheckedChangeListener { _, b ->
//                        if (b) {
//                            backListener.backInfo(id, right["id"] as String)
//                        }
//                    }
//                } else {
//                    selectView.visibility = RecyclerView.VISIBLE
//                    iconView.visibility = RecyclerView.VISIBLE
//                    titleView.visibility = RecyclerView.VISIBLE
//                }
//
//                if (itemValue.isNotEmpty()) {
//                    selectView.text = itemValue
//                    selectView.setTextColor(ContextCompat.getColor(context, R.color.black))
//                } else {
//                    selectView.setTextColor(ContextCompat.getColor(context, R.color.unMain))
//                }
//
//                val name = itemData["name"] as String? ?: "Por favor, seleccione"
////                LogUtils.json(itemData)
//                onClick(
//                    id = id,
//                    name,
//                    opts = itemData["options"] as MutableList<*>?,
//                    position = position
//                )
//            }
//
//            /**
//             * 输入框
//             */
//            "text" -> {
//                b.text.visibility = RecyclerView.VISIBLE
//                iconView.visibility = RecyclerView.VISIBLE
//                titleView.visibility = RecyclerView.VISIBLE
//                selectView.visibility = RecyclerView.GONE
//
//                when (id) {
//                    "socialAccounts.whatsapp" -> {
//                        b.text.inputType = InputType.TYPE_CLASS_PHONE
//                        b.text.filters += InputFilter.LengthFilter(14)
//                    }
//
//                    "userBank.bankCard" -> {
//                        b.text.inputType = InputType.TYPE_CLASS_NUMBER
//                        b.text.filters += InputFilter.LengthFilter(24)
//                        b.bankTip.visibility = View.VISIBLE
//                        InputUtil.bankType(b.text)
//                    }
//                }
//
//                onEdit(
//                    id = id,
//                    position = position
//                )
//            }
//
//            /**
//             * 输入框 - Number
//             */
//            "number" -> {
//                b.text.visibility = RecyclerView.VISIBLE
//                titleView.visibility = RecyclerView.VISIBLE
//                iconView.visibility = RecyclerView.VISIBLE
//                b.text.inputType = InputType.TYPE_CLASS_NUMBER
//
//                //      LogUtils.e(id)
//
//                if (id == "monthIncome") {
//                    b.text.filters += arrayOf(InputFilter.LengthFilter(10))
//                }
//
//                if (itemValue.isNotEmpty()) {
//                    b.text.setText(itemValue)
//                } else {
//                    selectView.setHintTextColor(ContextCompat.getColor(context, R.color.unMain))
//                }
//
//                onEdit(
//                    id = id,
//                    position = position
//                )
//            }
//
//            /**
//             * 工作选择
//             */
//            "jobType" -> {
//                selectView.visibility = RecyclerView.VISIBLE
//                titleView.visibility = RecyclerView.VISIBLE
//                iconView.visibility = RecyclerView.VISIBLE
//
//                if (itemValue.isNotEmpty()) {
//                    selectView.text = itemValue
//                    selectView.setTextColor(ContextCompat.getColor(context, R.color.black))
//                } else {
//                    selectView.setTextColor(ContextCompat.getColor(context, R.color.unMain))
//                }
//
//                selectView.setOnClickListener {
//
//                    if (workInfo == null) {
//                        MyUI.uiRun {
//                            loadingDialog.show()
//                        }
//                        Httt.post(AAA.getWorkInfo, mutableMapOf(), object : CB {
//                            override fun fail(msg: String) {
//                                MyUI.uiRun {
//                                    loadingDialog.dismiss()
//                                    ToastUtils.showShort(msg)
//                                }
//                            }
//
//                            override fun success(body: MutableMap<*, *>) {
//                                MyUI.uiRun {
//                                    loadingDialog.dismiss()
//                                    workInfo = body["model"] as MutableList<*>
//                                    val dialog = WorkDialog(context, workInfo!!)
//                                    dialog.workSelect(object : WorkDialog.WorkSelect {
//                                        override fun backInfo(
//                                            workBody: MutableMap<String, Any>,
//                                            showName: String
//                                        ) {
//                                            backListener.backInfo(id, workBody)
//                                            setData(position, showName, true)
//                                        }
//                                    })
//                                    dialog.show()
//                                }
//                            }
//                        })
//                    } else {
//                        val dialog = WorkDialog(context, workInfo!!)
//                        dialog.workSelect(object : WorkDialog.WorkSelect {
//                            override fun backInfo(
//                                workBody: MutableMap<String, Any>,
//                                showName: String
//                            ) {
//                                backListener.backInfo(id, workBody)
//                                setData(position, showName, true)
//                            }
//                        })
//                        dialog.show()
//                    }
//                }
//            }
//
//            /**
//             * 地址选择
//             */
//            "addressType" -> {
//                titleView.visibility = View.VISIBLE
//                iconView.visibility = RecyclerView.VISIBLE
//                selectView.visibility = View.VISIBLE
//
//                if (itemValue.isNotEmpty()) {
//                    selectView.text = itemValue
//                    selectView.setTextColor(ContextCompat.getColor(context, R.color.black))
//                } else {
//                    selectView.setTextColor(ContextCompat.getColor(context, R.color.unMain))
//                }
//
//                selectView.setOnClickListener {
//                    val addressDialog = AddressDialog(context)
//                    addressDialog.addressSelect(object : AddressDialog.AddressSelect {
//                        override fun backInfo(
//                            addressInfo: MutableMap<String, Any>,
//                            nameInfo: String
//                        ) {
//                            //      LogUtils.json(addressInfo)
//                            //     LogUtils.e(nameInfo)
//                            //      LogUtils.e(id)
//                            //       LogUtils.e(position)
//
//                            backListener.backInfo(id, addressInfo)
//                            setData(position, nameInfo, true)
//                        }
//                    })
//                    addressDialog.show()
//                }
//            }
//
//            /**
//             * 发薪日选择框
//             */
//            "day_select" -> {
////                LogUtils.e(id)
////                if (id == "payDate.monthDay" || id == "payDate.secondMonthDay") {
//
//                if (itemVisible == "V") {
//                    selectView.visibility = View.VISIBLE
//                    iconView.visibility = RecyclerView.VISIBLE
//                    titleView.visibility = View.VISIBLE
//                } else if (itemVisible.isEmpty()) {
//                    setVisible(position, value = "G", notify = false)
//                    selectView.visibility = RecyclerView.GONE
//                    iconView.visibility = RecyclerView.GONE
//                    titleView.visibility = RecyclerView.GONE
//                } else {
//                    selectView.visibility = RecyclerView.GONE
//                    iconView.visibility = RecyclerView.GONE
//                    titleView.visibility = RecyclerView.GONE
//                }
//
//                if (itemValue.isNotEmpty()) {
//                    selectView.text = itemValue
//                    selectView.setTextColor(ContextCompat.getColor(context, R.color.black))
//                } else {
//                    selectView.setTextColor(ContextCompat.getColor(context, R.color.unMain))
//                }
//
//                val dateList = mutableListOf<MutableMap<String, Any>>()
//
//                for (i in 1 until 32) {
//                    val dateItem = mutableMapOf<String, Any>()
//                    dateItem["name"] = "$i"
//                    dateItem["value"] = "$i"
//                    dateList.add(dateItem)
//                }
//
//
//                //        LogUtils.e(id)
//
//                val title = itemData["name"] as String? ?: ""
////                when (id) {
////                    "payDate.monthDay" -> {
////                        title = "Frecuencia de la nómina"
////                    }
////
////                    "payDate.secondMonthDay" -> {
////                        title = "Frecuencia de la nómina"
////                    }
////                }
//
//                onClick(
//                    id = id,
//                    name = title,
//                    opts = dateList,
//                    position = position
//                )
//            }
//        }
//    }

    /**
     * 设置 value
     * 往 Data 中直接插入 Value ， Key = "itemValue"
     * notify -> 是否更新 RecycleView
     */
    private fun setData(position: Int, value: String, notify: Boolean) {
        val itemData = data[position]
        itemData["itemValue"] = value
        if (notify) {
            notifyItemChanged(position)
        }
        buildListener.backInfo(data as MutableList<*>? ?: return)
    }

    /**
     * 设置 课件状态
     *
     * notify -> 是否更新整个 RecycleView
     */
    private fun setVisible(position: Int, value: String, notify: Boolean) {
        val itemData = data[position]
        LogUtils.json(itemData)
        itemData["itemVisible"] = value
        if (notify) {
            notifyItemChanged(position)
        }
        buildListener.backInfo(data as MutableList<*>? ?: return)
    }

    /**
     * 设置 Item Title
     */
    private fun setTitle(position: Int, value: String) {
        val itemData = data[position]
        itemData["name"] = value
        buildListener.backInfo(data as MutableList<*>? ?: return)
    }

    /**
     * 返回新构筑的 MML
     */
    interface BackBuildMML {
        fun backInfo(buildInfo: MutableList<*>)
    }

    /**
     * 返回基础 Value
     */
    interface BackInfo {
        fun backInfo(id: String, value: Any)
    }

    /**
     * 返回点击的 BankName Value
     */
    interface BackBankName {
        fun backName(name: String)
    }
}