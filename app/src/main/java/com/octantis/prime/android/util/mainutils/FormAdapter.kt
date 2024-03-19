package com.octantis.prime.android.util.mainutils

import android.content.Context
import com.octantis.prime.android.util.mainutils.databinding.RecycleItemAdapterBinding
import com.octantis.prime.android.util.utilsmain.run.form.adapter.FormMainAdapter
import com.octantis.prime.android.util.utilsmain.run.main.MainAdapter
import com.octantis.prime.android.util.utilsmain.run.type.MML

class FormAdapter(
    private val context: Context, data: MML
) : FormMainAdapter<RecycleItemAdapterBinding>(context, data) {

    init {
    }

    override fun getLayoutId(): Int {
        return R.layout.recycle_item_adapter
    }

    override fun onBindViewHolder(holder: MainAdapter.ViewHolder, position: Int) {
    }
}