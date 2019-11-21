package cn.zhengshang.namepic.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.FontMetrics
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import androidx.core.widget.ScrollerCompat
import cn.zhengshang.namepic.tools.Constants

class ScrollPicker : View {
    private var mScroller: ScrollerCompat? = null
    private var mVelocityTracker: VelocityTracker? = null
    private var scrollStopLiserer: ScrollStopLiserer? = null
    private var mMyHandlerThread: HandlerThread? = null
    private var mHandlerInMy: Handler? = null
    private val mFlingFriction = 1f
    private val mFlingMinVelocity = 150
    private var mCanWrap = false
    private var hasUpdateFirstItem = false
    private var mDefaultPickedIndex = 0
    private val mDefaultScrollByMills = 300
    private val mDefaultScrollByMillsMax = 1500
    private val mDefaultScrollByMillsMin = 300
    private var datas: List<String>? = null
    private var mViewWidth = 0
    private var mViewHeight = 0
    private var mViewCenterX = 0f
    private var mItemHeight = 0
    private var mCurrDrawFirstItemIndex = 0
    private var mCurrDrawFirstItemY = 0
    private var mCurrDrawGlobalY = 0
    private var mHalfTextHeight = 0f
    private val mScrollTouchSlot = 1
    private var mShowCount = 0
    private val mPaintText: Paint? = Paint()
    private val mStartValueColor = Color.GRAY
    private val mEndValueColor = Color.BLACK
    private val mPaintTextSize = 20f

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun init(context: Context) {
        mScroller = ScrollerCompat.create(context)
        mShowCount = 3
        mPaintText!!.color = mStartValueColor
        mPaintText.isAntiAlias = true
        mPaintText.textSize = mPaintTextSize
        mPaintText.textAlign = Paint.Align.CENTER
        mDefaultPickedIndex = context.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE).getInt(Constants.TEXT_SET_FONT, 0)
        initHandler()
    }

    private fun initHandler() {
        mMyHandlerThread = HandlerThread("HandlerThread_for_scroller_callback")
        mMyHandlerThread!!.start()
        mHandlerInMy = object : Handler(mMyHandlerThread!!.looper) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    HANDLER_WHAT_SCROLLING ->  //监听Scroller,如果滚动没结束,继续监听,如果结束,则修正结束时的位置
                        if (!mScroller!!.isFinished) {
                            mHandlerInMy!!.sendEmptyMessageDelayed(HANDLER_WHAT_SCROLLING, 32)
                        } else {
                            correctPickedPos()
                        }
                    HANDLER_WHAT_SCROLLING_END -> {
                    }
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var fraction = 0f // fraction of the item in state between normal and selected, in[0, 1]
        var textColor: Int
        var textSize: Float
        for (i in 0 until mShowCount + 1) {
            var index = mCurrDrawFirstItemIndex + i
            val y = mCurrDrawFirstItemY + i * mItemHeight + mItemHeight / 2 + mHalfTextHeight
            if (i == mShowCount / 2) {
                fraction = (mItemHeight + mCurrDrawFirstItemY).toFloat() / mItemHeight
                textColor = evaluate(fraction, mStartValueColor, mEndValueColor)
                textSize = getEvaluateSize(fraction, mPaintTextSize, mPaintTextSize + 30f)
            } else if (i == mShowCount / 2 + 1) {
                textColor = evaluate(1 - fraction, mStartValueColor, mEndValueColor)
                textSize = getEvaluateSize(1 - fraction, mPaintTextSize, mPaintTextSize + 30f)
            } else {
                textColor = mStartValueColor
                textSize = mPaintTextSize
            }
            mPaintText!!.color = textColor
            mPaintText.textSize = textSize
            if (!mCanWrap) { //不循环滚动
                if (0 <= index && index < datas!!.size) {
                    canvas.drawText(datas!![index], mViewCenterX, y, mPaintText)
                }
            } else { //循环滚动
                index = index % datas!!.size
                if (index < 0) {
                    index += datas!!.size
                }
                canvas.drawText(datas!![index], mViewCenterX, y, mPaintText)
            }
        }
    }

    private var isFirstInit = true
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h
        mViewCenterX = (mViewWidth + paddingLeft - paddingRight).toFloat() / 2
        mItemHeight = mViewHeight / mShowCount
        requireNotNull(mPaintText) { "mPaintText should not be null." }
        mPaintText.textSize = mPaintTextSize
        mHalfTextHeight = getTextCenterYOffset(mPaintText.fontMetrics)
        if (isFirstInit) {
            mScroller!!.startScroll(0, 0, 0, (mDefaultPickedIndex - mShowCount / 2) * mItemHeight, 0)
            isFirstInit = false
        }
    }

    /**
     * mScroller.startScroll,postInvalidate,会执行该方法。
     */
    override fun computeScroll() {
        super.computeScroll()
        //如果滚动还未结束,则先pudate参数,然后再postInvalidate
        if (mScroller!!.computeScrollOffset()) {
            mCurrDrawGlobalY = mScroller!!.currY
            updateFirstItemIndexAndY()
            postInvalidate()
        }
    }

    //根据mCurrDrawGlobalY得到第一个item的位置信息,后续的item根据第一个item推算得到
    private fun updateFirstItemIndexAndY() {
        hasUpdateFirstItem = true
        mCurrDrawFirstItemIndex = Math.floor(mCurrDrawGlobalY.toFloat() / mItemHeight.toDouble()).toInt()
        mCurrDrawFirstItemY = -(mCurrDrawGlobalY - mCurrDrawFirstItemIndex * mItemHeight)
    }

    private var mFlagMayPress = false
    private var mDownGlobalY = 0f
    private var mDownY = 0f
    private var mCurrY = 0f
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
        mCurrY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mFlagMayPress = true
                mDownY = mCurrY
                mDownGlobalY = mCurrDrawGlobalY.toFloat()
            }
            MotionEvent.ACTION_MOVE -> {
                val spanY = mDownY - mCurrY
                if (mFlagMayPress && -mScrollTouchSlot < spanY && spanY < mScrollTouchSlot) { //判定为点击
                } else { //判定为滑动
                    mFlagMayPress = false
                    mCurrDrawGlobalY = (mDownGlobalY + spanY).toInt()
                    mCurrDrawGlobalY = limitY(mCurrDrawGlobalY)
                    updateFirstItemIndexAndY()
                    postInvalidate()
                }
            }
            MotionEvent.ACTION_UP -> if (mFlagMayPress) { //点击
                mFlagMayPress = false
                click(event)
            } else { //滑动
                val velocityTracker = mVelocityTracker
                velocityTracker!!.computeCurrentVelocity(1000)
                val velocityY = (velocityTracker.yVelocity * mFlingFriction).toInt()
                if (Math.abs(velocityY) > mFlingMinVelocity) {
                    mScroller!!.fling(0, mCurrDrawGlobalY, 0, -velocityY,
                            Integer.MIN_VALUE, Integer.MAX_VALUE, limitY(Integer.MIN_VALUE), limitY(Integer.MAX_VALUE))
                    postInvalidate()
                }
                //滑动开始
                mHandlerInMy!!.sendEmptyMessage(HANDLER_WHAT_SCROLLING)
                releaseVelocityTracker()
            }
            MotionEvent.ACTION_CANCEL -> {
            }
        }
        return true
    }

    private fun releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker!!.clear()
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }

    private fun limitY(newGlobalY: Int): Int {
        var newGlobalY = newGlobalY
        if (!mCanWrap) { //不能滚动,限制滑动范围
            if (newGlobalY < -mItemHeight * (mShowCount / 2)) {
                newGlobalY = -mItemHeight * (mShowCount / 2)
            } else if (newGlobalY > mItemHeight * (datas!!.size - mShowCount / 2 - 1)) {
                newGlobalY = mItemHeight * (datas!!.size - mShowCount / 2 - 1)
            }
        }
        return newGlobalY
    }

    private fun click(event: MotionEvent) {
        val y = event.y
        for (i in 0 until mShowCount) {
            if (mItemHeight * i <= y && y < mItemHeight * (i + 1)) {
                smoothScrollByIndex(i - mShowCount / 2)
                break
            }
        }
    }

    //在Dialog中使用时需要注意这两个方法。
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //        if(mMyHandlerThread == null || !mMyHandlerThread.isAlive()) {
//            initHandler();
//        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //        mMyHandlerThread.quit();
    }

    //校准位置
    private fun correctPickedPos() {
        if (mCurrDrawFirstItemY != 0) {
            if (mCurrDrawFirstItemY < -(mItemHeight / 2)) {
                val dy = mCurrDrawFirstItemY + mItemHeight
                val duration = ((mItemHeight + mCurrDrawFirstItemY).toFloat() / mItemHeight * mDefaultScrollByMills).toInt()
                mScroller!!.startScroll(0, mCurrDrawGlobalY, 0, dy, duration * 3)
            } else {
                val dy = mCurrDrawFirstItemY
                val duration = ((-mCurrDrawFirstItemY).toFloat() / mItemHeight * mDefaultScrollByMills).toInt()
                mScroller!!.startScroll(0, mCurrDrawGlobalY, 0, dy, duration * 3)
            }
            postInvalidate()
        }
        if (scrollStopLiserer != null) {
            scrollStopLiserer!!.onScrollStop(selectedString)
        }
    }

    fun smoothScrollByIndex(deltaIndex: Int) { //不能循环滚动时,校准deltaIndex,不超出上下限
        var deltaIndex = deltaIndex
        if (!mCanWrap) {
            if (deltaIndex < -dataPickedIndex) {
                deltaIndex = -dataPickedIndex
            } else if (deltaIndex > datas!!.size - 1 - dataPickedIndex) {
                deltaIndex = datas!!.size - 1 - dataPickedIndex
            }
        }
        var dy = 0
        var duration = 0
        if (mCurrDrawFirstItemY < -(mItemHeight / 2)) {
            dy = mItemHeight + mCurrDrawFirstItemY
            duration = ((mItemHeight + mCurrDrawFirstItemY).toFloat() / mItemHeight * mDefaultScrollByMills).toInt()
            duration = if (deltaIndex < 0) {
                -duration - deltaIndex * mDefaultScrollByMills
            } else {
                duration + deltaIndex * mDefaultScrollByMills
            }
        } else {
            dy = mCurrDrawFirstItemY
            duration = ((-mCurrDrawFirstItemY).toFloat() / mItemHeight * mDefaultScrollByMills).toInt()
            duration = if (deltaIndex < 0) {
                duration - deltaIndex * mDefaultScrollByMills
            } else {
                -duration + deltaIndex * mDefaultScrollByMills
            }
        }
        dy = dy + deltaIndex * mItemHeight
        if (duration > mDefaultScrollByMillsMax) {
            duration = mDefaultScrollByMillsMax
        } else if (duration < mDefaultScrollByMillsMin) {
            duration = mDefaultScrollByMillsMin
        }
        mScroller!!.startScroll(0, mCurrDrawGlobalY, 0, dy, duration)
        postInvalidate()
    }

    fun hasUpdateFirstItem(): Boolean {
        return hasUpdateFirstItem
    }

    //获取当前选中的数据位置
    val dataPickedIndex: Int
        get() {
            val index: Int
            index = if (mCurrDrawFirstItemY != 0) {
                if (mCurrDrawFirstItemY < -mItemHeight / 2) {
                    mCurrDrawFirstItemIndex + 1 + mShowCount / 2
                } else {
                    mCurrDrawFirstItemIndex + mShowCount / 2
                }
            } else {
                mCurrDrawFirstItemIndex + mShowCount / 2
            }
            return index
        }

    /**
     * get the half height of text
     *
     * @param fontMetrics
     * @return
     */
    private fun getTextCenterYOffset(fontMetrics: FontMetrics?): Float {
        return if (fontMetrics == null) 0f else Math.abs(fontMetrics.top + fontMetrics.bottom) / 2
    }

    /**
     * copy from [android.animation.ArgbEvaluator.evaluate]
     */
    fun evaluate(fraction: Float, startValue: Int, endValue: Int): Int {
        val startA = startValue shr 24 and 0xff
        val startR = startValue shr 16 and 0xff
        val startG = startValue shr 8 and 0xff
        val startB = startValue and 0xff
        val endA = endValue shr 24 and 0xff
        val endR = endValue shr 16 and 0xff
        val endG = endValue shr 8 and 0xff
        val endB = endValue and 0xff
        return (startA + (fraction * (endA - startA)).toInt() shl 24) or
                (startR + (fraction * (endR - startR)).toInt() shl 16) or
                (startG + (fraction * (endG - startG)).toInt() shl 8) or
                (startB + (fraction * (endB - startB)).toInt())
    }

    private fun getEvaluateSize(fraction: Float, startSize: Float, endSize: Float): Float {
        return startSize + (endSize - startSize) * fraction
    }

    fun setCanWrap(wrap: Boolean) {
        mCanWrap = wrap
    }

    fun setDatas(datas: List<String>?) {
        this.datas = datas
    }

    val selectedString: String?
        get() {
            val result: String?
            result = try {
                datas!![dataPickedIndex]
            } catch (e: Exception) {
                null
            }
            return result
        }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(ev)
    }

    fun setScrollStopLiserer(liserer: ScrollStopLiserer?) {
        scrollStopLiserer = liserer
    }

    interface ScrollStopLiserer {
        /**
         * 返回当前滑动停止时所选择的字符串
         */
        fun onScrollStop(selectString: String?)
    }

    companion object {
        private const val HANDLER_WHAT_SCROLLING = 1
        private const val HANDLER_WHAT_SCROLLING_END = 2
    }
}