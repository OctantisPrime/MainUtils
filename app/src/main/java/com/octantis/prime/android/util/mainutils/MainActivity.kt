package com.octantis.prime.android.util.mainutils

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octantis.prime.android.util.mainutils.databinding.ActivityMainBinding
import com.octantis.prime.android.util.mainutils.dialog.SelectDialog
import com.octantis.prime.android.util.utilsmain.run.AesUtil
import com.octantis.prime.android.util.utilsmain.run.RunUtil
import com.octantis.prime.android.util.utilsmain.run.form.FormUtils
import com.octantis.prime.android.util.utilsmain.run.form.adapter.FormMainAdapter
import com.octantis.prime.android.util.utilsmain.run.http.HttpUtil
import com.octantis.prime.android.util.utilsmain.run.inf.BackMMM
import com.octantis.prime.android.util.utilsmain.run.init.MainInit
import com.octantis.prime.android.util.utilsmain.run.type.MMM

class MainActivity : AppCompatActivity() {
    private lateinit var v: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        v = DataBindingUtil.inflate(layoutInflater, R.layout.activity_main, null, false)
        setContentView(v.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initClick()
    }

    private fun testDialog() {
        val showData = mutableListOf<MMM>()
        for (i in 0 until 16) {
            val mmm = mutableMapOf<String, Any>()
            mmm["name"] = i.toString()
            mmm["color"] = "$i color"
            showData.add(mmm)
        }

        val dia = SelectDialog(this, showData, "hhh", null, "name")
        dia.show()
    }

    private fun showFormAdapter() {
        val data = mutableListOf<MMM>()
        for (i in 0 until 3) {
            val info = mutableMapOf<String, Any>()
            info["name"] = "第 $i 个元素"
            info["type"] = "select"
            info["itemValue"] = "$i"
            info["id"] = "select.$i"
            val options = mutableListOf<MMM>()
            for (j in 0 until 3) {
                val optItem = mutableMapOf<String, Any>()
                optItem["name"] = j.toString()
                optItem["value"] = j.toString()
                options.add(optItem)
            }
            info["options"] = options
            data.add(info)
        }

        val adapter = FormAdapter(this, data)
        adapter.backBuildMML(object : FormMainAdapter.BackBuildMML {
            override fun backInfo(buildInfo: MutableList<*>) {
                Log.e("qfhqwhfhiowqfi", buildInfo.toString())
            }
        })

        adapter.backInfo(object : FormMainAdapter.BackInfo {
            override fun backInfo(id: String, value: Any) {
                Log.e("asdasd", id)
            }
        })

        adapter.backBankName(object : FormMainAdapter.BackBankName {
            override fun backName(name: String) {
                Log.e("asdasdasd", name)
            }

        })
        v.rv.layoutManager = LinearLayoutManager(this)
        v.rv.adapter = adapter

    }

    private fun initClick() {
        v.button.setOnClickListener {
            en()
        }
        v.button2.setOnClickListener {
            showFormAdapter()
        }
    }

    private fun en() {
        val textViewe = findViewById<TextView>(R.id.qwpdpqwd)
        val showText = textViewe.text.toString()
        val newShowText = AesUtil.encrypt(showText)
        textViewe.text = newShowText
    }

    private fun de() {
        val textViewe = findViewById<TextView>(R.id.qwpdpqwd)
        val showText = textViewe.text.toString()
        val newShowText = AesUtil.decrypt(showText)
        textViewe.text = newShowText
    }
}