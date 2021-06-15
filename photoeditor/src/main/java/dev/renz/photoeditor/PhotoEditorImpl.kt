package dev.renz.photoeditor

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

class PhotoEditorImpl(builder: PhotoEditor.Builder) : PhotoEditor {

    companion object {
        private const val TAG = "PhotoEditor"
    }

    private var parentView: PhotoEditorView = builder.parentView
    private val viewState: PhotoEditorViewState = PhotoEditorViewState()
    private var mOnPhotoEditorListener: OnPhotoEditorListener? = null
    private val mGraphicManager: GraphicManager = GraphicManager(builder.parentView, this.viewState)

    override fun addImage(desiredImage: Bitmap) : ImageLayer {
        val multiTouchListener: MultiTouchListener = getMultiTouchListener(true)
        val imageLayer = ImageLayer(parentView, multiTouchListener, viewState, mGraphicManager)
        imageLayer.buildView(desiredImage)
        addToEditor(imageLayer)
        return imageLayer
    }

    override fun undo(): Boolean = mGraphicManager.undoView()

    override fun redo(): Boolean = mGraphicManager.redoView()

    override fun setSelectedView(graphic: Graphic) {
        viewState.setCurrentSelectedView(graphic.rootView)
    }

    override fun saveAsBitmap(saveSettings: SaveSettings, onSaveBitmap: PhotoEditor.OnSaveBitmap) {
        try {
            val result = buildBitmap(saveSettings)
            onSaveBitmap.onBitmapReady(result)
        } catch (e: Exception) {
            onSaveBitmap.onFailure(e)
        }
    }

    override fun setOnPhotoEditorListener(onPhotoEditorListener: OnPhotoEditorListener) {
        mOnPhotoEditorListener = onPhotoEditorListener
        mGraphicManager.setOnPhotoEditorListener(mOnPhotoEditorListener)
    }

    private fun addToEditor(graphic: Graphic) {
        mGraphicManager.addView(graphic)
        // Change the in-focus view
        viewState.setCurrentSelectedView(graphic.rootView)
    }

    /**
     * Create a new instance and scalable touchview
     *
     * @param isPinchScalable true if make pinch-scalable, false otherwise.
     * @return scalable multitouch listener
     */
    private fun getMultiTouchListener(isPinchScalable: Boolean): MultiTouchListener {
        return MultiTouchListener(
            parentView,
            isPinchScalable,
            mOnPhotoEditorListener!!,
            this.viewState
        )
    }

    private fun buildBitmap(mSaveSettings : SaveSettings) : Bitmap {
        return if (mSaveSettings.isTransparencyEnabled)
            BitmapUtil.removeTransparency(captureView(parentView))
        else captureView(parentView)
    }

    private fun captureView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}