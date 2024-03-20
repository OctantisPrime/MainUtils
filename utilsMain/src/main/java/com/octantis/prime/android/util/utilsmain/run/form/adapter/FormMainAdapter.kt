package com.octantis.prime.android.util.utilsmain.run.form.adapter

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import com.octantis.prime.android.util.utilsmain.run.EditUtils
import com.octantis.prime.android.util.utilsmain.run.inf.BackMMM
import com.octantis.prime.android.util.utilsmain.run.main.MainAdapter
import com.octantis.prime.android.util.utilsmain.run.type.MML
import com.octantis.prime.android.util.utilsmain.run.type.MMM

@Suppress("UNCHECKED_CAST")
abstract class FormMainAdapter<V : ViewDataBinding>(
    private val context: Context, private val data: MML
) : MainAdapter<V>(data) {
    private lateinit var dataBack: BackMMM
    private lateinit var backListener: BackInfo
    private lateinit var buildListener: BackBuildMML
    private lateinit var bankNameListener: BackBankName
    private var colorOff = 0
    private var colorOn = 0
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

    /**
     * 绑定所需布局控件
     * @param rootView View
     * @param iconView ImageView
     * @param titleView TextView
     * @param selectView TextView
     * @param rvView RecyclerView
     * @param rgView RadioGroup
     * @param rgLView RadioButton
     * @param rgRView RadioButton
     * @param editView EditText
     * @param bankView TextView
     */
    protected fun initLayoutView(
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
    ): InitFormView {
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
        return InitFormView()
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        onViewInit(holder.bind)
        rvView.layoutManager = LinearLayoutManager(context)
        val itemData = data[position]
        val isRequired = itemData["required"] as Boolean? ?: true
        var title = itemData["name"] as String? ?: ""
        if (!isRequired) {
            title += " (Opcional)"
        }
        titleView.text = title
        val itemValue = itemData["itemValue"] as String? ?: ""
        val itemVisible = itemData["itemVisible"] as String? ?: ""
        initVisible()
        setView(itemData, itemValue, itemVisible, position)
        if (!initIcon(itemData["id"] as String? ?: "")) {
            iconView.visibility = View.GONE
        }
    }

    private fun setItemName(draId: Int?) {
        if (draId != null) {
            iconView.setImageDrawable(
                ContextCompat.getDrawable(
                    context, draId
                )
            )
        } else {
            iconView.visibility = RecyclerView.GONE
        }
    }

    private fun initVisible() {
        selectView.visibility = View.GONE
        editView.visibility = View.GONE
        bankView.visibility = View.GONE
        rvView.visibility = View.GONE
        iconView.visibility = View.GONE
        rgView.visibility = View.GONE
    }

    /**
     * 初始化 Adapter 各个 Item
     * @param itemData MutableMap<String, Any>  取 data 的 position Map
     * @param itemValue String                  显示 Text 的 Value
     * @param itemVisible String                是否显示
     * @param position Int
     */
    private fun setView(
        itemData: MutableMap<String, Any>, itemValue: String, itemVisible: String, position: Int
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
                    selectView.setTextColor(ContextCompat.getColor(context, colorOn))
                } else {
                    selectView.setTextColor(ContextCompat.getColor(context, colorOff))
                }

                val name = itemData["name"] as String? ?: "Por favor, seleccione"

                onClick(
                    id = id, name, opts = itemData["options"] as MML, position = position
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
                        EditUtils.bankType(editView)
                    }
                }

                onEdit(
                    id = id, position = position
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
                if (id == "monthIncome") {
                    editView.filters += arrayOf(InputFilter.LengthFilter(10))
                }

                if (itemValue.isNotEmpty()) {
                    editView.setText(itemValue)
                } else {
                    selectView.setHintTextColor(ContextCompat.getColor(context, colorOff))
                }

                onEdit(
                    id = id, position = position
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
                    selectView.setTextColor(ContextCompat.getColor(context, colorOn))
                } else {
                    selectView.setTextColor(ContextCompat.getColor(context, colorOff))
                }

                selectView.setOnClickListener {
                    val workBackInfo = showWorkDialog(workInfo)
                    backListener.backInfo(id, workBackInfo.info as Any)
                    setData(position, workBackInfo.name, true)
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
                    selectView.setTextColor(ContextCompat.getColor(context, colorOn))
                } else {
                    selectView.setTextColor(ContextCompat.getColor(context, colorOff))
                }

                selectView.setOnClickListener {
                    val addressInfo = showAddressDialog()
                    backListener.backInfo(id, addressInfo.info as Any)
                    setData(position, addressInfo.name, true)
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
                    selectView.setTextColor(ContextCompat.getColor(context, colorOn))
                } else {
                    selectView.setTextColor(ContextCompat.getColor(context, colorOff))
                }

                val dateList = mutableListOf<MutableMap<String, Any>>()

                for (i in 1 until 32) {
                    val dateItem = mutableMapOf<String, Any>()
                    dateItem["name"] = "$i"
                    dateItem["value"] = "$i"
                    dateList.add(dateItem)
                }
                val title = itemData["name"] as String? ?: ""
                onClick(
                    id = id, name = title, opts = dateList, position = position
                )
            }
        }
    }


    fun doingClickFun(id: String, dialogBackInfo: MMM, position: Int) {
        if (id == "payDate.type") {
            when (dialogBackInfo["value"] as String? ?: "") {
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
        var backInfo = dialogBackInfo["value"] as String? ?: ""
        if (backInfo == "") {
            backInfo = dialogBackInfo["id"] as String? ?: ""
        }

        if (id == "userBank.bankCode") {
            bankNameListener.backName(dialogBackInfo["name"] as String? ?: "")
        }

        backListener.backInfo(id, backInfo)
        setData(position, dialogBackInfo["name"] as String? ?: "", true)
    }

    /**
     * OnClickListener
     */
    private fun onClick(id: String, name: String, opts: MML, position: Int) {
        // select 类型事件
        selectView.setOnClickListener {
            showClickDialog(context, opts, position, name, id)
        }
    }

    /**
     * 实现到 Edit 的封装
     * @param id String
     * @param position Int
     */
    private fun onEdit(id: String, position: Int) {
        // 防止崩溃
        editView.imeOptions = EditorInfo.IME_ACTION_DONE
        editView.setOnEditorActionListener { _, _, _ -> true }
        editView.setOnClickListener {}

        when (id) {
            "email" -> {
                val emailRv = rvView
                val emailEdt = editView
                val emailWatcher = object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (p0 != null) {
                            val text = p0.toString().trim()
                            backListener.backInfo(id, text)
                            setData(position, text, false)
                            if (!RegexUtils.isEmail(text) && text.isNotEmpty() && !text.contains("@")) {
                                emailRv.visibility = View.VISIBLE
                                emailRv.adapter = initEmailAdapter()

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

                            } else {
                                emailRv.visibility = View.GONE
                            }
                        } else {
                            backListener.backInfo(id, "")
                            setData(position, "", false)
                        }
                    }

                    override fun afterTextChanged(p0: Editable?) {}
                }

                emailEdt.addTextChangedListener(emailWatcher)
            }

            else -> {
                val textListener = object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

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

                    override fun afterTextChanged(p0: Editable?) {}
                }
                editView.addTextChangedListener(textListener)
            }
        }
    }


    // 外部接口
    fun backBankName(bankNameListener: BackBankName) {
        this.bankNameListener = bankNameListener
    }

    fun backInfo(select: BackInfo) {
        this.backListener = select
    }

    fun backBuildMML(buildListener: BackBuildMML) {
        this.buildListener = buildListener
    }

    fun setData(data: BackMMM) {
        this.dataBack = data
    }

    // 家族
    /**
     * 资源ID
     * @param off Int Select 未选中颜色
     * @param on Int Select 选中颜色
     */
    protected fun initShowAsset(off: Int, on: Int) {
        this.colorOff = off
        this.colorOn = on
    }

    // 本地
    /**
     * 设置 value
     * 往 Data 中直接插入 Value ， Key = "itemValue"
     * notify -> 是否更新 RecycleView
     */
    private fun setData(position: Int, value: String, notify: Boolean) {
        val itemData = data[position]
        itemData["itemValue"] = value
        LogUtils.json(data)
        if (notify) {
            notifyItemChanged(position)
        }
        buildListener.backInfo(data as MML? ?: return)
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

    // 抽象
    /**
     * 实现 Email Adapter 的方法
     * @return RecyclerView.Adapter<*>
     */
    protected abstract fun initEmailAdapter(): RecyclerView.Adapter<*>

    /**
     * 实现工作信息 Dialog
     * @param workInfo MutableList<*>? 缓存 Work 信息
     */
    protected abstract fun showWorkDialog(workInfo: MutableList<*>?): WorkBackInfo


    /**
     * 实现地址信息 Dialog
     */
    protected abstract fun showAddressDialog(): AddressBackInfo

    /**
     * 初始化 IconView
     * @param id String
     */
    protected abstract fun initIcon(id: String): Boolean

    /**
     * 实现基础点击事件的Dialog
     * 需实现 doingClickFun方法
     *
     * @param context Context
     * @param title String
     * @param id String
     */
    protected abstract fun showClickDialog(
        context: Context, listData: MML, position: Int, title: String, id: String
    )

    /**
     * 初始化和绑定 Adapter View Binding
     * @return InitFormView
     */
    protected abstract fun onViewInit(view: V): InitFormView


    // 接口
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

    // 实体
    class InitFormView

    /**
     * 工作返回体
     * @property info MutableMap<String, Any>   插入提交 Map 的数据
     * @property name String                    显示名
     */
    class WorkBackInfo {
        var info: MMM = mutableMapOf()
        var name = ""
    }

    /**
     * 地址返回体
     * @property info MutableMap<String, Any>   插入提交 Map 的数据
     * @property name String                    显示名
     */
    class AddressBackInfo {
        var info: MMM = mutableMapOf()
        var name = ""
    }
}