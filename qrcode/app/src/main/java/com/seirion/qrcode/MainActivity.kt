package com.seirion.qrcode

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.seirion.qrcode.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDataBinding()
        binding.button.setOnClickListener { openQrScan() }
    }

    private fun setDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private fun openQrScan() {
        IntentIntegrator(this).initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Log.d(TAG, result.contents)
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                binding.text.text = result.contents
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private val TAG = MainActivity.javaClass.simpleName
    }
}
