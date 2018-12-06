package com.seirion.qrcode

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.seirion.qrcode.databinding.ActivityMainBinding
import com.google.zxing.WriterException
import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDataBinding()
        binding.button.setOnClickListener { openQrScan() }
        binding.generate.setOnClickListener { generate() }
    }

    private fun setDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private fun openQrScan() {
        IntentIntegrator(this).initiateScan()
    }

    private fun generate() {
        try {
            val qrCodeWriter = QRCodeWriter()
            val bitmap = toBitmap(qrCodeWriter.encode(binding.input.text.toString(), BarcodeFormat.QR_CODE, 200, 200))
            binding.image.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

    private fun toBitmap(matrix: BitMatrix): Bitmap {
        val height = matrix.height
        val width = matrix.width
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val color = when (matrix.get(x, y)) {
                    true -> Color.BLACK
                    else -> Color.WHITE
                }
                bmp.setPixel(x, y, color)
            }
        }
        return bmp
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Log.d(TAG, "result: ${result.contents}")
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                binding.text.text = result.contents
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        private val TAG = MainActivity.javaClass.simpleName
    }
}
