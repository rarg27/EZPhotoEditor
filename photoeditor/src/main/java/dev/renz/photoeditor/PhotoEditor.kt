package dev.renz.photoeditor

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap

interface PhotoEditor {

    /**
     * This will add image on {@link PhotoEditorView} which you drag,rotate and scale using pinch
     * if {@link PhotoEditor.Builder#setPinchTextScalable(boolean)} enabled
     *
     * @param desiredImage bitmap image you want to add
     */
    fun addImage(desiredImage : Bitmap) : ImageLayer

    /**
     * Undo the last operation perform on the [PhotoEditor]
     *
     * @return true if there nothing more to undo
     */
    fun undo(): Boolean

    /**
     * Redo the last operation perform on the [PhotoEditor]
     *
     * @return true if there nothing more to redo
     */
    fun redo(): Boolean

    /**
     * Set selected view
     *
     * @param selectedView view to set as selected
     */
    fun setSelectedView(graphic: Graphic)

    /**
     * Save the edited image as bitmap
     *
     * @param saveSettings builder for multiple save options [SaveSettings]
     * @param onSaveBitmap callback for saving image as bitmap
     * @see OnSaveBitmap
     */
    @SuppressLint("StaticFieldLeak")
    fun saveAsBitmap(saveSettings: SaveSettings, onSaveBitmap: OnSaveBitmap)

    /**
     * Callback on editing operation perform on [PhotoEditorView]
     *
     * @param onPhotoEditorListener [OnPhotoEditorListener]
     */
    fun setOnPhotoEditorListener(onPhotoEditorListener: OnPhotoEditorListener)

    class Builder(val context: Context,
                  val parentView: PhotoEditorView) {

        /**
         * @return build PhotoEditor instance
         */
        fun build(): PhotoEditor {
            return PhotoEditorImpl(this)
        }
    }

    interface OnSaveBitmap {
        fun onBitmapReady(saveBitmap: Bitmap?)
        fun onFailure(e: java.lang.Exception?)
    }

}