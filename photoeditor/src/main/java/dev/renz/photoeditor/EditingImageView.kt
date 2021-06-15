package dev.renz.photoeditor

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.utils.widget.ImageFilterView

class EditingImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ImageFilterView(context, attrs, defStyleAttr) {

    private val erasedPaths = mutableListOf<ErasedPath>()
    private val historyErasedPaths = mutableListOf<ErasedPath>()
    private var currentErasedPath : ErasedPath? = null

    var showEraser = false
        set(value) {
            field = value
            if (field) {
                currX = this.x + this.width / 2
                currY = this.y + this.height / 2
            }

            invalidate()
        }

    private var currX = 0f
    private var currY = 0f

    private var eraserStrokeWidth = 50f
    private val eraserIndicatorPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    private val eraserPaint = Paint().apply {
        alpha = 0
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = eraserStrokeWidth
        maskFilter = null
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        erasedPaths.forEach { canvas?.drawPath(it.path, eraserPaint.apply { strokeWidth = it.width }) }
        currentErasedPath?.let { canvas?.drawPath(it.path, eraserPaint.apply { strokeWidth = it.width }) }

        if (showEraser) {
            canvas?.drawCircle(currX, currY, eraserStrokeWidth / 2, eraserIndicatorPaint)
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun setImageBitmap(bm: Bitmap?) {
        currentErasedPath = null
        erasedPaths.clear()
        historyErasedPaths.clear()

        return super.setImageBitmap(bm)
    }

    fun setEraserWidth(width: Float) {
        eraserStrokeWidth = width
        invalidate()
    }

    fun undo() : Boolean {
        if (erasedPaths.isNotEmpty()) {
            historyErasedPaths.add(erasedPaths.last())
            erasedPaths.removeAt(erasedPaths.lastIndex)

            invalidate()
            return erasedPaths.isEmpty()
        }

        return true
    }

    fun redo() : Boolean {
        if (historyErasedPaths.isNotEmpty()) {
            erasedPaths.add(historyErasedPaths.last())
            historyErasedPaths.removeAt(historyErasedPaths.lastIndex)

            invalidate()
            return historyErasedPaths.isEmpty()
        }

        return true
    }

    inner class EraserTouchListener : OnTouchListener {

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            currX = event.x
            currY = event.y

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (currentErasedPath == null) {
                        currentErasedPath = ErasedPath(Path(), eraserStrokeWidth)
                    }
                    currentErasedPath?.path?.moveTo(event.x, event.y)
                }
                MotionEvent.ACTION_MOVE -> {
                    currentErasedPath?.path?.lineTo(event.x, event.y)
                }
                MotionEvent.ACTION_UP -> {
                    currentErasedPath?.let {
                        erasedPaths.add(it)
                        historyErasedPaths.clear()
                    }
                    currentErasedPath = null
                }
            }

            this@EditingImageView.invalidate()
            return true
        }
    }
}

data class ErasedPath(
    var path: Path,
    var width: Float
)