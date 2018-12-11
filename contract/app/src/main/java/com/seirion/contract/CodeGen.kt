package com.seirion.contract

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.seirion.contract.utils.generateQRCode

import kotlinx.android.synthetic.main.activity_codegen.*

class CodeGenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_codegen)
        initUi()
    }

    private fun initUi() {
        generateButton.setOnClickListener { generate() }
    }

    private fun generate() {
        val str = input.text.toString().trim()
        if (!TextUtils.isEmpty(str)) {
            imageView.setImageBitmap(generateQRCode(str, 600))
        }
    }

    companion object {
        private val TAG = CodeGenActivity::class.java.simpleName

        fun start(activity: Activity) {
            Log.d(TAG, "$TAG.start()")
            val intent = Intent(activity, CodeGenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity.startActivity(intent)
        }
    }
}
