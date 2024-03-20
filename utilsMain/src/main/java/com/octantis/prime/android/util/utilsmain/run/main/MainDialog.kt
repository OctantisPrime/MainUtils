package com.octantis.prime.android.util.utilsmain.run.main

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.ScreenUtils

abstract class MainDialog<V : ViewDataBinding>(context: Context) : Dialog(context) {
    protected lateinit var v: V

    init {
        super.setCancelable(false)
        super.setCanceledOnTouchOutside(false)
        initViewBinding()
    }

    fun canCancelable() {
        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setContentView())
    }

    private fun initViewBinding() {
        v = DataBindingUtil.inflate(layoutInflater, getLayoutId(), null, false)
    }

    abstract fun setContentView(): View

    abstract fun getLayoutId(): Int

    /**
     * 中央
     * @param dimAmount Float   背景透明度
     * @param wid Float         宽度 0.1 ~ 1.0
     */
    fun baseType(root: View, dimAmount: Float, wid: Float) {
        this.window?.let {
            val wei = ScreenUtils.getScreenWidth()
            root.post {
                it.attributes.let { p ->
                    p.height = root.height
                    if (wid == 1.0F) {
                        p.width = WindowManager.LayoutParams.MATCH_PARENT
                    } else {
                        p.width = (wei * wid).toInt()
                    }
                    it.setDimAmount(dimAmount)
                    it.attributes = p
                }
            }
        }
    }

    /**
     * 底部
     * @param dimAmount Float 背景透明度
     */
    fun baseBottom(root: View, dimAmount: Float) {
        this.window?.let {
            // 获取根视图高宽度
            root.post {
                it.attributes.let { p ->
                    p.height = root.height
                    p.width = WindowManager.LayoutParams.MATCH_PARENT
                    p.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                    it.setDimAmount(dimAmount)
                    it.attributes = p
                }
            }
        }
    }

    /**
     * 全屏
     */
    @Suppress("DEPRECATION")
    fun fullWindow() {
        val win = this.window!!
        win.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = win.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        lp.gravity = Gravity.CENTER

        //兼容刘海屏
        if (Build.VERSION.SDK_INT >= 28) {
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        win.attributes = lp

        win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val options = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        win.decorView.systemUiVisibility = options
    }

    /**
     * 透明背景
     * 传入透明颜色
     * @param notColorId Int
     */
    fun hideBack(notColorId: Int) {
        window?.setBackgroundDrawableResource(notColorId)
    }

    /**
     * 隐藏背景 设置透明度
     * @param dim Float
     * @param notDra Int 同名色ID
     */
    fun hideBackDim(dim: Float, notDra: Int) {
        window?.setDimAmount(dim)
        window?.setBackgroundDrawableResource(notDra)
    }

}