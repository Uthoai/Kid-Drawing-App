package com.top.best.kid.drawing.app

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {
    private var drawingView: DrawingView? = null
    private var myImageButtonCurrentPaint: ImageButton? = null

    val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            result->
            if (result.resultCode == RESULT_OK && result.data != null ){
                val bgImage: ImageView = findViewById(R.id.bg_Image)
                bgImage.setImageURI(result.data?.data)
            }
        }

    private var requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permission ->
            permission.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted) {
                    Toast.makeText(this@MainActivity, "gallery permission granted", Toast.LENGTH_SHORT).show()
                    val pickIntent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGalleryLauncher.launch(pickIntent)
                }
                else {
                    if (permissionName == Manifest.permission.READ_MEDIA_IMAGES) {
                        Toast.makeText(this@MainActivity, "gallery permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.drawingView)
        drawingView?.setSizeForBrush(16.toFloat())

        val linearLayoutPaintColors = findViewById<LinearLayout>(R.id.ll_color_pallet)

        myImageButtonCurrentPaint = linearLayoutPaintColors[1] as ImageButton
        myImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.pressed_color_pallet)
        )

        val ibBrush: ImageButton = findViewById(R.id.ib_brushChooseBtn)
        ibBrush.setOnClickListener {
            showBrushSizeChooserDialog()
        }

        val btnUndo = findViewById<ImageButton>(R.id.ib_undoBtn)
        btnUndo.setOnClickListener {
            drawingView?.onClickUndo()
        }

        val btnGallery = findViewById<ImageButton>(R.id.ib_galleryBtn)
        btnGallery.setOnClickListener {
            requestGalleryPermission()
        }
    }

    private fun requestGalleryPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@MainActivity,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        ) {
            showRationalDialog(
                "Kid Drawing App",
                "Kid Drawing App need to gallery access"
            )
        } else {
            requestPermission.launch(
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                )
            )
        }
    }

    private fun showRationalDialog(
        title: String,
        message: String
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun showBrushSizeChooserDialog() {
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

    fun paintClicked(view: View) {
        if (view !== myImageButtonCurrentPaint) {
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            drawingView?.setColor(colorTag)

            imageButton.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pressed_color_pallet)
            )

            myImageButtonCurrentPaint!!.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.normal_color_pallet)
            )

            myImageButtonCurrentPaint = view
        }
    }
}