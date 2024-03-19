package com.octantis.prime.android.util.utilsmain.run.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.viewbinding.ViewBinding

abstract class MainAdapter<V : ViewDataBinding>(private val data: MutableList<*>) :
    Adapter<MainAdapter.ViewHolder>() {
    protected lateinit var v: V

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        v = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            getLayoutId(),
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    abstract fun getLayoutId(): Int
    class ViewHolder(itemView: ViewBinding) : RecyclerView.ViewHolder(itemView.root)

}