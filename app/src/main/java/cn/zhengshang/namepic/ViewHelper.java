package cn.zhengshang.namepic;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by zhengshang on 2017/2/10.
 */

class ViewHelper {
    static final boolean HIDE = true;
    static final boolean SHOW = false;

    private static ArrayList<Uri> tmpShareUriList = new ArrayList<>();

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

    /**
     * A copy of the Android internals  insertImage method, this method populates the
     * meta data with DATE_ADDED and DATE_TAKEN. This fixes a common problem where media
     * that is inserted manually gets saved at the end of the gallery (because date is not populated).
     *
     * @see android.provider.MediaStore.Images.Media#insertImage(ContentResolver, Bitmap, String, String)
     */
    public static final String insertImage(ContentResolver cr,
                                           Bitmap source,
                                           String title,
                                           String description) {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                } finally {
                    imageOut.close();
                }

                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                storeThumbnail(cr, miniThumb, id, 50F, 50F, MediaStore.Images.Thumbnails.MICRO_KIND);
            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return stringUrl;
    }

    /**
     * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
     * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
     * meta data. The StoreThumbnail method is private so it must be duplicated here.
     *
     * @see android.provider.MediaStore.Images.Media (StoreThumbnail private method)
     */
    private static final Bitmap storeThumbnail(
            ContentResolver cr,
            Bitmap source,
            long id,
            float width,
            float height,
            int kind) {

        // create the matrix to scale it
        Matrix matrix = new Matrix();

        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
                source.getWidth(),
                source.getHeight(), matrix,
                true
        );

        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Images.Thumbnails.KIND, kind);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID, (int) id);
        values.put(MediaStore.Images.Thumbnails.HEIGHT, thumb.getHeight());
        values.put(MediaStore.Images.Thumbnails.WIDTH, thumb.getWidth());

        Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = cr.openOutputStream(url);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    static View generateButton(Context context, String text, Drawable background, View.OnClickListener listener) {
        Button button = new Button(context);
        button.setText(text);
        button.setTextColor(Color.WHITE);
        button.setBackground(background);
        button.setOnClickListener(listener);
        return button;
    }

    static void saveOrShare(Context context, FrameLayout layout, int type) {
        layout.destroyDrawingCache();
        layout.buildDrawingCache();
        Bitmap bitmap = layout.getDrawingCache();
        if (bitmap != null) {

            //content://media/external/images/media/49881
            String path = insertImage(context.getContentResolver(), bitmap, "title", "description");
            if (type == Constants.TYPE_SAVE) {
                Toast.makeText(context, TextUtils.isEmpty(path) ? "Save failed " : "Saved to gallery ", Toast.LENGTH_SHORT).show();
            } else if (type == Constants.TYPE_SHARE) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                Uri uri = Uri.parse(path);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                context.startActivity(Intent.createChooser(shareIntent, "请选择"));

                tmpShareUriList.add(uri);
            }
        }
    }

    /**
     * 删除点击分享时产生的临时文件
     */
    static void deleteSharePics(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        for (Uri uri : tmpShareUriList) {
            contentResolver.delete(uri, null, null);
        }
    }

    /**
     * 保存当前选中的radio的索引
     */
    static void saveGroupConfig(RadioGroup radioGroup, SharedPreferences.Editor editor) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.color_base_pic:
                editor.putInt(Constants.COLOR_SET_GROUP_INDEX, 0);
                break;
            case R.id.color_center_text:
                editor.putInt(Constants.COLOR_SET_GROUP_INDEX, 1);
                break;
            case R.id.rb_text_count:
                editor.putInt(Constants.TEXT_SET_GROUP_INDEX, 0);
                break;
            case R.id.rb_text_size:
                editor.putInt(Constants.TEXT_SET_GROUP_INDEX, 1);
                break;
            case R.id.rb_y_position:
                editor.putInt(Constants.TEXT_SET_Y_POSITION, 2);
                break;
        }
    }
}
