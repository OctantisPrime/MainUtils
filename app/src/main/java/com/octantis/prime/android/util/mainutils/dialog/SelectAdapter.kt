package com.octantis.prime.android.util.mainutils.dialog

import android.widget.TextView
import com.octantis.prime.android.util.mainutils.R
import com.octantis.prime.android.util.mainutils.databinding.SelectItemBinding
import com.octantis.prime.android.util.utilsmain.run.dialog.SelectMainAdapter
import com.octantis.prime.android.util.utilsmain.run.type.MML

/**
 * 基础选择Adapter
 * @constructor
 * nameKey 展示的键值
 * colorKey 颜色的键值 如果有的话  如 (#147256)
 */
class SelectAdapter(data: MML, nameKey: String, colorKey: String?) :
    SelectMainAdapter<SelectItemBinding>(
        data, nameKey, colorKey
    ) {
    override fun showTitleView(view: SelectItemBinding): TextView {
        return view.name
    }

    override fun getLayoutId(): Int {
        return R.layout.select_item
    }
}