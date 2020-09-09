package heath.android.sample.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import heath.android.sample.utils.getBitmap

class RectImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    private val rects by lazy { arrayListOf<RectF>() }
    private val paint by lazy { Paint().apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    } }
    @DrawableRes var checkImageResource: Int? = null
    val checkedPoints by lazy { arrayListOf<PointF>() }

    fun setRectColor(color: Int) {
        paint.color = color
    }

    fun updateRects(data : ArrayList<RectF>) {
        rects.clear()
        rects.addAll(data)
        invalidate()
    }

    fun addCheckPoint(point: PointF) {
        checkedPoints.add(point)
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        rects.forEach {
            canvas?.drawRect(it, paint);
        }

        checkImageResource?.let {  res ->
            checkedPoints.forEach {
                val bitmap = getBitmap(context, res)
                val bitmapLeft = it.x - bitmap.width/2.0f
                val bitmapTop = it.y - bitmap.height/2.0f
                canvas?.drawBitmap(bitmap, bitmapLeft, bitmapTop, null)
                bitmap.recycle()
            }
        }
    }
}