package com.psob96.waismprojekt

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.view.View
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

fun View.toBitmap(): Bitmap {
    val returnedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(returnedBitmap)

    if (background != null)
        background.draw(canvas)
    else
        canvas.drawColor(Color.WHITE)

    draw(canvas)

    return returnedBitmap
}

fun Activity.toast(txt: String) {
    Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()
}

fun Bitmap.saveToFile(path: String): Boolean {

    return try {
        val file = File(path)
        val fOut = FileOutputStream(file)
        compress(Bitmap.CompressFormat.PNG, 85, fOut)
        fOut.flush()
        fOut.close()
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun Bitmap.resize(maxWidth: Int, maxHeight: Int): Bitmap {
    return Bitmap.createScaledBitmap(this, maxWidth, maxHeight, true)
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

