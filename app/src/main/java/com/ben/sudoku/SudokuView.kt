package com.ben.sudoku

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

/**
 * 数独视图
 *
 * @author Benhero
 * @date 2018/7/11
 */
class SudokuView : View {
    private val mSourcePaint = Paint()
    private val mInputPaint = Paint()
    private val mTipsPaint = Paint()
    private val mBoldLinePaint = Paint()
    private val mThinLinePaint = Paint()
    private var mRectSize: Int = 0
    private var mPanelMarginLeft: Int = 0
    private var mPanelMarginTop: Int = 0
    private var mTextSize: Int = 0
    private var mTextBaseline: Int = 0
    private val mNoteTextBaseline = IntArray(3)
    private val mSudokuPresenter = SudokuPresenter()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mSourcePaint.isAntiAlias = true
        mSourcePaint.textAlign = Paint.Align.CENTER

        mInputPaint.isAntiAlias = true
        mInputPaint.textAlign = Paint.Align.CENTER
        mInputPaint.color = resources.getColor(R.color.colorPrimary)

        mTipsPaint.isAntiAlias = true
        mTipsPaint.strokeWidth = 5f
        mTipsPaint.textAlign = Paint.Align.CENTER

        mBoldLinePaint.strokeWidth = 5f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mRectSize = w / 10
        val rectPadding = mRectSize / 5
        mPanelMarginLeft = mRectSize / 2
        mPanelMarginTop = (h - w) / 2 + mPanelMarginLeft
        mTextSize = mRectSize - rectPadding * 2
        mSourcePaint.textSize = mTextSize.toFloat()
        mInputPaint.textSize = mTextSize.toFloat()

        val fontMetrics = mSourcePaint.fontMetricsInt
        mTextBaseline = (mRectSize - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top

        val noteFontMetrics = mTipsPaint.fontMetricsInt
        for (i in mNoteTextBaseline.indices) {
            mNoteTextBaseline[i] = (mRectSize / 3 * (i * 2 + 1) - noteFontMetrics.bottom + noteFontMetrics.top) / 2 - noteFontMetrics.top
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = canvas.width
        val height = canvas.height

        for (i in 0 until SudokuPresenter.GRID_COUNT) {
            val column = i % 9
            val row = i / 9

            val gridBean = mSudokuPresenter.getGrid(i)
            if (gridBean.source != 0) {
                drawSource(canvas, gridBean.source, column, row)
            } else if (gridBean.input != 0) {
                drawInput(canvas, gridBean.input, column, row)
            } else {
                drawTips(canvas, gridBean.tips, column, row)
            }
        }
        for (i in 0..9) {
            canvas.drawLine(mPanelMarginLeft.toFloat(), (mPanelMarginTop + mRectSize * i).toFloat(),
                    (width - mPanelMarginLeft).toFloat(), (mPanelMarginTop + mRectSize * i).toFloat(),
                    if (i % 3 == 0) mBoldLinePaint else mThinLinePaint)
        }
        for (i in 0..9) {
            canvas.drawLine((mPanelMarginLeft + mRectSize * i).toFloat(), mPanelMarginTop.toFloat(),
                    (mPanelMarginLeft + mRectSize * i).toFloat(), (height - mPanelMarginTop).toFloat(),
                    if (i % 3 == 0) mBoldLinePaint else mThinLinePaint)
        }
    }

    /**
     * 绘制原始的数字
     */
    private fun drawSource(canvas: Canvas, num: Int, column: Int, row: Int) {
        canvas.drawText(num.toString(),
                (mPanelMarginLeft + column * mRectSize + mRectSize / 2).toFloat(),
                (mPanelMarginTop + mTextBaseline + row * mRectSize).toFloat(),
                mSourcePaint)
    }

    /**
     * 绘制输入的数字
     */
    private fun drawInput(canvas: Canvas, num: Int, column: Int, row: Int) {
        canvas.drawText(num.toString(),
                (mPanelMarginLeft + column * mRectSize + mRectSize / 2).toFloat(),
                (mPanelMarginTop + mTextBaseline + row * mRectSize).toFloat(),
                mInputPaint)
    }

    /**
     * 绘制提示数字
     */
    private fun drawTips(canvas: Canvas, tips: IntArray, column: Int, row: Int) {
        for (j in 0..8) {
            if (tips[j] == 0) {
                continue
            }
            canvas.drawText((j + 1).toString(),
                    (mPanelMarginLeft + column * mRectSize + mRectSize / 6 * (j % 3 * 2 + 1)).toFloat(),
                    (mPanelMarginTop + mNoteTextBaseline[j / 3] + row * mRectSize).toFloat(),
                    mTipsPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mSudokuPresenter.checkInput()) {
            mSudokuPresenter.reset()
        } else {
            mSudokuPresenter.updateTips()
            if (mSudokuPresenter.checkInput()) {
                Toast.makeText(context, "Suc", Toast.LENGTH_SHORT).show()
            }
        }
        postInvalidate()
        return super.onTouchEvent(event)
    }
}
