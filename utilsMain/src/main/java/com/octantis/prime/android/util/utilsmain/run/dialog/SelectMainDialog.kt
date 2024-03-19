package com.octantis.prime.android.util.utilsmain.run.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.octantis.prime.android.util.utilsmain.run.inf.BackMMM
import com.octantis.prime.android.util.utilsmain.run.main.MainDialog
import com.octantis.prime.android.util.utilsmain.run.type.MML
import com.octantis.prime.android.util.utilsmain.run.type.MMM
import java.util.Locale
import java.util.Objects

abstract class SelectMainDialog<V : ViewDataBinding>(
    context: Context,
    private val data: MML,
    private val title: String,
    private val id: String?,
    private val nameKey: String
) : MainDialog<V>(context) {
    protected lateinit var backSelect: BackMMM
    private lateinit var searchView: SearchView
    private lateinit var titleView: TextView
    private lateinit var bodyView: RecyclerView
    private var showGriCount = 7
    private var showSearchCount = 10
    fun backSelect(select: BackMMM) {
        this.backSelect = select
    }

    init {
        super.setContentView(v.root)
        super.setCancelable(true)
        baseType(v.root, 0.3F, 1F)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSearchView()?.apply {
            searchView = this
        }
        setBodyView()?.apply {
            bodyView = this
        }
        setTitleView()?.apply {
            titleView = this
        }
        initSearch()
        initView()
    }

    abstract fun setSearchView(): SearchView?
    abstract fun setBodyView(): RecyclerView?
    abstract fun setTitleView(): TextView?

    fun setShowPayDateCount(count: Int) {
        this.showGriCount = count
    }

    fun setShowSearchCount(count: Int) {
        this.showSearchCount = count
    }

    private fun initSearch() {
        if (this::searchView.isInitialized) {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isNotEmpty()) {
                        val showList = mutableListOf<MMM>()
                        for (i in data.indices) {
                            val itemData = data[i]
                            if (Objects.requireNonNull<Any>(itemData[nameKey]).toString()
                                    .lowercase(
                                        Locale.getDefault()
                                    ).contains(newText.lowercase(Locale.getDefault())) ||
                                Objects.requireNonNull<Any>(itemData[nameKey]).toString()
                                    .uppercase(
                                        Locale.getDefault()
                                    ).contains(newText.uppercase(Locale.getDefault()))
                            ) {
                                showList.add(itemData)
                            }
                        }
                        bodyView.adapter =  setRecycleView(showList)
                    } else {
                        bodyView.adapter =  setRecycleView(data)
                    }
                    return false
                }
            })
        }
    }

    private fun initView() {
        titleView.text = title

        if (id == "payDate.monthDay" || id == "payDate.secondMonthDay") {
            bodyView.layoutManager = GridLayoutManager(context, showGriCount)
        } else {
            bodyView.layoutManager = LinearLayoutManager(context)
        }

        if (id == "payDate.monthDay" || id == "payDate.secondMonthDay") {
            searchView.visibility = View.GONE
        } else {
            if (data.size > showSearchCount) {
                searchView.visibility = View.VISIBLE
            } else {
                searchView.visibility = View.GONE
            }
        }
        bodyView.adapter =  setRecycleView(data)
    }

    /**
     * 自定义配置的数据
     * 返回体自己定义
     * @param showData MutableList<MutableMap<String, Any>> 返回数据类型
     */
    abstract fun setRecycleView(showData: MML): Adapter<*>

}