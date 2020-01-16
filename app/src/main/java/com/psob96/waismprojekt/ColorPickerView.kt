package com.psob96.waismprojekt

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView

class ColorPickerView : ImageView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    interface OnColorChangedListener {
        fun onColorChanged(color: Int)
    }

    lateinit var callback: OnColorChangedListener

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {

        if (ev == null) return false

        val inverse = Matrix()
        imageMatrix.invert(inverse)
        val touchPoint = floatArrayOf(ev.x, ev.y)
        inverse.mapPoints(touchPoint)
        val x  = touchPoint[0].toInt()
        val y = touchPoint[1].toInt()

        when (ev.action){
            MotionEvent.ACTION_MOVE -> {onMove(x,y)}
        }

        return true
    }

    private fun onMove(x: Int, y: Int){

        val bitmap = (drawable as BitmapDrawable).bitmap

        if (x !in 0 until bitmap.width || y !in 0 until bitmap.height) return

        val pixel = bitmap.getPixel(x, y)
        val redValue = Color.red(pixel)
        val blueValue = Color.blue(pixel)
        val greenValue = Color.green(pixel)
        val color = Color.rgb(redValue, greenValue, blueValue)

        callback.onColorChanged(color)
    }
}