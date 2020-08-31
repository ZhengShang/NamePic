package cn.zhengshang.namepic.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import cn.zhengshang.namepic.tools.*

/**
 * Created by zhengshang on 2016/12/14.
 */
class PicView : View {
    private val mBgPaint = Paint()
    private val mNameTextPaint = Paint()
    var nameTextSize = TEXT_SIZE_DEF_VALUE
    var nameTextCount = TEXT_COUNT_DEF_VALUE
    private var mAvgAngle = 0f
    var nameText = DEF_TEXT
    private val mNameFont = DEF_TEXT_FONT
    var baseColor = DEF_BASE_COLOR
    var centerTextColor = DEF_TEXT_COLOR
    private var mNameTextYPosition = 0
    var yPosition = DEF_Y_POSITION
    private val mTextRadius = DEF_TEXT_RADIUS

    constructor(context: Context?) : super(context) {
        initPaints()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initPaints()
        setFont(mNameFont)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initPaints()
    }

    private fun initPaints() { //initPaints Paints
        mBgPaint.color = baseColor
        mBgPaint.style = Paint.Style.FILL
        mNameTextPaint.color = centerTextColor
        mNameTextPaint.textSize = nameTextSize.toFloat()
        mNameTextPaint.isAntiAlias = true
        //        mPicPaint.setStyle(Paint.Style.FILL);
//        mPicPaint.setColor(Color.RED);
//Init avgAngle
        mAvgAngle = 360 / nameTextCount.toFloat()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        //Set y position
        mNameTextYPosition = height / 2 + yPosition
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val mHalfTextXcoor = width / 2 - mNameTextPaint.measureText(nameText) / 2
        //Draw Background
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), mBgPaint)
        //Draw NameText
        for (i in 0 until nameTextCount) {
            canvas.drawText(nameText, mHalfTextXcoor, mNameTextYPosition.toFloat(), mNameTextPaint)
            canvas.rotate(mAvgAngle, width / 2.toFloat(), mNameTextYPosition + mTextRadius.toFloat())
            canvas.save()
        }
    }

    override fun postInvalidate() {
        initPaints()
        super.postInvalidate()
    }

    fun setFont(fontName: String) {
        val typeface = Typeface.createFromAsset(this.context.assets, "fonts/$fontName")
        mNameTextPaint.typeface = typeface
    }

    fun getNameFont(): String {
        return mNameFont
    }
}