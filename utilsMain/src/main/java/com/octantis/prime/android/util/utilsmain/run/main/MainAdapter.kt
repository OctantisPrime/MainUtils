package com.octantis.prime.android.util.utilsmain.run.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

abstract class MainAdapter<V : ViewDataBinding>(private val data: MutableList<*>) :
    Adapter<MainAdapter<V>.MainViewHolder>() {
    inner class MainViewHolder(var bind: V) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val v = DataBindingUtil.inflate<V>(
            LayoutInflater.from(parent.context), getLayoutId(), parent, false
        )
        return MainViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    abstract fun getLayoutId(): Int

}