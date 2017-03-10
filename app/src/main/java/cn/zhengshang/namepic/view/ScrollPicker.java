package cn.zhengshang.namepic.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import java.util.List;

import cn.zhengshang.namepic.tools.Constants;

import static android.content.Context.MODE_PRIVATE;

public class ScrollPicker extends View {

    private static final int HANDLER_WHAT_SCROLLING = 1;
    private static final int HANDLER_WHAT_SCROLLING_END = 2;

    private ScrollerCompat mScroller;
    private VelocityTracker mVelocityTracker;
    private ScrollStopLiserer scrollStopLiserer;

    private HandlerThread mMyHandlerThread;
    private Handler mHandlerInMy;

    private float mFlingFriction = 1f;
    private int mFlingMinVelocity = 150;

    private boolean mCanWrap = false;
    private boolean hasUpdateFirstItem;
    private int mDefaultPickedIndex = 0;

    private int mDefaultScrollByMills = 300;
    private int mDefaultScrollByMillsMax = 1500;
    private int mDefaultScrollByMillsMin = 300;

    private List<String> datas;

    private int mViewWidth;
    private int mViewHeight;
    private float mViewCenterX;
    private int mItemHeight;

    private int mCurrDrawFirstItemIndex = 0;
    private int mCurrDrawFirstItemY = 0;
    private int mCurrDrawGlobalY = 0;

    private float mHalfTextHeight;

    private int mScrollTouchSlot = 1;

    private int mShowCount;

    private Paint mPaintText = new Paint();
    private int mStartValueColor = Color.GRAY;
    private int mEndValueColor = Color.BLACK;
    private float mPaintTextSize = 20f;

    public ScrollPicker(Context context) {
        super(context);
        init(context);
    }

    public ScrollPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        mScroller = ScrollerCompat.create(context);
        mShowCount = 3;

        mPaintText.setColor(mStartValueColor);
        mPaintText.setAntiAlias(true);
        mPaintText.setTextSize(mPaintTextSize);
        mPaintText.setTextAlign(Paint.Align.CENTER);

        mDefaultPickedIndex = context.getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE).getInt(Constants.TEXT_SET_FONT, 0);
        initHandler();
    }

    private void initHandler() {
        mMyHandlerThread = new HandlerThread("HandlerThread_for_scroller_callback");
        mMyHandlerThread.start();
        mHandlerInMy = new Handler(mMyHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case HANDLER_WHAT_SCROLLING:
                        //监听Scroller,如果滚动没结束,继续监听,如果结束,则修正结束时的位置
                        if (!mScroller.isFinished()) {
                            mHandlerInMy.sendEmptyMessageDelayed(HANDLER_WHAT_SCROLLING, 32);
                        } else {
                            correctPickedPos();
                        }
                        break;
                    case HANDLER_WHAT_SCROLLING_END:

                        break;
                }
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float fraction = 0f;// fraction of the item in state between normal and selected, in[0, 1]
        int textColor;
        float textSize;
        for (int i = 0; i < mShowCount + 1; i++) {
            int index = mCurrDrawFirstItemIndex + i;
            float y = mCurrDrawFirstItemY + i * mItemHeight + mItemHeight / 2 + mHalfTextHeight;
            if (i == mShowCount / 2) {
                fraction = (float) (mItemHeight + mCurrDrawFirstItemY) / mItemHeight;
                textColor = evaluate(fraction, mStartValueColor, mEndValueColor);
                textSize = getEvaluateSize(fraction, mPaintTextSize, mPaintTextSize + 30f);
            } else if (i == mShowCount / 2 + 1) {
                textColor = evaluate(1 - fraction, mStartValueColor, mEndValueColor);
                textSize = getEvaluateSize(1 - fraction, mPaintTextSize, mPaintTextSize + 30f);
            } else {
                textColor = mStartValueColor;
                textSize = mPaintTextSize;
            }
            mPaintText.setColor(textColor);
            mPaintText.setTextSize(textSize);
            if (!mCanWrap) {//不循环滚动
                if (0 <= index && index < datas.size()) {
                    canvas.drawText(datas.get(index), mViewCenterX, y, mPaintText);
                }
            } else {//循环滚动
                index = index % datas.size();
                if (index < 0) {
                    index += datas.size();
                }
                canvas.drawText(datas.get(index), mViewCenterX, y, mPaintText);
            }

        }
    }

    private boolean isFirstInit = true;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        mViewCenterX = ((float) (mViewWidth + getPaddingLeft() - getPaddingRight())) / 2;
        mItemHeight = mViewHeight / mShowCount;

        if (mPaintText == null) {
            throw new IllegalArgumentException("mPaintText should not be null.");
        }
        mPaintText.setTextSize(mPaintTextSize);
        mHalfTextHeight = getTextCenterYOffset(mPaintText.getFontMetrics());

        if (isFirstInit) {
            mScroller.startScroll(0, 0, 0, (mDefaultPickedIndex - mShowCount / 2) * mItemHeight, 0);
            isFirstInit = false;
        }

    }

    /**
     * mScroller.startScroll,postInvalidate,会执行该方法。
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        //如果滚动还未结束,则先pudate参数,然后再postInvalidate
        if (mScroller.computeScrollOffset()) {
            mCurrDrawGlobalY = mScroller.getCurrY();
            updateFirstItemIndexAndY();
            postInvalidate();
        }
    }

    //根据mCurrDrawGlobalY得到第一个item的位置信息,后续的item根据第一个item推算得到
    private void updateFirstItemIndexAndY() {
        hasUpdateFirstItem = true;
        mCurrDrawFirstItemIndex = (int) Math.floor((float) mCurrDrawGlobalY / mItemHeight);
        mCurrDrawFirstItemY = -(mCurrDrawGlobalY - mCurrDrawFirstItemIndex * mItemHeight);
    }

    private boolean mFlagMayPress = false;
    private float mDownGlobalY = 0;
    private float mDownY = 0;
    private float mCurrY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        mCurrY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFlagMayPress = true;
                mDownY = mCurrY;
                mDownGlobalY = mCurrDrawGlobalY;
                break;
            case MotionEvent.ACTION_MOVE:
                float spanY = mDownY - mCurrY;
                if (mFlagMayPress && (-mScrollTouchSlot < spanY && spanY < mScrollTouchSlot)) {
                    //判定为点击
                } else {
                    //判定为滑动
                    mFlagMayPress = false;
                    mCurrDrawGlobalY = (int) (mDownGlobalY + spanY);
                    mCurrDrawGlobalY = limitY(mCurrDrawGlobalY);
                    updateFirstItemIndexAndY();
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mFlagMayPress) {//点击
                    mFlagMayPress = false;
                    click(event);
                } else {//滑动
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000);
                    int velocityY = (int) (velocityTracker.getYVelocity() * mFlingFriction);
                    if (Math.abs(velocityY) > mFlingMinVelocity) {
                        mScroller.fling(0, mCurrDrawGlobalY, 0, -velocityY,
                                Integer.MIN_VALUE, Integer.MAX_VALUE, limitY(Integer.MIN_VALUE), limitY(Integer.MAX_VALUE));
                        postInvalidate();
                    }
                    //滑动开始
                    mHandlerInMy.sendEmptyMessage(HANDLER_WHAT_SCROLLING);
                    releaseVelocityTracker();

                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private int limitY(int newGlobalY) {
        if (!mCanWrap) {//不能滚动,限制滑动范围
            if (newGlobalY < -mItemHeight * (mShowCount / 2)) {
                newGlobalY = -mItemHeight * (mShowCount / 2);
            } else if (newGlobalY > mItemHeight * (datas.size() - mShowCount / 2 - 1)) {
                newGlobalY = mItemHeight * (datas.size() - mShowCount / 2 - 1);
            }
        }
        return newGlobalY;
    }

    private void click(MotionEvent event) {
        float y = event.getY();
        for (int i = 0; i < mShowCount; i++) {
            if (mItemHeight * i <= y && y < mItemHeight * (i + 1)) {
                smoothScrollByIndex(i - mShowCount / 2);
                break;
            }
        }
    }

    //在Dialog中使用时需要注意这两个方法。
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        if(mMyHandlerThread == null || !mMyHandlerThread.isAlive()) {
//            initHandler();
//        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        mMyHandlerThread.quit();
    }

    //校准位置
    private void correctPickedPos() {
        if (mCurrDrawFirstItemY != 0) {
            if (mCurrDrawFirstItemY < -(mItemHeight / 2)) {
                int dy = mCurrDrawFirstItemY + mItemHeight;
                int duration = (int) (((float) (mItemHeight + mCurrDrawFirstItemY)) / mItemHeight * mDefaultScrollByMills);
                mScroller.startScroll(0, mCurrDrawGlobalY, 0, dy, duration * 3);
            } else {
                int dy = mCurrDrawFirstItemY;
                int duration = (int) (((float) (-mCurrDrawFirstItemY)) / mItemHeight * mDefaultScrollByMills);
                mScroller.startScroll(0, mCurrDrawGlobalY, 0, dy, duration * 3);
            }
            postInvalidate();
        }
        if (scrollStopLiserer != null) {
            scrollStopLiserer.onScrollStop(getSelectedString());
        }
    }

    public void smoothScrollByIndex(int deltaIndex) {
        //不能循环滚动时,校准deltaIndex,不超出上下限
        if (!mCanWrap) {
            if (deltaIndex < -getDataPickedIndex()) {
                deltaIndex = -getDataPickedIndex();
            } else if (deltaIndex > datas.size() - 1 - getDataPickedIndex()) {
                deltaIndex = datas.size() - 1 - getDataPickedIndex();
            }
        }
        int dy = 0;
        int duration = 0;
        if (mCurrDrawFirstItemY < -(mItemHeight / 2)) {
            dy = mItemHeight + mCurrDrawFirstItemY;
            duration = (int) (((float) (mItemHeight + mCurrDrawFirstItemY)) / mItemHeight * mDefaultScrollByMills);
            if (deltaIndex < 0) {
                duration = -duration - deltaIndex * mDefaultScrollByMills;
            } else {
                duration = duration + deltaIndex * mDefaultScrollByMills;
            }
        } else {
            dy = mCurrDrawFirstItemY;
            duration = (int) (((float) (-mCurrDrawFirstItemY)) / mItemHeight * mDefaultScrollByMills);
            if (deltaIndex < 0) {
                duration = duration - deltaIndex * mDefaultScrollByMills;
            } else {
                duration = -duration + deltaIndex * mDefaultScrollByMills;
            }
        }
        dy = dy + deltaIndex * mItemHeight;
        if (duration > mDefaultScrollByMillsMax) {
            duration = mDefaultScrollByMillsMax;
        } else if (duration < mDefaultScrollByMillsMin) {
            duration = mDefaultScrollByMillsMin;
        }
        mScroller.startScroll(0, mCurrDrawGlobalY, 0, dy, duration);
        postInvalidate();
    }

    public boolean hasUpdateFirstItem() {
        return hasUpdateFirstItem;
    }

    //获取当前选中的数据位置
    public int getDataPickedIndex() {
        int index;
        if (mCurrDrawFirstItemY != 0) {
            if (mCurrDrawFirstItemY < (-mItemHeight / 2)) {
                index = mCurrDrawFirstItemIndex + 1 + mShowCount / 2;
            } else {
                index = mCurrDrawFirstItemIndex + mShowCount / 2;
            }
        } else {
            index = mCurrDrawFirstItemIndex + mShowCount / 2;
        }
        return index;
    }

    /**
     * get the half height of text
     *
     * @param fontMetrics
     * @return
     */
    private float getTextCenterYOffset(Paint.FontMetrics fontMetrics) {
        if (fontMetrics == null) return 0;
        return Math.abs(fontMetrics.top + fontMetrics.bottom) / 2;
    }

    /**
     * copy from {@link android.animation.ArgbEvaluator#evaluate(float, Object, Object)}
     */
    public int evaluate(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return (int) ((startA + (int) (fraction * (endA - startA))) << 24) |
                (int) ((startR + (int) (fraction * (endR - startR))) << 16) |
                (int) ((startG + (int) (fraction * (endG - startG))) << 8) |
                (int) ((startB + (int) (fraction * (endB - startB))));
    }

    private float getEvaluateSize(float fraction, float startSize, float endSize) {
        return startSize + (endSize - startSize) * fraction;
    }

    public void setCanWrap(boolean wrap) {
        this.mCanWrap = wrap;
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
    }

    public String getSelectedString() {
        String result;
        try {
            result = datas.get(getDataPickedIndex());
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    public void setScrollStopLiserer(ScrollStopLiserer liserer) {
        this.scrollStopLiserer = liserer;
    }

    public interface ScrollStopLiserer {
        /**
         * 返回当前滑动停止时所选择的字符串
         */
        void onScrollStop(String selectString);
    }
}