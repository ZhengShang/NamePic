package cn.zhengshang.namepic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhengshang on 2016/12/14.
 */

public class PicView extends View {
    private Paint mBgPaint = new Paint();
    private Paint mNameTextPaint = new Paint();
    private int mNameTextSize = Constants.TEXT_SIZE_DEF_VALUE;
    private int mNameTextCount = Constants.TEXT_COUNT_DEF_VALUE;
    private float mAvgAngle;
    private String mNameText = Constants.DEF_TEXT;
    private String mNameFont = Constants.DEF_TEXT_FONT;
    private int baseColor = Constants.DEF_BASE_COLOR;
    private int centerTextColor = Constants.DEF_TEXT_COLOR;
    private int mNameTextYPosition;
    private int yPosition = Constants.DEF_Y_POSITION;
    private int mTextRadius = Constants.DEF_TEXT_RADIUS;

    public PicView(Context context) {
        super(context);
        initPaints();
    }


    public PicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
        setFont(mNameFont);
    }

    public PicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
    }

    private void initPaints() {
        //initPaints Paints
        mBgPaint.setColor(baseColor);
        mBgPaint.setStyle(Paint.Style.FILL);

        mNameTextPaint.setColor(centerTextColor);
        mNameTextPaint.setTextSize(mNameTextSize);
        mNameTextPaint.setAntiAlias(true);

//        mPicPaint.setStyle(Paint.Style.FILL);
//        mPicPaint.setColor(Color.RED);
        //Init avgAngle
        mAvgAngle = 360 / mNameTextCount;

        //Init y position
        this.post(new Runnable() {
            @Override
            public void run() {
                mNameTextYPosition = getHeight() / 2 + yPosition;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float mHalfTextXcoor = getWidth() / 2 - mNameTextPaint.measureText(mNameText) / 2;

        //Draw Background
        canvas.drawRect(0, 0, getWidth(), getHeight(), mBgPaint);

        //Draw NameText
        for (int i = 0; i < mNameTextCount; i++) {
            canvas.drawText(mNameText, mHalfTextXcoor, mNameTextYPosition, mNameTextPaint);
            canvas.rotate(mAvgAngle, getWidth() / 2, mNameTextYPosition + mTextRadius);
            canvas.save();
        }

    }

    @Override
    public void postInvalidate() {
        initPaints();
        super.postInvalidate();
    }

    public void setFont(String fontName) {
        Typeface typeface = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/" + fontName);
        mNameTextPaint.setTypeface(typeface);
    }

    public int getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(int baseColor) {
        this.baseColor = baseColor;
    }

    public int getCenterTextColor() {
        return centerTextColor;
    }

    public void setCenterTextColor(int textColor) {
        this.centerTextColor = textColor;
    }

    public int getNameTextSize() {
        return mNameTextSize;
    }

    public void setNameTextSize(int mNameTextSize) {
        this.mNameTextSize = mNameTextSize;
    }

    public int getNameTextCount() {
        return mNameTextCount;
    }

    public void setNameTextCount(int mNameTextCount) {
        this.mNameTextCount = mNameTextCount;
    }

    public String getNameText() {
        return mNameText;
    }

    public void setNameText(String mNameText) {
        this.mNameText = mNameText;
    }

    public void setYPosition(int position) {
        this.yPosition = position;
    }

    public int getYPosition() {
        return yPosition;
    }

    public String getmNameFont() {
        return mNameFont;
    }
}
