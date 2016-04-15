package org.weijian.mydriversrss

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by weijian on 16-4-15.
 */

class AsyncImageView : ImageView, ImageManager.ImageChangedListener {
    override fun onImageChange(url: String, bitmap: Bitmap) {
        throw UnsupportedOperationException()
    }

    override fun onComplete(url: String, bitmap: Bitmap) {
        imageUrl = url
    }

    override fun onFailed(reason: Int) {
        throw UnsupportedOperationException()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    var imageUrl: String = ""
        get
        set(value) {
            field = value
            val bitmap = ImageManager.getBitmap(value)
            if (bitmap == null) {
                ImageManager.setImageChangedListener(value, this)
            }
            setImageBitmap(bitmap)
        }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}