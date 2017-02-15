package com.example.zhengshang.namepic;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, DiscreteSeekBar.OnProgressChangeListener,
        ScrollPicker.ScrollStopLiserer {
    private ColorSettings colorSettings;
    private TextSettings textSettings;
    private RadioButton rbBasePic, rbCenterText;
    private AppCompatRadioButton rbTextCount, rbTextSize, rbYPosition;
    private TextView tvColor1, tvColor2, tvColor3, tvColor4, tvColor5, tvColor6, tvColor7, tvColor8, tvColor9, tvColor10, tvColor11, tvColor12; // 12 color options
    private TextView centerText;
    private TextView currentTypeSeekBarValue; //当前选择的类型的数值
    private DiscreteSeekBar mSeekBar;
    private DrawerLayout drawer;
    private LinearLayout naviLayout;
    private NavigationView navigationView;
    private ScrollPicker scrollPicker;
    private FrameLayout contentMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentMain = (FrameLayout) findViewById(R.id.content_main);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        final FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(MainActivity.this)
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

        rbBasePic = (RadioButton) naviLayout.findViewById(R.id.color_base_pic);
        rbCenterText = (RadioButton) naviLayout.findViewById(R.id.color_center_text);

        tvColor1 = (TextView) naviLayout.findViewById(R.id.color_option_1);
        tvColor2 = (TextView) naviLayout.findViewById(R.id.color_option_2);
        tvColor3 = (TextView) naviLayout.findViewById(R.id.color_option_3);
        tvColor4 = (TextView) naviLayout.findViewById(R.id.color_option_4);
        tvColor5 = (TextView) naviLayout.findViewById(R.id.color_option_5);
        tvColor6 = (TextView) naviLayout.findViewById(R.id.color_option_6);
        tvColor7 = (TextView) naviLayout.findViewById(R.id.color_option_7);
        tvColor8 = (TextView) naviLayout.findViewById(R.id.color_option_8);
        tvColor9 = (TextView) naviLayout.findViewById(R.id.color_option_9);
        tvColor10 = (TextView) naviLayout.findViewById(R.id.color_option_10);
        tvColor11 = (TextView) naviLayout.findViewById(R.id.color_option_11);
        tvColor12 = (TextView) naviLayout.findViewById(R.id.color_option_12);

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

        rbTextCount.setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.color_option_12:
                ColorPickerDialogBuilder
                        .with(this)
                        .setTitle(getResources().getString(R.string.choose_color))
                        .initialColor(colorSettings.getBasePicColor())
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
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
                tvColor1.setText(null);
                tvColor2.setText(null);
                tvColor3.setText(null);
                tvColor4.setText(null);
                tvColor5.setText(null);
                tvColor6.setText(null);
                tvColor7.setText(null);
                tvColor8.setText(null);
                tvColor9.setText(null);
                tvColor10.setText(null);
                tvColor11.setText(null);
                ((TextView) v).setText("✓");
                doSetColor(((ColorDrawable) v.getBackground()).getColor());
                break;
            case R.id.center_text:
                ViewHelper.changeCenterText(MainActivity.this, textSettings, centerText);
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
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (!compoundButton.isChecked()) {
            return;
        }
        switch (compoundButton.getId()) {
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
    protected void onDestroy() {
        ViewHelper.deleteSharePics(MainActivity.this);
        super.onDestroy();
    }
}
