package dev.renz.photoediting

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.slider.Slider
import dev.renz.photoeditor.ImageLayer
import dev.renz.photoeditor.OnPhotoEditorListener
import dev.renz.photoeditor.PhotoEditor
import dev.renz.photoeditor.ViewType

class MainActivity : AppCompatActivity(), OnPhotoEditorListener {

    private val imageUrl = "https://dev.snkrhud.com/storage/rembg/20210615083306_6834d8.png"
    private val imageUrl2 = "https://dev.snkrhud.com/storage/rembg/20210615083412_9ebcd8.png"

    private var imageLayer1 : ImageLayer? = null
    private var imageLayer2 : ImageLayer? = null

    private var eraserMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val photoEditor = PhotoEditor.Builder(this, findViewById(R.id.photoEditor)).build().apply {
            setOnPhotoEditorListener(this@MainActivity)
        }

        Glide.with(this).asBitmap().load(imageUrl).into(object : CustomTarget<Bitmap?>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                imageLayer1 = photoEditor.addImage(resource).apply {
                    adjustMode = false
                }
            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }
        })

        Glide.with(this).asBitmap().load(imageUrl2).into(object : CustomTarget<Bitmap?>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                imageLayer2 = photoEditor.addImage(resource).apply {
                    filterMode = true
                }
            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }
        })

        findViewById<Button>(R.id.btnEraser).setOnClickListener {
            eraserMode = !eraserMode
            imageLayer2?.eraserMode = eraserMode
        }

        findViewById<Button>(R.id.btnRedo).setOnClickListener {
            imageLayer2?.redo()
        }

        findViewById<Button>(R.id.btnUndo).setOnClickListener {
            imageLayer2?.undo()
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {

        }

        findViewById<Slider>(R.id.slider).addOnChangeListener { _, value, _ ->
            imageLayer2?.eraserWidth = value
        }

        findViewById<Slider>(R.id.saturation).addOnChangeListener { _, value, _ ->
            imageLayer2?.saturation = value
        }
    }

    override fun onAddViewListener(viewType: ViewType, numberOfAddedViews: Int) {

    }

    override fun onRemoveViewListener(viewType: ViewType, numberOfAddedViews: Int) {

    }

    override fun onStartViewChangeListener(viewType: ViewType) {

    }

    override fun onStopViewChangeListener(viewType: ViewType) {

    }

}