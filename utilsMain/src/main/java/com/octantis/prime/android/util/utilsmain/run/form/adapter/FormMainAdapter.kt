package com.octantis.prime.android.util.utilsmain.run.form.adapter

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Adapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import com.octantis.prime.android.util.utilsmain.R
import com.octantis.prime.android.util.utilsmain.run.inf.BackMMM
import com.octantis.prime.android.util.utilsmain.run.main.MainAdapter
import com.octantis.prime.android.util.utilsmain.run.type.MML
import com.octantis.prime.android.util.utilsmain.run.type.MMM

@Suppress("UNCHECKED_CAST")
abstract class FormMainAdapter<V : ViewDataBinding>(
    private val context: Context,
    private val data: MML
) : MainAdapter<V>(data) {
    private lateinit var dataBack: BackMMM
    private lateinit var backListener: BackInfo
    private lateinit var buildListener: BackBuildMML
    private lateinit var bankNameListener: BackBankName
    private var colorUnMain = 0
    private var colorBlack = 0
    private lateinit var rootView: View
    private lateinit var iconView: ImageView
    private lateinit var titleView: TextView
    private lateinit var selectView: TextView
    private lateinit var rvView: RecyclerView
    private lateinit var rgView: RadioGroup
    private lateinit var rgLView: RadioButton
    private lateinit var rgRView: RadioButton
    private lateinit var editView: EditText
    private lateinit var bankView: TextView
    private var workInfo: MutableList<*>? = null

    fun initLayoutView(
        rootView: View,
        iconView: ImageView,
        titleView: TextView,
        selectView: TextView,
        rvView: RecyclerView,
        rgView: RadioGroup,
        rgLView: RadioButton,
        rgRView: RadioButton,
        editView: EditText,
        bankView: TextView
    ) {
        this.rootView = rootView
        this.iconView = iconView
        this.titleView = titleView
        this.selectView = selectView
        this.rvView = rvView
        this.rgView = rgView
        this.rgLView = rgLView
        this.rgRView = rgRView
        this.editView = editView
        this.bankView = bankView
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    private fun setView(
        itemData: MutableMap<String, Any>,
        itemValue: String,
        itemVisible: String,
        position: Int
    ) {
        val type = itemData["type"] as String? ?: ""
        val id = itemData["id"] as String? ?: ""

        if (id.contains("payDate")) {
            if (id.contains("type")) {
                rootView.setPadding(0, 5, 0, 0)
            } else {
                rootView.setPadding(0, 0, 0, 0)
            }
        }

        when (type) {
            /**
             * 选择框
             */
            "select" -> {
                if (id == "payDate.weekDay") {
                    if (itemVisible == "V") {
                        selectView.visibility = RecyclerView.VISIBLE
                        iconView.visibility = RecyclerView.VISIBLE
                        titleView.visibility = RecyclerView.VISIBLE
                    } else if (itemVisible.isEmpty()) {
                        selectView.visibility = RecyclerView.GONE
                        iconView.visibility = RecyclerView.GONE
                        titleView.visibility = RecyclerView.GONE
                        setVisible(position, "G", false)
                    } else {
                        selectView.visibility = RecyclerView.GONE
                        iconView.visibility = RecyclerView.GONE
                        titleView.visibility = RecyclerView.GONE
                    }
                } else if (id == "userBank.accountType") {
                    rgView.visibility = RecyclerView.VISIBLE
                    iconView.visibility = RecyclerView.VISIBLE
                    titleView.visibility = RecyclerView.VISIBLE
                    val showTitle = "Seleccione el tipo de cuenta bancaria"
                    titleView.text = showTitle
                    val opts = itemData["options"] as MutableList<*>?
                    val left = opts?.get(0) as MMM
                    val right = opts[1] as MMM
                    backListener.backInfo(id, left["id"] as String)
                    rgLView.setOnCheckedChangeListener { _, b ->
                        if (b) {
                            backListener.backInfo(id, left["id"] as String)
                        }
                    }

                    rgRView.setOnCheckedChangeListener { _, b ->
                        if (b) {
                            backListener.backInfo(id, right["id"] as String)
                        }
                    }
                } else {
                    selectView.visibility = RecyclerView.VISIBLE
                    iconView.visibility = RecyclerView.VISIBLE
                    titleView.visibility = RecyclerView.VISIBLE
                }

                if (itemValue.isNotEmpty()) {
                    selectView.text = itemValue
                    selectView.setTextColor(ContextCompat.getColor(context, colorBlack))
                } else {
                    selectView.setTextColor(ContextCompat.getColor(context, colorUnMain))
                }

                val name = itemData["name"] as String? ?: "Por favor, seleccione"

                onClick(
                    id = id,
                    name,
                    opts = itemData["options"] as MutableList<*>?,
                    position = position
                )
            }

            /**
             * 输入框
             */
            "text" -> {
                editView.visibility = RecyclerView.VISIBLE
                iconView.visibility = RecyclerView.VISIBLE
                titleView.visibility = RecyclerView.VISIBLE
                selectView.visibility = RecyclerView.GONE

                when (id) {
                    "socialAccounts.whatsapp" -> {
                        editView.inputType = InputType.TYPE_CLASS_PHONE
                        editView.filters += InputFilter.LengthFilter(14)
                    }

                    "userBank.bankCard" -> {
                        editView.inputType = InputType.TYPE_CLASS_NUMBER
                        editView.filters += InputFilter.LengthFilter(24)
                        bankView.visibility = View.VISIBLE
                        InputUtil.bankType(editView)
                    }
                }

                onEdit(
                    id = id,
                    position = position
                )
            }

            /**
             * 输入框 - Number
             */
            "number" -> {
                editView.visibility = RecyclerView.VISIBLE
                titleView.visibility = RecyclerView.VISIBLE
                iconView.visibility = RecyclerView.VISIBLE
                editView.inputType = InputType.TYPE_CLASS_NUMBER

                //      LogUtils.e(id)

                if (id == "monthIncome") {
                    editView.filters += arrayOf(InputFilter.LengthFilter(10))
                }

                if (itemValue.isNotEmpty()) {
                    editView.setText(itemValue)
                } else {
                    selectView.setHintTextColor(ContextCompat.getColor(context, colorUnMain))
                }

                onEdit(
                    id = id,
                    position = position
                )
            }

            /**
             * 工作选择
             */
            "jobType" -> {
                selectView.visibility = RecyclerView.VISIBLE
                titleView.visibility = RecyclerView.VISIBLE
                iconView.visibility = RecyclerView.VISIBLE

                if (itemValue.isNotEmpty()) {
                    selectView.text = itemValue
                    selectView.setTextColor(ContextCompat.getColor(context, colorBlack))
                } else {
                    selectView.setTextColor(ContextCompat.getColor(context, colorUnMain))
                }

                selectView.setOnClickListener {
                    showWorkDialog(workInfo)
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
                }
            }

            /**
             * 地址选择
             */
            "addressType" -> {
                titleView.visibility = View.VISIBLE
                iconView.visibility = RecyclerView.VISIBLE
                selectView.visibility = View.VISIBLE

                if (itemValue.isNotEmpty()) {
                    selectView.text = itemValue
                    selectView.setTextColor(ContextCompat.getColor(context, colorBlack))
                } else {
                    selectView.setTextColor(ContextCompat.getColor(context, colorUnMain))
                }

                selectView.setOnClickListener {
                    showAddressDialog()
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
                }
            }

            /**
             * 发薪日选择框
             */
            "day_select" -> {
                if (itemVisible == "V") {
                    selectView.visibility = View.VISIBLE
                    iconView.visibility = RecyclerView.VISIBLE
                    titleView.visibility = View.VISIBLE
                } else if (itemVisible.isEmpty()) {
                    setVisible(position, value = "G", notify = false)
                    selectView.visibility = RecyclerView.GONE
                    iconView.visibility = RecyclerView.GONE
                    titleView.visibility = RecyclerView.GONE
                } else {
                    selectView.visibility = RecyclerView.GONE
                    iconView.visibility = RecyclerView.GONE
                    titleView.visibility = RecyclerView.GONE
                }

                if (itemValue.isNotEmpty()) {
                    selectView.text = itemValue
                    selectView.setTextColor(ContextCompat.getColor(context, colorBlack))
                } else {
                    selectView.setTextColor(ContextCompat.getColor(context, colorUnMain))
                }

                val dateList = mutableListOf<MutableMap<String, Any>>()

                for (i in 1 until 32) {
                    val dateItem = mutableMapOf<String, Any>()
                    dateItem["name"] = "$i"
                    dateItem["value"] = "$i"
                    dateList.add(dateItem)
                }


                //        LogUtils.e(id)

                val title = itemData["name"] as String? ?: ""
//                when (id) {
//                    "payDate.monthDay" -> {
//                        title = "Frecuencia de la nómina"
//                    }
//
//                    "payDate.secondMonthDay" -> {
//                        title = "Frecuencia de la nómina"
//                    }
//                }

                onClick(
                    id = id,
                    name = title,
                    opts = dateList,
                    position = position
                )
            }
        }
    }


    /**
     * 实现基础点击事件的Dialog
     * 需实现 doingClickFun方法
     *
     * @param context Context
     * @param data MutableList<*>
     * @param title String
     * @param id String
     */
    abstract fun showClickDialog(
        context: Context,
        data: MML,
        listData: MutableList<*>?,
        position: Int,
        title: String,
        id: String
    )

    fun doingClickFun(data: MML, id: String, info: MMM, position: Int) {
        if (id == "payDate.type") {
            when (info["value"] as String? ?: "") {
                "WEEK" -> {
                    for (i in data.indices) {
                        val itemData = data[i]
                        when (itemData["id"]) {
                            "payDate.weekDay" -> {
                                setTitle(i, "Día de pago")
                                setVisible(i, "V", true)
                            }

                            "payDate.monthDay" -> {
                                setVisible(i, "G", true)
                            }

                            "payDate.secondMonthDay" -> {
                                setVisible(i, "G", true)
                            }
                        }
                    }
                }

                "HALF_MONTH" -> {
                    for (i in data.indices) {
                        val itemData = data[i]
                        when (itemData["id"]) {
                            "payDate.weekDay" -> {
                                setVisible(i, "G", true)
                            }

                            "payDate.monthDay" -> {
                                setTitle(i, "El primer día de pago")
                                setVisible(i, "V", true)
                            }

                            "payDate.secondMonthDay" -> {
                                setTitle(i, "El segundo día de pago")
                                setVisible(i, "V", true)
                            }
                        }
                    }
                }

                "NEXT_WEEK" -> {
                    for (i in data.indices) {
                        val itemData = data[i]
                        when (itemData["id"]) {
                            "payDate.weekDay" -> {
                                setTitle(i, "Día de pago")
                                setVisible(i, "V", true)
                            }

                            "payDate.monthDay" -> {
                                setVisible(i, "G", true)
                            }

                            "payDate.secondMonthDay" -> {
                                setVisible(i, "G", true)
                            }
                        }
                    }
                }

                "MONTH" -> {
                    for (i in data.indices) {
                        val itemData = data[i]
                        when (itemData["id"]) {
                            "payDate.weekDay" -> {
                                setVisible(i, "G", true)
                            }

                            "payDate.monthDay" -> {
                                setTitle(i, "Día de pago")
                                setVisible(i, "V", true)
                            }

                            "payDate.secondMonthDay" -> {
                                setVisible(i, "G", true)
                            }
                        }
                    }
                }
            }
        }
        var backInfo = info["value"] as String? ?: ""
        if (backInfo == "") {
            backInfo = info["id"] as String? ?: ""
        }

        if (id == "userBank.bankCode") {
            bankNameListener.backName(info["name"] as String? ?: "")
        }

        backListener.backInfo(id, backInfo)
        setData(position, info["name"] as String? ?: "", true)
    }

    /**
     * OnClickListener
     */
    private fun onClick(id: String, name: String, opts: MutableList<*>?, position: Int) {
        // select 类型事件
        selectView.setOnClickListener {
            showClickDialog(context, data, opts, position, name, id)
        }
    }



    abstract fun initEmailAdapter():RecyclerView.Adapter<*>


    /**
     * 实现到 Edit 的封装
     * @param id String
     * @param position Int
     */

    这里开始

    private fun onEdit(id: String, position: Int) {
        // 防止崩溃
        editView.imeOptions = EditorInfo.IME_ACTION_DONE
        editView.setOnEditorActionListener { _, _, _ -> true }
        editView.setOnClickListener {
            // Not
        }

        when (id) {
            "email" -> {
                val emailRv = rvView
                val emailEdt = editView
                val emailWatcher = object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        // Not
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (p0 != null) {
                            val text = p0.toString().trim()
                            backListener.backInfo(id, text)
                            setData(position, text, false)
                            if (!RegexUtils.isEmail(text) && text
                                    .isNotEmpty() && !text.contains("@")
                            ) {
                                emailRv.visibility = View.VISIBLE
//                                val adapter = EmailAdapter(text, CCC.email)
//                                adapter.getEmailInfo(object : EmailAdapter.EmailBack {
//                                    override fun email(emailText: String) {
//                                        val manager =
//                                            context.getSystemService(Context.INPUT_METHOD_SERVICE)
//                                                    as InputMethodManager
//                                        manager.hideSoftInputFromWindow(emailEdt.windowToken, 0)
//
//                                        emailEdt.setText(emailText)
//                                        backListener.backInfo(id, emailText)
//                                        setData(position, emailText, false)
//                                        emailRv.visibility = View.GONE
//                                        emailEdt.clearFocus()
//                                    }
//                                })
                                emailRv.adapter = initEmailAdapter()
                            } else {
                                emailRv.visibility = View.GONE
                            }
                        } else {
                            backListener.backInfo(id, "")
                            setData(position, "", false)
                        }
                    }

                    override fun afterTextChanged(p0: Editable?) {
                        // Not
                    }
                }

                emailEdt.addTextChangedListener(emailWatcher)
            }

            else -> {
                val textListener = object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        // Not
                    }

                    override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (text != null) {
                            if (id == "userBank.bankCard") {
                                setData(position, text.toString().replace(" ", ""), false)
                                backListener.backInfo(id, text.toString().replace(" ", ""))
                            } else {
                                setData(position, text.toString(), false)
                                backListener.backInfo(id, text.toString())
                            }
                        }
                    }

                    override fun afterTextChanged(p0: Editable?) {
                        // Not
                    }
                }
                editView.addTextChangedListener(textListener)
            }
        }
    }


    /**
     * 实现工作信息 Dialog
     * @param workInfo MutableList<*>? 缓存 Work 信息
     */
    abstract fun showWorkDialog(workInfo: MutableList<*>?)


    /**
     * 实现地址信息 Dialog
     */
    abstract fun showAddressDialog()


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

    fun setData(data: BackMMM) {
        this.dataBack = data
    }

    /**
     * 资源ID
     * @param unMain Int
     * @param black Int
     */
    fun initShowAsset(unMain: Int, black: Int) {
        this.colorUnMain = unMain
        this.colorBlack = black
    }

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
     * 设置可见状态
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