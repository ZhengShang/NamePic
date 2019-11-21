package cn.zhengshang.namepic.activity

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import cn.zhengshang.namepic.R
import cn.zhengshang.namepic.presenter.ColorSettings
import cn.zhengshang.namepic.presenter.TextSettings
import cn.zhengshang.namepic.tools.Constants
import cn.zhengshang.namepic.tools.ViewHelper
import cn.zhengshang.namepic.view.DotMenuPopView
import cn.zhengshang.namepic.view.PicView
import cn.zhengshang.namepic.view.ScrollPicker
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import java.lang.ref.WeakReference


class MainActivity : BaseActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener, DiscreteSeekBar.OnProgressChangeListener, ScrollPicker.ScrollStopLiserer {
    private lateinit var colorSettings: ColorSettings
    private lateinit var textSettings: TextSettings
    private lateinit var rbBasePic: RadioButton
    private lateinit var rbCenterText: RadioButton
    private lateinit var rgColor: RadioGroup
    private lateinit var rgText: RadioGroup
    private lateinit var rbTextCount: AppCompatRadioButton
    private lateinit var rbTextSize: AppCompatRadioButton
    private lateinit var rbYPosition: AppCompatRadioButton
    private lateinit var cbShowBackPic: AppCompatCheckBox
    private lateinit var tvColor11: TextView
    private lateinit var centerText: TextView
    private lateinit var currentTypeSeekBarValue: TextView
    private lateinit var mSeekBar: DiscreteSeekBar
    private lateinit var drawer: DrawerLayout
    private lateinit var navLayout: LinearLayout
    private lateinit var colorOptionsLayout: LinearLayout
    private lateinit var navigationView: NavigationView
    private lateinit var scrollPicker: ScrollPicker
    private lateinit var contentMain: FrameLayout
    private lateinit var backPicture: ImageView
    private lateinit var actionMenu: FloatingActionMenu
    private lateinit var dotMenuPopView: DotMenuPopView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contentMain = findViewById<View>(R.id.content_main) as FrameLayout

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton

        actionMenu = FloatingActionMenu.Builder(this)
                .addSubActionView(ViewHelper.generateButton(this,
                        resources.getString(R.string.share), resources.getDrawable(R.drawable.oval_blue), View.OnClickListener { view ->
                    fab.callOnClick()
                    view.postDelayed({ ViewHelper.saveOrShare(this, contentMain, Constants.TYPE_SHARE) }, 400)
                }), 150, 150)
                .addSubActionView(ViewHelper.generateButton(this,
                        resources.getString(R.string.save), resources.getDrawable(R.drawable.oval_orange), View.OnClickListener { view ->
                    fab.callOnClick()
                    view.postDelayed({ ViewHelper.saveOrShare(this, contentMain, Constants.TYPE_SAVE) }, 400)
                }), 150, 150)
                .attachTo(fab)
                .build()

        fab.setOnClickListener {
            if (actionMenu.isOpen) {
                actionMenu.close(true)
            } else {
                actionMenu.open(true)
            }
        }

        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navLayout = navigationView.getHeaderView(0) as LinearLayout

        val picView = findViewById<View>(R.id.pic_view) as PicView
        colorSettings = ColorSettings(picView)
        textSettings = TextSettings(picView)

        rgColor = navLayout.findViewById<View>(R.id.rg_color) as RadioGroup
        rbBasePic = navLayout.findViewById<View>(R.id.color_base_pic) as RadioButton
        rbCenterText = navLayout.findViewById<View>(R.id.color_center_text) as RadioButton
        rbBasePic.setOnCheckedChangeListener(this)
        rbCenterText.setOnCheckedChangeListener(this)

        colorOptionsLayout = navLayout.findViewById<View>(R.id.color_options_layout) as LinearLayout
        val tvColor1 = navLayout.findViewById<View>(R.id.color_option_1) as TextView
        val tvColor2 = navLayout.findViewById<View>(R.id.color_option_2) as TextView
        val tvColor3 = navLayout.findViewById<View>(R.id.color_option_3) as TextView
        val tvColor4 = navLayout.findViewById<View>(R.id.color_option_4) as TextView
        val tvColor5 = navLayout.findViewById<View>(R.id.color_option_5) as TextView
        val tvColor6 = navLayout.findViewById<View>(R.id.color_option_6) as TextView
        val tvColor7 = navLayout.findViewById<View>(R.id.color_option_7) as TextView
        val tvColor8 = navLayout.findViewById<View>(R.id.color_option_8) as TextView
        val tvColor9 = navLayout.findViewById<View>(R.id.color_option_9) as TextView
        val tvColor10 = navLayout.findViewById<View>(R.id.color_option_10) as TextView
        tvColor11 = navLayout.findViewById<View>(R.id.color_option_11) as TextView
        val tvColor12 = navLayout.findViewById<View>(R.id.color_option_12) as TextView

        tvColor1.setOnClickListener(this)
        tvColor2.setOnClickListener(this)
        tvColor3.setOnClickListener(this)
        tvColor4.setOnClickListener(this)
        tvColor5.setOnClickListener(this)
        tvColor6.setOnClickListener(this)
        tvColor7.setOnClickListener(this)
        tvColor8.setOnClickListener(this)
        tvColor9.setOnClickListener(this)
        tvColor10.setOnClickListener(this)
        tvColor11.setOnClickListener(this)
        tvColor12.setOnClickListener(this)

        centerText = navLayout.findViewById<View>(R.id.center_text) as TextView
        centerText.setOnClickListener(this)

        rgText = navLayout.findViewById<View>(R.id.rg_text) as RadioGroup
        rbTextCount = navLayout.findViewById<View>(R.id.rb_text_count) as AppCompatRadioButton
        rbTextSize = navLayout.findViewById<View>(R.id.rb_text_size) as AppCompatRadioButton
        rbYPosition = navLayout.findViewById<View>(R.id.rb_y_position) as AppCompatRadioButton
        rbTextCount.setOnCheckedChangeListener(this)
        rbTextSize.setOnCheckedChangeListener(this)
        rbYPosition.setOnCheckedChangeListener(this)
        currentTypeSeekBarValue = navLayout.findViewById<View>(R.id.seek_bar_type_text) as TextView

        mSeekBar = navLayout.findViewById<View>(R.id.seek_bar) as DiscreteSeekBar
        mSeekBar.setOnProgressChangeListener(this)

        scrollPicker = navLayout.findViewById<View>(R.id.scroll_picker) as ScrollPicker
        scrollPicker.setDatas(listOf(*resources.getStringArray(R.array.fonts_name)))
        scrollPicker.setScrollStopLiserer(this)

        backPicture = findViewById<View>(R.id.back_pic) as ImageView
        cbShowBackPic = navLayout.findViewById<View>(R.id.show_back_pic) as AppCompatCheckBox
        cbShowBackPic.setOnCheckedChangeListener(this)

        val dotMenu = findViewById<View>(R.id.dot_menu) as ImageView
        dotMenu.setOnClickListener(this)
        dotMenuPopView = DotMenuPopView(this)

        loadViewsStatus()
    }

    //加载控件初始状态
    private fun loadViewsStatus() {
        val mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        tvColor11.setBackgroundColor(mSharedPreferences.getInt(Constants.COLOR_SET_TV11, -0xcc4a1b))
        colorSettings.basePicColor = mSharedPreferences.getInt(Constants.COLOR_SET_BASE_COLOR, Constants.DEF_BASE_COLOR)
        colorSettings.setCenterTextColor(mSharedPreferences.getInt(Constants.COLOR_SET_TEXT_COLOR, Constants.DEF_TEXT_COLOR))
        (rgColor.getChildAt(mSharedPreferences.getInt(Constants.COLOR_SET_GROUP_INDEX, 0)) as RadioButton).isChecked = true

        textSettings.nameText = mSharedPreferences.getString(Constants.TEXT_SET_CENTER_TEXT, Constants.DEF_TEXT)
        centerText.text = textSettings.nameText
        this.textSettings.textCount = mSharedPreferences.getInt(Constants.TEXT_SET_COUNT, Constants.TEXT_COUNT_DEF_VALUE)
        textSettings.textSize = mSharedPreferences.getInt(Constants.TEXT_SET_SIZE, Constants.TEXT_SIZE_DEF_VALUE)
        textSettings.setTextYPosition(mSharedPreferences.getInt(Constants.TEXT_SET_Y_POSITION, Constants.DEF_Y_POSITION))
        textSettings.font = resources.getStringArray(R.array.fonts_file_name)[mSharedPreferences.getInt(Constants.TEXT_SET_FONT, 0)]
        (rgText.getChildAt(mSharedPreferences.getInt(Constants.TEXT_SET_GROUP_INDEX, 0)) as AppCompatRadioButton).isChecked = true

        backPicture.visibility = if (mSharedPreferences.getBoolean(Constants.PIC_SET_SHOW, Constants.DEF_PIC_SHOW)) View.VISIBLE else View.GONE
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (actionMenu.isOpen) {
            actionMenu.close(true)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
        //        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        when {
            drawer.isDrawerOpen(GravityCompat.START) -> {
                drawer.closeDrawer(GravityCompat.START)
            }
            actionMenu.isOpen -> {
                actionMenu.close(true)
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.color_option_12 -> ColorPickerDialogBuilder
                    .with(this)
                    .setTitle(resources.getString(R.string.choose_color))
                    .initialColor(colorSettings.basePicColor)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener { selectedColor ->
                        tvColor11.setBackgroundColor(selectedColor)
                        doSetColor(selectedColor)
                    }
                    .build()
                    .show()
            R.id.color_option_1, R.id.color_option_2, R.id.color_option_3, R.id.color_option_4, R.id.color_option_5, R.id.color_option_6, R.id.color_option_7, R.id.color_option_8, R.id.color_option_9, R.id.color_option_10, R.id.color_option_11 -> doSetColor((v.background as ColorDrawable).color)
            R.id.center_text -> {
                val weakReference = WeakReference<Context>(this)
                weakReference.get()?.let { ViewHelper.changeCenterText(it, textSettings, centerText) }
            }
            R.id.dot_menu -> dotMenuPopView.showAsDropDown(v)
        }

        drawer.closeDrawer(GravityCompat.START)
    }

    private fun doSetColor(color: Int) {
        if (rbBasePic.isChecked) {
            colorSettings.basePicColor = color
        }

        if (rbCenterText.isChecked) {
            colorSettings.setCenterTextColor(color)
        }
        setCheckText(color)
    }

    private fun setCheckText(color: Int) {
        var count = 0
        for (i in 0 until colorOptionsLayout.childCount) {
            val layout = colorOptionsLayout.getChildAt(i) as LinearLayout
            for (i1 in 0 until layout.childCount) {
                count++
                if (count == 12) {
                    return
                }
                val view = layout.getChildAt(i1) as TextView
                if ((view.background as ColorDrawable).color == color) {
                    if (color == -1) {
                        view.setTextColor(Color.BLACK)
                    }
                    view.text = "✓"
                } else {
                    view.text = null
                }
            }
        }
    }

    override fun onCheckedChanged(compoundButton: CompoundButton, b: Boolean) {
        if (compoundButton.id == R.id.show_back_pic) {
            backPicture.visibility = if (compoundButton.isChecked) View.VISIBLE else View.GONE
            return
        }

        if (!compoundButton.isChecked) {
            return
        }
        when (compoundButton.id) {
            R.id.color_base_pic -> {
                setCheckText(colorSettings.basePicColor)
                return
            }
            R.id.color_center_text -> {
                setCheckText(colorSettings.textColor)
                return
            }
            R.id.rb_text_count -> {
                ViewHelper.initSeekBar(mSeekBar, Constants.TEXT_COUNT_MIN_VALUE, Constants.TEXT_COUNT_MAX_VALUE)
                mSeekBar.progress = textSettings.textCount
            }
            R.id.rb_text_size -> {
                ViewHelper.initSeekBar(mSeekBar, Constants.TEXT_SIZE_MIN_VALUE, Constants.TEXT_SIZE_MAX_VALUE)
                mSeekBar.progress = textSettings.textSize
            }
            R.id.rb_y_position -> {
                ViewHelper.initSeekBar(mSeekBar, Constants.Y_POSITION_MIN_VALUE, Constants.Y_POSITION_MAX_VALUE)
                mSeekBar.progress = textSettings.yPosition
            }
        }
        currentTypeSeekBarValue.text = String.format(getString(R.string.current_value), compoundButton.text, mSeekBar.progress)
    }

    override fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) {
        if (!fromUser) {
            return
        }
        when {
            rbTextCount.isChecked -> {
                textSettings.textCount = value
            }
            rbTextSize.isChecked -> {
                textSettings.textSize = value
            }
            rbYPosition.isChecked -> {
                textSettings.setTextYPosition(value)
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: DiscreteSeekBar) {
        navigationView.setBackgroundColor(0x00FFFFFF)
        ViewHelper.hideOrShowAllChild(navLayout, ViewHelper.HIDE)
        (mSeekBar.parent as LinearLayout).background = null
        mSeekBar.visibility = View.VISIBLE
    }

    override fun onStopTrackingTouch(seekBar: DiscreteSeekBar) {
        navigationView.setBackgroundColor(-0x1)
        (mSeekBar.parent as LinearLayout).setBackgroundResource(R.drawable.rec_border_gray)
        ViewHelper.hideOrShowAllChild(navLayout, ViewHelper.SHOW)

        currentTypeSeekBarValue.text = currentTypeSeekBarValue.text.toString().replace("-*\\d+".toRegex(), seekBar.progress.toString() + "") //替换数字
    }

    override fun onStop() {
        val editor = getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE).edit()
        ViewHelper.saveGroupConfig(rgColor, editor)
        ViewHelper.saveGroupConfig(rgText, editor)
        editor.putInt(Constants.COLOR_SET_TV11, (tvColor11.background as ColorDrawable).color)
        editor.putInt(Constants.COLOR_SET_BASE_COLOR, colorSettings.basePicColor)
        editor.putInt(Constants.COLOR_SET_TEXT_COLOR, colorSettings.textColor)
        editor.putString(Constants.TEXT_SET_CENTER_TEXT, textSettings.nameText)
        editor.putInt(Constants.TEXT_SET_COUNT, textSettings.textCount)
        editor.putInt(Constants.TEXT_SET_SIZE, textSettings.textSize)
        editor.putInt(Constants.TEXT_SET_Y_POSITION, textSettings.yPosition)
        editor.putBoolean(Constants.PIC_SET_SHOW, cbShowBackPic.isChecked)
        if (scrollPicker.hasUpdateFirstItem()) {
            editor.putInt(Constants.TEXT_SET_FONT, scrollPicker.dataPickedIndex)
        }
        editor.apply()
        super.onStop()
    }

    override fun onDestroy() {
        val weakReference = WeakReference(this)
        ViewHelper.deleteSharePics(weakReference.get() as Context)
        super.onDestroy()
    }

    override fun onScrollStop(selectString: String?) {
        val s = resources.getStringArray(R.array.fonts_file_name)
        textSettings.font = s[scrollPicker.dataPickedIndex]
    }
}
