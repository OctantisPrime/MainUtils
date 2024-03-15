package com.octantis.prime.android.util.mainutils

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octantis.prime.android.util.utilsmain.run.AesUtil
import com.octantis.prime.android.util.utilsmain.run.RunUtil
import com.octantis.prime.android.util.utilsmain.run.form.FormUtils
import com.octantis.prime.android.util.utilsmain.run.http.HttpUtil
import com.octantis.prime.android.util.utilsmain.run.init.MainInit
import com.octantis.prime.android.util.utilsmain.run.type.MMM

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initClick()
        test()
    }

    private fun test() {
        val rb = findViewById<RecyclerView>(R.id.rv)
        val data = mutableListOf<MMM>()
        data.add(mutableMapOf())
        data.add(mutableMapOf())
        data.add(mutableMapOf())

        rb.layoutManager = LinearLayoutManager(this)
        rb.adapter = FormAdapter(this, data = data)


    }

    private fun initClick() {
        val enB = findViewById<Button>(R.id.button)
        val deB = findViewById<Button>(R.id.button2)
        enB.setOnClickListener {
            en()
        }
        deB.setOnClickListener {
            de()
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