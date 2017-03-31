package cn.zhengshang.namepic.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import cn.zhengshang.namepic.R;
import cn.zhengshang.namepic.presenter.ColorSettings;
import cn.zhengshang.namepic.presenter.TextSettings;
import cn.zhengshang.namepic.tools.Constants;
import cn.zhengshang.namepic.tools.ViewHelper;
import cn.zhengshang.namepic.view.DotMenuPopView;
import cn.zhengshang.namepic.view.PicView;
import cn.zhengshang.namepic.view.ScrollPicker;


public class MainActivity extends BaseActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, DiscreteSeekBar.OnProgressChangeListener,
        ScrollPicker.ScrollStopLiserer {
    private ColorSettings colorSettings;
    private TextSettings textSettings;
    private RadioButton rbBasePic, rbCenterText;
    private RadioGroup rgColor, rgText;
    private AppCompatRadioButton rbTextCount, rbTextSize, rbYPosition;
    private AppCompatCheckBox cbShowBackPic;
    private TextView tvColor11;
    private TextView centerText;
    private TextView currentTypeSeekBarValue; //当前选择的类型的数值
    private DiscreteSeekBar mSeekBar;
    private DrawerLayout drawer;
    private LinearLayout naviLayout, colorOptionsLayout;
    private NavigationView navigationView;
    private ScrollPicker scrollPicker;
    private FrameLayout contentMain;
    private ImageView backPicture;
    private FloatingActionMenu actionMenu;
    private DotMenuPopView dotMenuPopView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentMain = (FrameLayout) findViewById(R.id.content_main);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        actionMenu = new FloatingActionMenu.Builder(MainActivity.this)
                .addSubActionView(ViewHelper.generateButton(MainActivity.this,
                        getResources().getString(R.string.share), getResources().getDrawable(R.drawable.oval_blue), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ViewHelper.saveOrShare(MainActivity.this, contentMain, Constants.TYPE_SHARE);
                                fab.callOnClick();
                            }
                        }), 150, 150)
                .addSubActionView(ViewHelper.generateButton(MainActivity.this,
                        getResources().getString(R.string.save), getResources().getDrawable(R.drawable.oval_orange), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ViewHelper.saveOrShare(MainActivity.this, contentMain, Constants.TYPE_SAVE);
                                fab.callOnClick();
                            }
                        }), 150, 150)
                .attachTo(fab)
                .build();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionMenu.isOpen()) {
                    actionMenu.close(true);
                } else {
                    actionMenu.open(true);
                }
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        naviLayout = (LinearLayout) navigationView.getHeaderView(0);

        PicView picView = (PicView) findViewById(R.id.pic_view);
        colorSettings = new ColorSettings(picView);
        textSettings = new TextSettings(picView);

        rgColor = (RadioGroup) naviLayout.findViewById(R.id.rg_color);
        rbBasePic = (RadioButton) naviLayout.findViewById(R.id.color_base_pic);
        rbCenterText = (RadioButton) naviLayout.findViewById(R.id.color_center_text);
        rbBasePic.setOnCheckedChangeListener(this);
        rbCenterText.setOnCheckedChangeListener(this);

        colorOptionsLayout = (LinearLayout) naviLayout.findViewById(R.id.color_options_layout);
        TextView tvColor1 = (TextView) naviLayout.findViewById(R.id.color_option_1);
        TextView tvColor2 = (TextView) naviLayout.findViewById(R.id.color_option_2);
        TextView tvColor3 = (TextView) naviLayout.findViewById(R.id.color_option_3);
        TextView tvColor4 = (TextView) naviLayout.findViewById(R.id.color_option_4);
        TextView tvColor5 = (TextView) naviLayout.findViewById(R.id.color_option_5);
        TextView tvColor6 = (TextView) naviLayout.findViewById(R.id.color_option_6);
        TextView tvColor7 = (TextView) naviLayout.findViewById(R.id.color_option_7);
        TextView tvColor8 = (TextView) naviLayout.findViewById(R.id.color_option_8);
        TextView tvColor9 = (TextView) naviLayout.findViewById(R.id.color_option_9);
        TextView tvColor10 = (TextView) naviLayout.findViewById(R.id.color_option_10);
        tvColor11 = (TextView) naviLayout.findViewById(R.id.color_option_11);
        TextView tvColor12 = (TextView) naviLayout.findViewById(R.id.color_option_12);

        tvColor1.setOnClickListener(this);
        tvColor2.setOnClickListener(this);
        tvColor3.setOnClickListener(this);
        tvColor4.setOnClickListener(this);
        tvColor5.setOnClickListener(this);
        tvColor6.setOnClickListener(this);
        tvColor7.setOnClickListener(this);
        tvColor8.setOnClickListener(this);
        tvColor9.setOnClickListener(this);
        tvColor10.setOnClickListener(this);
        tvColor11.setOnClickListener(this);
        tvColor12.setOnClickListener(this);

        centerText = (TextView) naviLayout.findViewById(R.id.center_text);
        centerText.setOnClickListener(this);

        rgText = (RadioGroup) naviLayout.findViewById(R.id.rg_text);
        rbTextCount = (AppCompatRadioButton) naviLayout.findViewById(R.id.rb_text_count);
        rbTextSize = (AppCompatRadioButton) naviLayout.findViewById(R.id.rb_text_size);
        rbYPosition = (AppCompatRadioButton) naviLayout.findViewById(R.id.rb_y_position);
        rbTextCount.setOnCheckedChangeListener(this);
        rbTextSize.setOnCheckedChangeListener(this);
        rbYPosition.setOnCheckedChangeListener(this);
        currentTypeSeekBarValue = (TextView) naviLayout.findViewById(R.id.seek_bar_type_text);

        mSeekBar = (DiscreteSeekBar) naviLayout.findViewById(R.id.seek_bar);
        mSeekBar.setOnProgressChangeListener(this);

        scrollPicker = (ScrollPicker) naviLayout.findViewById(R.id.scroll_picker);
        scrollPicker.setDatas(Arrays.asList(getResources().getStringArray(R.array.fonts_name)));
        scrollPicker.setScrollStopLiserer(this);

        backPicture = (ImageView) findViewById(R.id.back_pic);
        cbShowBackPic = (AppCompatCheckBox) naviLayout.findViewById(R.id.show_back_pic);
        cbShowBackPic.setOnCheckedChangeListener(this);

        ImageView dotMenu = (ImageView) findViewById(R.id.dot_menu);
        dotMenu.setOnClickListener(this);
        dotMenuPopView = new DotMenuPopView(this);

        loadViewsStatus();
    }

    //加载控件初始状态
    private void loadViewsStatus() {
        SharedPreferences mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE);
        tvColor11.setBackgroundColor(mSharedPreferences.getInt(Constants.COLOR_SET_TV11, 0xff33b5e5));
        colorSettings.setBasePicColor(mSharedPreferences.getInt(Constants.COLOR_SET_BASE_COLOR, Constants.DEF_BASE_COLOR));
        colorSettings.setCenterTextColor(mSharedPreferences.getInt(Constants.COLOR_SET_TEXT_COLOR, Constants.DEF_TEXT_COLOR));
        ((RadioButton) rgColor.getChildAt(mSharedPreferences.getInt(Constants.COLOR_SET_GROUP_INDEX, 0))).setChecked(true);

        textSettings.setNameText(mSharedPreferences.getString(Constants.TEXT_SET_CENTER_TEXT, Constants.DEF_TEXT));
        centerText.setText(textSettings.getNameText());
        textSettings.setTextCount(mSharedPreferences.getInt(Constants.TEXT_SET_COUNT, Constants.TEXT_COUNT_DEF_VALUE));
        textSettings.setTextSize(mSharedPreferences.getInt(Constants.TEXT_SET_SIZE, Constants.TEXT_SIZE_DEF_VALUE));
        textSettings.setTextYPosition(mSharedPreferences.getInt(Constants.TEXT_SET_Y_POSITION, Constants.DEF_Y_POSITION));
        textSettings.setFont(getResources().getStringArray(R.array.fonts_file_name)[mSharedPreferences.getInt(Constants.TEXT_SET_FONT, 0)]);
        ((AppCompatRadioButton) rgText.getChildAt(mSharedPreferences.getInt(Constants.TEXT_SET_GROUP_INDEX, 0))).setChecked(true);

        backPicture.setVisibility(mSharedPreferences.getBoolean(Constants.PIC_SET_SHOW, Constants.DEF_PIC_SHOW) ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (actionMenu.isOpen()) {
            actionMenu.close(true);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (actionMenu.isOpen()) {
            actionMenu.close(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.color_option_12:
                ColorPickerDialogBuilder
                        .with(getApplicationContext())
                        .setTitle(getResources().getString(R.string.choose_color))
                        .initialColor(colorSettings.getBasePicColor())
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                tvColor11.setBackgroundColor(selectedColor);
                                doSetColor(selectedColor);
                            }
                        })
                        .build()
                        .show();
                break;
            case R.id.color_option_1:
            case R.id.color_option_2:
            case R.id.color_option_3:
            case R.id.color_option_4:
            case R.id.color_option_5:
            case R.id.color_option_6:
            case R.id.color_option_7:
            case R.id.color_option_8:
            case R.id.color_option_9:
            case R.id.color_option_10:
            case R.id.color_option_11:
                doSetColor(((ColorDrawable) v.getBackground()).getColor());
                break;
            case R.id.center_text:
                WeakReference<Context> weakReference = new WeakReference<Context>(this);
                ViewHelper.changeCenterText(weakReference.get(), textSettings, centerText);
                break;
            case R.id.dot_menu:
                dotMenuPopView.showAsDropDown(v);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
    }

    private void doSetColor(int color) {
        if (rbBasePic.isChecked()) {
            colorSettings.setBasePicColor(color);
        }

        if (rbCenterText.isChecked()) {
            colorSettings.setCenterTextColor(color);
        }
        setCheckText(color);
    }

    private void setCheckText(int color) {
        int count = 0;
        for (int i = 0; i < colorOptionsLayout.getChildCount(); i++) {
            LinearLayout layout = (LinearLayout) colorOptionsLayout.getChildAt(i);
            for (int i1 = 0; i1 < layout.getChildCount(); i1++) {
                count++;
                if (count == 12) {
                    return;
                }
                TextView view = (TextView) layout.getChildAt(i1);
                if (((ColorDrawable) view.getBackground()).getColor() == color) {
                    if (color == -1) {
                        view.setTextColor(Color.BLACK);
                    }
                    view.setText("✓");
                } else {
                    view.setText(null);
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == R.id.show_back_pic) {
            backPicture.setVisibility(compoundButton.isChecked() ? View.VISIBLE : View.GONE);
            return;
        }

        if (!compoundButton.isChecked()) {
            return;
        }
        switch (compoundButton.getId()) {
            case R.id.color_base_pic:
                setCheckText(colorSettings.getBasePicColor());
                return;
            case R.id.color_center_text:
                setCheckText(colorSettings.getTextColor());
                return;
            case R.id.rb_text_count:
                ViewHelper.initSeekBar(mSeekBar, Constants.TEXT_COUNT_MIN_VALUE, Constants.TEXT_COUNT_MAX_VALUE);
                mSeekBar.setProgress(textSettings.getTextCount());
                break;
            case R.id.rb_text_size:
                ViewHelper.initSeekBar(mSeekBar, Constants.TEXT_SIZE_MIN_VALUE, Constants.TEXT_SIZE_MAX_VALUE);
                mSeekBar.setProgress(textSettings.getTextSize());
                break;
            case R.id.rb_y_position:
                ViewHelper.initSeekBar(mSeekBar, Constants.Y_POSITION_MIN_VALUE, Constants.Y_POSITION_MAX_VALUE);
                mSeekBar.setProgress(textSettings.getYPosition());
                break;
        }
        currentTypeSeekBarValue.setText(String.format(getString(R.string.current_value), compoundButton.getText(), mSeekBar.getProgress()));
    }

    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
        if (!fromUser) {
            return;
        }
        if (rbTextCount.isChecked()) {
            textSettings.setTextCount(value);
        } else if (rbTextSize.isChecked()) {
            textSettings.setTextSize(value);
        } else if (rbYPosition.isChecked()) {
            textSettings.setTextYPosition(value);
        }
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
        navigationView.setBackgroundColor(0x00FFFFFF);
        ViewHelper.hideOrShowAllChild(naviLayout, ViewHelper.HIDE);
        ((LinearLayout) mSeekBar.getParent()).setBackground(null);
        mSeekBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
        navigationView.setBackgroundColor(0xFFFFFFFF);
        ((LinearLayout) mSeekBar.getParent()).setBackgroundResource(R.drawable.rec_border_gray);
        ViewHelper.hideOrShowAllChild(naviLayout, ViewHelper.SHOW);

        currentTypeSeekBarValue.setText(currentTypeSeekBarValue.getText().toString().replaceAll("-*\\d+", seekBar.getProgress() + "")); //替换数字
    }

    @Override
    public void onScrollStop(String selectString) {
        String s[] = getResources().getStringArray(R.array.fonts_file_name);
        textSettings.setFont(s[scrollPicker.getDataPickedIndex()]);
    }

    @Override
    protected void onStop() {
        SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE).edit();
        ViewHelper.saveGroupConfig(rgColor, editor);
        ViewHelper.saveGroupConfig(rgText, editor);
        editor.putInt(Constants.COLOR_SET_TV11, ((ColorDrawable) tvColor11.getBackground()).getColor());
        editor.putInt(Constants.COLOR_SET_BASE_COLOR, colorSettings.getBasePicColor());
        editor.putInt(Constants.COLOR_SET_TEXT_COLOR, colorSettings.getTextColor());
        editor.putString(Constants.TEXT_SET_CENTER_TEXT, textSettings.getNameText());
        editor.putInt(Constants.TEXT_SET_COUNT, textSettings.getTextCount());
        editor.putInt(Constants.TEXT_SET_SIZE, textSettings.getTextSize());
        editor.putInt(Constants.TEXT_SET_Y_POSITION, textSettings.getYPosition());
        editor.putBoolean(Constants.PIC_SET_SHOW, cbShowBackPic.isChecked());
        if (scrollPicker.hasUpdateFirstItem()) {
            editor.putInt(Constants.TEXT_SET_FONT, scrollPicker.getDataPickedIndex());
        }
        editor.apply();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ViewHelper.deleteSharePics(MainActivity.this);
        super.onDestroy();
    }
}
