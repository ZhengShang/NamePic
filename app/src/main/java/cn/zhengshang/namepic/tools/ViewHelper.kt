package cn.zhengshang.namepic.tools

import android.content.*
import android.content.SharedPreferences.Editor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog.Builder
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import cn.zhengshang.namepic.R
import cn.zhengshang.namepic.presenter.TextSettings
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import java.io.FileNotFoundException
import java.io.IOException

/**
 * Created by zhengshang on 2017/2/10.
 */
object ViewHelper {
    const val HIDE = true
    const val SHOW = false
    private val tmpShareUriList = ArrayList<Uri>()
    /**
     * 隐藏或显示除了SeekBar以外的所有View
     *
     * @param hide hide or show
     */
    fun hideOrShowAllChild(linearLayout: LinearLayout, hide: Boolean) {
        for (i in 0 until linearLayout.childCount) {
            val view = linearLayout.getChildAt(i)
            if (view is LinearLayout) {
                if (view.getId() == R.id.header_layout) {
                    view.setVisibility(if (hide) View.INVISIBLE else View.VISIBLE)
                } else {
                    hideOrShowAllChild(view, hide)
                }
            } else {
                view.visibility = if (hide) View.INVISIBLE else View.VISIBLE
            }
        }
    }

    /**
     * 设置中间显示的文字
     */
    fun changeCenterText(context: Context, textSettings: TextSettings, centerText: TextView) {
        val editText = EditText(context)
        editText.filters = arrayOf<InputFilter>(LengthFilter(1))
        Builder(context)
                .setTitle(context.resources.getString(R.string.input_name_title))
                .setView(editText)
                .setPositiveButton(context.resources.getString(R.string.ok)) { _, _ ->
                    if (!TextUtils.isEmpty(editText.text)) {
                        textSettings.nameText = editText.text.toString()
                        centerText.text = textSettings.nameText
                    }
                }
                .setNegativeButton(context.resources.getString(R.string.cancel), null)
                .show()
        editText.post {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(editText, 0)
        }
    }

    fun initSeekBar(seekBar: DiscreteSeekBar, min: Int, max: Int) {
        seekBar.min = min
        seekBar.max = max
    }

    /**
     * A copy of the Android internals  insertImage method, this method populates the
     * meta data with DATE_ADDED and DATE_TAKEN. This fixes a common problem where media
     * that is inserted manually gets saved at the end of the gallery (because date is not populated).
     *
     * @see android.provider.MediaStore.Images.Media.insertImage
     */
    fun insertImage(cr: ContentResolver,
                    source: Bitmap?,
                    title: String?,
                    description: String?): String? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, title)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title)
        values.put(MediaStore.Images.Media.DESCRIPTION, description)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        var url: Uri? = null
        var stringUrl: String? = null /* value to be returned */
        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (source != null) {
                val imageOut = cr.openOutputStream(url!!)
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut)
                } finally {
                    imageOut!!.close()
                }
                val id = ContentUris.parseId(url)
                // Wait until MINI_KIND thumbnail is generated.
                val miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null)
                // This is for backward compatibility.
                storeThumbnail(cr, miniThumb, id, 50f, 50f, MediaStore.Images.Thumbnails.MICRO_KIND)
            } else {
                cr.delete(url!!, null, null)
                url = null
            }
        } catch (e: Exception) {
            if (url != null) {
                cr.delete(url, null, null)
                url = null
            }
        }
        if (url != null) {
            stringUrl = url.toString()
        }
        return stringUrl
    }

    /**
     * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
     * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
     * meta data. The StoreThumbnail method is private so it must be duplicated here.
     *
     * @see android.provider.MediaStore.Images.Media
     */
    private fun storeThumbnail(
            cr: ContentResolver,
            source: Bitmap,
            id: Long,
            width: Float,
            height: Float,
            kind: Int): Bitmap? { // create the matrix to scale it
        val matrix = Matrix()
        val scaleX = width / source.width
        val scaleY = height / source.height
        matrix.setScale(scaleX, scaleY)
        val thumb = Bitmap.createBitmap(source, 0, 0,
                source.width,
                source.height, matrix,
                true
        )
        val values = ContentValues(4)
        values.put(MediaStore.Images.Thumbnails.KIND, kind)
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID, id.toInt())
        values.put(MediaStore.Images.Thumbnails.HEIGHT, thumb.height)
        values.put(MediaStore.Images.Thumbnails.WIDTH, thumb.width)
        val url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values)
        return try {
            val thumbOut = cr.openOutputStream(url!!)
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut)
            thumbOut!!.close()
            thumb
        } catch (ex: FileNotFoundException) {
            null
        } catch (ex: IOException) {
            null
        }
    }

    fun generateButton(context: Context?, text: String?, background: Drawable?, listener: OnClickListener?): View {
        val button = Button(context)
        button.text = text
        button.setTextColor(Color.WHITE)
        button.background = background
        button.setOnClickListener(listener)
        return button
    }

    fun saveOrShare(context: Context, layout: FrameLayout, type: Int) {
        layout.destroyDrawingCache()
        layout.buildDrawingCache()
        val bitmap = layout.drawingCache
        if (bitmap != null) { //content://media/external/images/media/49881
            val path = insertImage(context.contentResolver, bitmap, "title", "description")
            if (type == Constants.TYPE_SAVE) {
                Toast.makeText(context, if (TextUtils.isEmpty(path)) R.string.save_failed else R.string.save_success, Toast.LENGTH_SHORT).show()
            } else if (type == Constants.TYPE_SHARE) {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "image/*"
                val uri = Uri.parse(path)
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.choose_share_to)))
                tmpShareUriList.add(uri)
            }
        }
    }

    /**
     * 删除点击分享时产生的临时文件
     */
    fun deleteSharePics(context: Context) {
        val contentResolver = context.contentResolver
        for (uri in tmpShareUriList) {
            contentResolver.delete(uri, null, null)
        }
    }

    /*
    初始化标题栏
     */
    fun initVIewToolbar(activity: AppCompatActivity, title: String?) {
        val toolbar = activity.findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.title = title
        toolbar.setNavigationOnClickListener { activity.finish() }
    }

    /**
     * 保存当前选中的radio的索引
     */
    fun saveGroupConfig(radioGroup: RadioGroup, editor: Editor) {
        when (radioGroup.checkedRadioButtonId) {
            R.id.color_base_pic -> editor.putInt(Constants.COLOR_SET_GROUP_INDEX, 0)
            R.id.color_center_text -> editor.putInt(Constants.COLOR_SET_GROUP_INDEX, 1)
            R.id.rb_text_count -> editor.putInt(Constants.TEXT_SET_GROUP_INDEX, 0)
            R.id.rb_text_size -> editor.putInt(Constants.TEXT_SET_GROUP_INDEX, 1)
            R.id.rb_y_position -> editor.putInt(Constants.TEXT_SET_Y_POSITION, 2)
        }
    }
}