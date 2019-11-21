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
import java.util.*


class MainActivity : BaseActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener, DiscreteSeekBar.OnProgressChangeListener, ScrollPicker.ScrollStopLiserer {
    private var colorSettings: ColorSettings? = null
    private var textSettings: TextSettings? = null
    private var rbBasePic: RadioButton? = null
    private var rbCenterText: RadioButton? = null
    private var rgColor: RadioGroup? = null
    private var rgText: RadioGroup? = null
    private var rbTextCount: AppCompatRadioButton? = null
    private var rbTextSize: AppCompatRadioButton? = null
    private var rbYPosition: AppCompatRadioButton? = null
    private var cbShowBackPic: AppCompatCheckBox? = null
    private var tvColor11: TextView? = null
    private var centerText: TextView? = null
    private var currentTypeSeekBarValue: TextView? = null //当前选择的类型的数值
    private var mSeekBar: DiscreteSeekBar? = null
    private var drawer: DrawerLayout? = null
    private var naviLayout: LinearLayout? = null
    private var colorOptionsLayout: LinearLayout? = null
    private var navigationView: NavigationView? = null
    private var scrollPicker: ScrollPicker? = null
    private var contentMain: FrameLayout? = null
    private var backPicture: ImageView? = null
    private var actionMenu: FloatingActionMenu? = null
    private var dotMenuPopView: DotMenuPopView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contentMain = findViewById<View>(R.id.content_main) as FrameLayout

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton

        actionMenu = FloatingActionMenu.Builder(this@MainActivity)
                .addSubActionView(ViewHelper.generateButton(this@MainActivity,
                        resources.getString(R.string.share), resources.getDrawable(R.drawable.oval_blue)) { view ->
                    fab.callOnClick()
                    view.postDelayed({ ViewHelper.saveOrShare(this@MainActivity, contentMain!!, Constants.TYPE_SHARE) }, 400)
                }, 150, 150)
                .addSubActionView(ViewHelper.generateButton(this@MainActivity,
                        resources.getString(R.string.save), resources.getDrawable(R.drawable.oval_orange)) { view ->
                    fab.callOnClick()
                    view.postDelayed({ ViewHelper.saveOrShare(this@MainActivity, contentMain!!, Constants.TYPE_SAVE) }, 400)
                }, 150, 150)
                .attachTo(fab)
                .build()

        fab.setOnClickListener {
            if (actionMenu!!.isOpen) {
                actionMenu!!.close(true)
            } else {
                actionMenu!!.open(true)
            }
        }

        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        naviLayout = navigationView!!.getHeaderView(0) as LinearLayout

        val picView = findViewById<View>(R.id.pic_view) as PicView
        colorSettings = ColorSettings(picView)
        textSettings = TextSettings(picView)

        rgColor = naviLayout!!.findViewById<View>(R.id.rg_color) as RadioGroup
        rbBasePic = naviLayout!!.findViewById<View>(R.id.color_base_pic) as RadioButton
        rbCenterText = naviLayout!!.findViewById<View>(R.id.color_center_text) as RadioButton
        rbBasePic!!.setOnCheckedChangeListener(this)
        rbCenterText!!.setOnCheckedChangeListener(this)

        colorOptionsLayout = naviLayout!!.findViewById<View>(R.id.color_options_layout) as LinearLayout
        val tvColor1 = naviLayout!!.findViewById<View>(R.id.color_option_1) as TextView
        val tvColor2 = naviLayout!!.findViewById<View>(R.id.color_option_2) as TextView
        val tvColor3 = naviLayout!!.findViewById<View>(R.id.color_option_3) as TextView
        val tvColor4 = naviLayout!!.findViewById<View>(R.id.color_option_4) as TextView
        val tvColor5 = naviLayout!!.findViewById<View>(R.id.color_option_5) as TextView
        val tvColor6 = naviLayout!!.findViewById<View>(R.id.color_option_6) as TextView
        val tvColor7 = naviLayout!!.findViewById<View>(R.id.color_option_7) as TextView
        val tvColor8 = naviLayout!!.findViewById<View>(R.id.color_option_8) as TextView
        val tvColor9 = naviLayout!!.findViewById<View>(R.id.color_option_9) as TextView
        val tvColor10 = naviLayout!!.findViewById<View>(R.id.color_option_10) as TextView
        tvColor11 = naviLayout!!.findViewById<View>(R.id.color_option_11) as TextView
        val tvColor12 = naviLayout!!.findViewById<View>(R.id.color_option_12) as TextView

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
        tvColor11!!.setOnClickListener(this)
        tvColor12.setOnClickListener(this)

        centerText = naviLayout!!.findViewById<View>(R.id.center_text) as TextView
        centerText!!.setOnClickListener(this)

        rgText = naviLayout!!.findViewById<View>(R.id.rg_text) as RadioGroup
        rbTextCount = naviLayout!!.findViewById<View>(R.id.rb_text_count) as AppCompatRadioButton
        rbTextSize = naviLayout!!.findViewById<View>(R.id.rb_text_size) as AppCompatRadioButton
        rbYPosition = naviLayout!!.findViewById<View>(R.id.rb_y_position) as AppCompatRadioButton
        rbTextCount!!.setOnCheckedChangeListener(this)
        rbTextSize!!.setOnCheckedChangeListener(this)
        rbYPosition!!.setOnCheckedChangeListener(this)
        currentTypeSeekBarValue = naviLayout!!.findViewById<View>(R.id.seek_bar_type_text) as TextView

        mSeekBar = naviLayout!!.findViewById<View>(R.id.seek_bar) as DiscreteSeekBar
        mSeekBar!!.setOnProgressChangeListener(this)

        scrollPicker = naviLayout!!.findViewById<View>(R.id.scroll_picker) as ScrollPicker
        scrollPicker!!.setDatas(Arrays.asList(*resources.getStringArray(R.array.fonts_name)))
        scrollPicker!!.setScrollStopLiserer(this)

        backPicture = findViewById<View>(R.id.back_pic) as ImageView
        cbShowBackPic = naviLayout!!.findViewById<View>(R.id.show_back_pic) as AppCompatCheckBox
        cbShowBackPic!!.setOnCheckedChangeListener(this)

        val dotMenu = findViewById<View>(R.id.dot_menu) as ImageView
        dotMenu.setOnClickListener(this)
        dotMenuPopView = DotMenuPopView(this)

        loadViewsStatus()
    }

    //加载控件初始状态
    private fun loadViewsStatus() {
        val mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        tvColor11!!.setBackgroundColor(mSharedPreferences.getInt(Constants.COLOR_SET_TV11, -0xcc4a1b))
        colorSettings!!.basePicColor = mSharedPreferences.getInt(Constants.COLOR_SET_BASE_COLOR, Constants.DEF_BASE_COLOR)
        colorSettings!!.setCenterTextColor(mSharedPreferences.getInt(Constants.COLOR_SET_TEXT_COLOR, Constants.DEF_TEXT_COLOR))
        (rgColor!!.getChildAt(mSharedPreferences.getInt(Constants.COLOR_SET_GROUP_INDEX, 0)) as RadioButton).isChecked = true

        textSettings!!.nameText = mSharedPreferences.getString(Constants.TEXT_SET_CENTER_TEXT, Constants.DEF_TEXT)
        centerText!!.text = textSettings!!.nameText
        textSettings!!.textCount = mSharedPreferences.getInt(Constants.TEXT_SET_COUNT, Constants.TEXT_COUNT_DEF_VALUE)
        textSettings!!.textSize = mSharedPreferences.getInt(Constants.TEXT_SET_SIZE, Constants.TEXT_SIZE_DEF_VALUE)
        textSettings!!.setTextYPosition(mSharedPreferences.getInt(Constants.TEXT_SET_Y_POSITION, Constants.DEF_Y_POSITION))
        textSettings!!.font = resources.getStringArray(R.array.fonts_file_name)[mSharedPreferences.getInt(Constants.TEXT_SET_FONT, 0)]
        (rgText!!.getChildAt(mSharedPreferences.getInt(Constants.TEXT_SET_GROUP_INDEX, 0)) as AppCompatRadioButton).isChecked = true

        backPicture!!.visibility = if (mSharedPreferences.getBoolean(Constants.PIC_SET_SHOW, Constants.DEF_PIC_SHOW)) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (actionMenu!!.isOpen) {
            actionMenu!!.close(true)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
        //        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)
        } else if (actionMenu!!.isOpen) {
            actionMenu!!.close(true)
        } else {
            super.onBackPressed()
        }
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.color_option_12 -> ColorPickerDialogBuilder
                    .with(this)
                    .setTitle(resources.getString(R.string.choose_color))
                    .initialColor(colorSettings!!.basePicColor)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener { selectedColor ->
                        tvColor11!!.setBackgroundColor(selectedColor)
                        doSetColor(selectedColor)
                    }
                    .build()
                    .show()
            R.id.color_option_1, R.id.color_option_2, R.id.color_option_3, R.id.color_option_4, R.id.color_option_5, R.id.color_option_6, R.id.color_option_7, R.id.color_option_8, R.id.color_option_9, R.id.color_option_10, R.id.color_option_11 -> doSetColor((v.background as ColorDrawable).color)
            R.id.center_text -> {
                val weakReference = WeakReference<Context>(this)
                ViewHelper.changeCenterText(weakReference.get(), textSettings, centerText)
            }
            R.id.dot_menu -> dotMenuPopView!!.showAsDropDown(v)
        }

        drawer!!.closeDrawer(GravityCompat.START)
    }

    private fun doSetColor(color: Int) {
        if (rbBasePic!!.isChecked) {
            colorSettings!!.basePicColor = color
        }

        if (rbCenterText!!.isChecked) {
            colorSettings!!.setCenterTextColor(color)
        }
        setCheckText(color)
    }

    private fun setCheckText(color: Int) {
        var count = 0
        for (i in 0 until colorOptionsLayout!!.childCount) {
            val layout = colorOptionsLayout!!.getChildAt(i) as LinearLayout
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
            backPicture!!.visibility = if (compoundButton.isChecked) View.VISIBLE else View.GONE
            return
        }

        if (!compoundButton.isChecked) {
            return
        }
        when (compoundButton.id) {
            R.id.color_base_pic -> {
                setCheckText(colorSettings!!.basePicColor)
                return
            }
            R.id.color_center_text -> {
                setCheckText(colorSettings!!.textColor)
                return
            }
            R.id.rb_text_count -> {
                ViewHelper.initSeekBar(mSeekBar!!, Constants.TEXT_COUNT_MIN_VALUE, Constants.TEXT_COUNT_MAX_VALUE)
                mSeekBar!!.progress = textSettings!!.textCount
            }
            R.id.rb_text_size -> {
                ViewHelper.initSeekBar(mSeekBar!!, Constants.TEXT_SIZE_MIN_VALUE, Constants.TEXT_SIZE_MAX_VALUE)
                mSeekBar!!.progress = textSettings!!.textSize
            }
            R.id.rb_y_position -> {
                ViewHelper.initSeekBar(mSeekBar!!, Constants.Y_POSITION_MIN_VALUE, Constants.Y_POSITION_MAX_VALUE)
                mSeekBar!!.progress = textSettings!!.yPosition
            }
        }
        currentTypeSeekBarValue!!.setText(String.format(getString(R.string.current_value), compoundButton.text, mSeekBar!!.progress))
    }

    override fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) {
        if (!fromUser) {
            return
        }
        if (rbTextCount!!.isChecked) {
            textSettings!!.textCount = value
        } else if (rbTextSize!!.isChecked) {
            textSettings!!.textSize = value
        } else if (rbYPosition!!.isChecked) {
            textSettings!!.setTextYPosition(value)
        }
    }

    override fun onStartTrackingTouch(seekBar: DiscreteSeekBar) {
        navigationView!!.setBackgroundColor(0x00FFFFFF)
        ViewHelper.hideOrShowAllChild(naviLayout!!, ViewHelper.HIDE)
        (mSeekBar!!.parent as LinearLayout).background = null
        mSeekBar!!.visibility = View.VISIBLE
    }

    override fun onStopTrackingTouch(seekBar: DiscreteSeekBar) {
        navigationView!!.setBackgroundColor(-0x1)
        (mSeekBar!!.parent as LinearLayout).setBackgroundResource(R.drawable.rec_border_gray)
        ViewHelper.hideOrShowAllChild(naviLayout!!, ViewHelper.SHOW)

        currentTypeSeekBarValue!!.setText(currentTypeSeekBarValue!!.text.toString().replace("-*\\d+".toRegex(), seekBar.progress.toString() + "")) //替换数字
    }

    override fun onScrollStop(selectString: String) {
        val s = resources.getStringArray(R.array.fonts_file_name)
        textSettings!!.font = s[scrollPicker!!.dataPickedIndex]
    }

    override fun onStop() {
        val editor = getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE).edit()
        ViewHelper.saveGroupConfig(rgColor!!, editor)
        ViewHelper.saveGroupConfig(rgText!!, editor)
        editor.putInt(Constants.COLOR_SET_TV11, (tvColor11!!.background as ColorDrawable).color)
        editor.putInt(Constants.COLOR_SET_BASE_COLOR, colorSettings!!.basePicColor)
        editor.putInt(Constants.COLOR_SET_TEXT_COLOR, colorSettings!!.textColor)
        editor.putString(Constants.TEXT_SET_CENTER_TEXT, textSettings!!.nameText)
        editor.putInt(Constants.TEXT_SET_COUNT, textSettings!!.textCount)
        editor.putInt(Constants.TEXT_SET_SIZE, textSettings!!.textSize)
        editor.putInt(Constants.TEXT_SET_Y_POSITION, textSettings!!.yPosition)
        editor.putBoolean(Constants.PIC_SET_SHOW, cbShowBackPic!!.isChecked)
        if (scrollPicker!!.hasUpdateFirstItem()) {
            editor.putInt(Constants.TEXT_SET_FONT, scrollPicker!!.dataPickedIndex)
        }
        editor.apply()
        super.onStop()
    }

    override fun onDestroy() {
        val weakReference = WeakReference(this)
        ViewHelper.deleteSharePics(weakReference.get() as Context)
        super.onDestroy()
    }
}
