package cn.zhengshang.namepic.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.provider.MediaStore
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import cn.zhengshang.namepic.R
import cn.zhengshang.namepic.view.PicView
import java.io.ByteArrayOutputStream


/**
 * Created by shang on 2020/8/13.
 *            🐳🐳🐳🍒           17:34 🥥
 */
fun Toolbar.navClickBack() {
    setNavigationOnClickListener {
        findNavController().popBackStack()
    }
}

/**
 * Transform PicView's content to bitmap
 */
fun PicView.toBitmap(): Bitmap {
    val b = Bitmap.createBitmap(measuredWidth, measuredHeight,
            Bitmap.Config.ARGB_8888)
    val c = Canvas(b)
    layout(left, top, right, bottom)
    draw(c)
    return b
}

fun Bitmap.getUri(context: Context?): Uri {
    val bytes = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path: String = MediaStore.Images.Media.insertImage(
            context?.contentResolver,
            this,
            context?.getString(R.string.app_name) + System.currentTimeMillis(),
            null)
    return Uri.parse(path)
}