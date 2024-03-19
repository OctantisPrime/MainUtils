package com.octantis.prime.android.util.mainutils.dialog

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.octantis.prime.android.util.mainutils.R
import com.octantis.prime.android.util.mainutils.databinding.SelectDialogBinding
import com.octantis.prime.android.util.utilsmain.run.dialog.SelectMainDialog
import com.octantis.prime.android.util.utilsmain.run.inf.BackMMM
import com.octantis.prime.android.util.utilsmain.run.type.MML
import com.octantis.prime.android.util.utilsmain.run.type.MMM

class SelectDialog(
    context: Context, data: MML, title: String, id: String?, private val nameKey: String
) : SelectMainDialog<SelectDialogBinding>(
    context, data, title, id, nameKey
) {
    override fun setSearchView(): SearchView {
        return v.search
    }

    override fun setBodyView(): RecyclerView {
        return v.body
    }

    override fun setTitleView(): TextView {
        return v.title
    }

    override fun setRecycleView(showData: MML): RecyclerView.Adapter<*> {
        val adapter = SelectAdapter(showData, nameKey, null)
        adapter.backSelect(object : BackMMM {
            override fun info(info: MMM) {
                backSelect.info(info)
            }
        })
        return adapter
    }

    override fun setContentView(): View {
        return v.root
    }

    override fun getLayoutId(): Int {
        return R.layout.select_dialog
    }
}