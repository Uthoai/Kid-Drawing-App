package com.top.best.kid.drawing.app

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context,attrs: AttributeSet): View(context,attrs) {
    private var myDrawPath: CustomPath? = null
    private var myCanvasBitmap: Bitmap? = null
    private var myDrawPaint: Paint? = null
    private var myCanvasPaint: Paint? = null
    private var myBrushSize: Float = 0.toFloat()
    private var color = Color.BLACK
    private var canvas: Canvas? = null
    private var myPaths = ArrayList<CustomPath>()


    init {
        setUPDrawing()
    }

    private fun setUPDrawing() {
        myDrawPaint = Paint()
        myDrawPath = CustomPath(color,myBrushSize)
        myDrawPaint!!.color = color
        myDrawPaint!!.style = Paint.Style.STROKE
        myDrawPaint!!.strokeJoin = Paint.Join.ROUND
        myDrawPaint!!.strokeCap = Paint.Cap.ROUND
        myCanvasPaint = Paint(Paint.DITHER_FLAG)
        //myBrushSize = 16.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        myCanvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas = Canvas(myCanvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(myCanvasBitmap!!,0f,0f,myCanvasPaint)

        for (path in myPaths){
            myDrawPaint!!.strokeWidth = path.brushThickness
            myDrawPaint!!.color = path.color
            canvas.drawPath(path,myDrawPaint!!)
        }

        if (!myDrawPath!!.isEmpty){
            myDrawPaint!!.strokeWidth = myDrawPath!!.brushThickness
            myDrawPaint!!.color = myDrawPath!!.color
            canvas.drawPath(myDrawPath!!,myDrawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
                myDrawPath!!.color = color
                myDrawPath!!.brushThickness = myBrushSize

                myDrawPath!!.reset()
                if (touchX != null) {
                    if (touchY != null) {
                        myDrawPath!!.moveTo(touchX,touchY)
                    }
                }
            }

            MotionEvent.ACTION_MOVE ->{
                if (touchX != null) {
                    if (touchY != null) {
                        myDrawPath!!.lineTo(touchX,touchY)
                    }
                }
            }

            MotionEvent.ACTION_UP ->{
                myPaths.add(myDrawPath!!)
                myDrawPath = CustomPath(color,myBrushSize)
            }
            else -> return false
        }
        invalidate()
        return true
    }

    fun setSizeForBrush(newSize: Float){
        myBrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            newSize,resources.displayMetrics
        )
        myDrawPaint!!.strokeWidth = myBrushSize
    }

    internal inner class CustomPath(var color: Int,var brushThickness: Float): Path() {
    }
}