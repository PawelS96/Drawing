package com.psob96.waismprojekt

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.graphics.RectF
import android.R.attr.bitmap

class DrawingView : View {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var currentPath : Path = Path()
    private var currentPaint: Paint = Paint()
    private var paths = mutableListOf<Pair<Path, Paint>>()
    private var bitmap: Bitmap? = null
    private var viewWidth: Int = 0
    private var viewHeight: Int = 0

    init {

        currentPaint.apply {
            color = Color.BLUE
            strokeWidth = 20f
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }
    }

    fun setColor(color: Int) {
        updatePath()
        currentPaint.color = color
    }

    fun setWidth(width: Float) {
        updatePath()
        currentPaint.strokeWidth = width
    }

    fun getCurrentColor() : Int = currentPaint.color

    fun getStrokeWidth() : Int  = currentPaint.strokeWidth.toInt()

    fun drawBitmap(newBitmap: Bitmap){

        bitmap = newBitmap

        if (bitmap!!.width > bitmap!!.height)
            bitmap = bitmap!!.rotate(90F)

        if (bitmap!!.width > viewWidth || bitmap!!.height > viewHeight)
            bitmap = bitmap!!.resize(viewWidth, viewHeight)

        clear()
    }

    fun clear() {
        paths.forEach { it.first.reset() }
        paths.clear()
        currentPath.reset();
        invalidate()
    }

    private fun updatePath(){
        paths.add(Pair(Path(currentPath), Paint(currentPaint)))
        currentPath.reset()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        bitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }

        paths.forEach { canvas.drawPath(it.first, it.second)  }
        canvas.drawPath(currentPath, currentPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        viewWidth = w
        viewHeight = h
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        event?.let {

            val x : Float = event.x
            val y : Float = event.y

            when (event.action){
                MotionEvent.ACTION_DOWN -> {currentPath.moveTo(x, y)}
                MotionEvent.ACTION_MOVE -> {currentPath.lineTo(x, y)}
                MotionEvent.ACTION_UP -> {currentPath.lineTo(x, y)}
            }

            invalidate()
        }

        return true
    }
}
