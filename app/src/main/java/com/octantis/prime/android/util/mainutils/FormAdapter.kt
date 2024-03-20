package com.octantis.prime.android.util.mainutils

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.octantis.prime.android.util.mainutils.databinding.RecycleItemAdapterBinding
import com.octantis.prime.android.util.mainutils.dialog.SelectDialog
import com.octantis.prime.android.util.utilsmain.run.form.adapter.FormMainAdapter
import com.octantis.prime.android.util.utilsmain.run.inf.BackMMM
import com.octantis.prime.android.util.utilsmain.run.type.MML
import com.octantis.prime.android.util.utilsmain.run.type.MMM

class FormAdapter(
    context: Context, data: MML
) : FormMainAdapter<RecycleItemAdapterBinding>(context, data) {
    private lateinit var selectDialog: SelectDialog

    override fun onInitIcon(id: String): Boolean {
        return false
    }

    override fun onInitViewBinding(view: RecycleItemAdapterBinding): InitFormView {
        return initLayoutView(
            rootView = view.root,
            iconView = view.icon,
            titleView = view.title,
            selectView = view.select,
            rvView = view.rv,
            rgView = view.formBankGr,
            rgLView = view.formBankLeft,
            rgRView = view.formBankRight,
            editView = view.text,
            bankView = view.bankTip
        )
    }

    override fun onShowClickDialog(
        context: Context, listData: MML, position: Int, title: String, id: String
    ) {
        selectDialog = SelectDialog(context, listData, title, id, "name")
        selectDialog.backSelect(object : BackMMM {
            override fun info(info: MMM) {
                selectDialog.dismiss()
                doingClickFun(id, info, position)
            }
        })
        selectDialog.show()
    }

    override fun onInitFontColor(): InitFontColor {
        return initFFontColor(R.color.black, R.color.black)
    }

    override fun onInitEmailAdapter(): RecyclerView.Adapter<*> {
        TODO("Not yet implemented")
    }

    override fun onShowWorkDialog(workInfo: MutableList<*>?): WorkBackInfo {
        val backInfo = WorkBackInfo()
        backInfo.info = mutableMapOf()
        backInfo.name = "a address"
        return backInfo
    }

    override fun onShowAddressDialog(): AddressBackInfo {
        val backInfo = AddressBackInfo()
        backInfo.info = mutableMapOf()
        backInfo.name = "a work"
        return backInfo
    }

    override fun getLayoutId(): Int {
        return R.layout.recycle_item_adapter
    }

}