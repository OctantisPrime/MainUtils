package com.octantis.prime.android.util.utilsmain.run.dialog

import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.databinding.ViewDataBinding
import com.octantis.prime.android.util.utilsmain.run.inf.BackMMM
import com.octantis.prime.android.util.utilsmain.run.main.MainAdapter
import com.octantis.prime.android.util.utilsmain.run.type.MML

abstract class SelectMainAdapter<V : ViewDataBinding>(
    private val data: MML,
    private val nameKey: String,
    private val colorKey: String?
) : MainAdapter<V>(data) {
    private lateinit var backSelect: BackMMM

    fun backSelect(backSelect: BackMMM) {
        this.backSelect = backSelect
    }

    abstract fun showTitleView(view:V): TextView

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val itemInfo = data[position]
        showTitleView(holder.bind).text = itemInfo[nameKey] as String? ?: ""
        colorKey?.apply {
            val color = itemInfo[this] as String? ?: ""
            if (color.isNotEmpty()) {
                showTitleView(holder.bind).setTextColor(color.toColorInt())
            }
        }
        showTitleView(holder.bind).setOnClickListener {
            backSelect.info(data[position])
        }
    }
}