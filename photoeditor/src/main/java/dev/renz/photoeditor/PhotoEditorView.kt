package dev.renz.photoeditor

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout

class PhotoEditorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "PhotoEditorView"
        private const val imgSrcId = 1
    }

    private var mImgSource: ImageView = ImageView(getContext())

    init {
        mImgSource.id = imgSrcId
        mImgSource.adjustViewBounds = true
        val imgSrcParam = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        imgSrcParam.addRule(CENTER_IN_PARENT, TRUE)
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.PhotoEditorView)
            val imgSrcDrawable = a.getDrawable(R.styleable.PhotoEditorView_photo_src)
            if (imgSrcDrawable != null) {
                mImgSource.setImageDrawable(imgSrcDrawable)
            }

            a.recycle()
        }

        addView(mImgSource, imgSrcParam)
    }

}