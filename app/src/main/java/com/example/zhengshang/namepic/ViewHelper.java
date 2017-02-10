package com.example.zhengshang.namepic;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

/**
 * Created by zhengshang on 2017/2/10.
 */

class ViewHelper {
    static final boolean HIDE = true;
    static final boolean SHOW = false;

    /**
     * 隐藏或显示除了SeekBar以外的所有View
     *
     * @param hide hide or show
     */
    static void hideOrShowAllChild(LinearLayout linearLayout, boolean hide) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View view = linearLayout.getChildAt(i);
            if (view instanceof LinearLayout) {
                if (view.getId() == R.id.header_layout) {
                    view.setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
                } else {
                    hideOrShowAllChild((LinearLayout) view, hide);
                }
            } else {
                view.setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
            }
        }
    }

    /**
     * 设置中间显示的文字
     */
    static void changeCenterText(Context context, final TextSettings textSettings, final TextView centerText) {
        final EditText editText = new EditText(context);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

        new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.input_name_title))
                .setView(editText)
                .setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!TextUtils.isEmpty(editText.getText())) {
                            textSettings.setNameText(editText.getText().toString());
                            centerText.setText(textSettings.getNameText());
                        }
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();

    }

    static void initSeekBar(DiscreteSeekBar seekBar, int min, int max) {
        seekBar.setMin(min);
        seekBar.setMax(max);
    }
}
