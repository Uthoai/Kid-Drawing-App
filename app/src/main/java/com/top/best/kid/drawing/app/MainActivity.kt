package com.top.best.kid.drawing.app

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {
    private var drawingView: DrawingView? = null
    private var myImageButtonCurrentPaint: ImageButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.drawingView)
        drawingView?.setSizeForBrush(16.toFloat())

        val linearLayoutPaintColors = findViewById<LinearLayout>(R.id.ll_color_pallet)

        myImageButtonCurrentPaint = linearLayoutPaintColors[2] as ImageButton
        myImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this,R.drawable.pressed_color_pallet)
        )

        val ibBrush: ImageButton = findViewById(R.id.ib_brushChooseBtn)
        ibBrush.setOnClickListener {
            showBrushSizeChooserDialog()
        }
    }

    private fun showBrushSizeChooserDialog(){
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        //brushDialog.setTitle("Brush Size:")

        val smallBtn = brushDialog.findViewById<ImageButton>(R.id.ib_small_brush)
        smallBtn.setOnClickListener {
            drawingView?.setSizeForBrush(10.toFloat())
            brushDialog.dismiss()
        }

        val mediumBtn = brushDialog.findViewById<ImageButton>(R.id.ib_medium_brush)
        mediumBtn.setOnClickListener {
            drawingView?.setSizeForBrush(16.toFloat())
            brushDialog.dismiss()
        }

        val bigBtn = brushDialog.findViewById<ImageButton>(R.id.ib_big_brush)
        bigBtn.setOnClickListener {
            drawingView?.setSizeForBrush(26.toFloat())
            brushDialog.dismiss()
        }

        brushDialog.show()
    }
}