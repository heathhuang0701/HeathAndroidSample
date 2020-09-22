package heath.android.sample.utils

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.JsonElement
import heath.android.sample.model.ModelStringList
import java.io.InputStream
import java.util.*


fun sdpToPx(context: Context, resource_id: Int): Float {
    val dp = context.resources.getDimension(resource_id) / context.resources.displayMetrics.density
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    )
}

fun sspToPx(context: Context, resource_id: Int): Float {
    val sp = context.resources.getDimension(resource_id) / context.resources.displayMetrics.density
    return sp
}

fun readRawData(context: Context, resource_id: Int): ArrayList<ModelStringList> {
    val inputStream: InputStream = context.resources.openRawResource(resource_id)
    val items = arrayListOf<ModelStringList>()
    val json = Scanner(inputStream).useDelimiter("\\A").next()
    val array = Gson().fromJson(json, JsonElement::class.java).asJsonArray
    for (i in 0 until array.size()) {
        val obj = array[i].asJsonObject
        val model = ModelStringList()
        model.title = obj.get("title").asString
        model.description = obj.get("description").asString
        if (obj.has("activity")) {
            model.viewPath = obj.get("activity").asString
        }
        if (obj.has("viewType")) {
            model.viewType = obj.get("viewType").asString
        }
        items.add(model)
    }
    return items
}

fun getBitmap(context: Context, drawableId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(context, drawableId)

    return when (drawable) {
        is BitmapDrawable -> BitmapFactory.decodeResource(context.resources, drawableId)
        is VectorDrawable -> getBitmap(drawable)
        else -> throw IllegalArgumentException("unsupported drawable type")
    }
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun getBitmap(vectorDrawable: VectorDrawable): Bitmap {
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    vectorDrawable.draw(canvas)
    return bitmap
}

