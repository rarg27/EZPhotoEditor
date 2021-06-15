package dev.renz.photoeditor

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

class ImageLayer(private val mPhotoEditorView: ViewGroup,
                 private val mMultiTouchListener: MultiTouchListener,
                 private val mViewState: PhotoEditorViewState,
                 private val graphicManager: GraphicManager) : Graphic(mPhotoEditorView.context, graphicManager) {

    private var imageView: EditingImageView? = null

    var adjustMode : Boolean = true
        set(value) {
            field = value
            setupAdjustMode()
        }

    var eraserWidth : Float = 50f
        set(value) {
            field = value
            imageView?.setEraserWidth(field)
        }

    var eraserMode : Boolean = false
        @SuppressLint("ClickableViewAccessibility")
        set(value) {
            field = value
            if (field) {
                imageView?.showEraser = true
                imageView?.setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        rootView.bringToFront()
                        mViewState.setCurrentSelectedView(rootView)
                    }

                    imageView!!.EraserTouchListener().onTouch(v, event)
                }
            } else {
                imageView?.showEraser = false
                imageView?.setOnTouchListener(null)
            }
        }

    var filterMode : Boolean = false
        set(value) {
            field = value
            if (field) {
                imageView?.visibility = View.VISIBLE
            } else {
                imageView?.visibility = View.GONE
            }
        }

    var brightness = 0f
        set(value) {
            field = value
            imageView?.brightness = field
        }

    var constrast = 0f
        set(value) {
            field = value
            imageView?.contrast = field
        }

    var saturation = 0f
        set(value) {
            field = value
            imageView?.saturation = field
        }

    init {
        setupAdjustMode()
    }

    override fun getViewType(): ViewType {
        return ViewType.IMAGE
    }

    override fun getLayoutId(): Int {
        return R.layout.view_photo_editor_image
    }

    override fun setupView(rootView: View) {
        imageView = rootView.findViewById(R.id.imgPhotoEditorImage)
    }

    fun buildView(desiredImage: Bitmap) {
        imageView?.setImageBitmap(desiredImage)
    }

    fun undo() : Boolean {
        return imageView!!.undo()
    }

    fun redo() : Boolean {
        return imageView!!.redo()
    }

    private fun setupAdjustMode() {
        if (adjustMode) {
            setupGesture()
        } else {
            rootView.setOnTouchListener(null)
        }
    }

    private fun setupGesture() {
        val onGestureControl: MultiTouchListener.OnGestureControl = buildGestureController(mPhotoEditorView, mViewState)
        mMultiTouchListener.setOnGestureControl(onGestureControl)
        val rootView = rootView
        rootView.setOnTouchListener(mMultiTouchListener)
    }
}