package com.seirion.contract.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix


fun generateQRCode(string: String, width: Int): Bitmap {
    try {
        val codeWriter = MultiFormatWriter()
        val height = width
        return toBitmap(codeWriter.encode(string, BarcodeFormat.QR_CODE, width, height))
    } catch (e: WriterException) {
        throw e
    }
}

fun toBitmap(matrix: BitMatrix): Bitmap {
    val height = matrix.height
    val width = matrix.width
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

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
