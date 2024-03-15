package com.octantis.prime.android.util.mainutils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import com.octantis.prime.android.util.mainutils.databinding.RecycleItemAdapterBinding
import com.octantis.prime.android.util.utilsmain.run.form.adapter.FormMainAdapter
import com.octantis.prime.android.util.utilsmain.run.type.MML

class FormAdapter(
    private val context: Context, data: MML
) : FormMainAdapter(context, data) {
    private var title = ObservableField("")
    private lateinit var v: RecycleItemAdapterBinding
    override fun initView(parent: ViewGroup): ViewDataBinding {
        v = RecycleItemAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return v
    }

    override fun bindingView(holder: ViewHolder, position: Int) {
        holder.itemView
        v.title = title
        title.set(position.toString())
    }
}