package com.seirion.contract

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
    }

    private fun initUi() {
        scanning.setOnClickListener { scan() }
        openWeb.setOnClickListener { open() }
    }

    private fun scan() {
        text.text = ""
        openWeb.visibility = View.GONE
        IntentIntegrator(this).initiateScan()
    }

    private fun open() {
        val intent = Intent(Intent.ACTION_VIEW)
        val url = text.text.toString()
        Log.d(TAG, "open url: $url")
        intent.data = Uri.parse(url)
        startActivity(intent)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result == null) {
                //Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Log.d(TAG, "result: ${result.contents}")
                //Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                text.text = result.contents
                if (result.contents.startsWith("http")) {
                    openWeb.visibility = View.VISIBLE
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName

        fun start(activity: Activity) {
            Log.d(TAG, "$TAG.start()")
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity.startActivity(intent)
        }
    }
}
