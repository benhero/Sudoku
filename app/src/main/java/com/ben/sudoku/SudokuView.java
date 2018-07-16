package com.ben.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 数独视图
 *
 * @author chenbenbin
 * @date 2018/7/11
 */
public class SudokuView extends View {
    private Paint mSourcePaint = new Paint();
    private Paint mInputPaint = new Paint();
    private Paint mTipsPaint = new Paint();
    private Paint mBoldLinePaint = new Paint();
    private Paint mThinLinePaint = new Paint();
    private int mRectSize;
    private int mPanelMarginLeft;
    private int mPanelMarginTop;
    private int mTextSize;
    private int mTextBaseline;
    private int[] mNoteTextBaseline = new int[3];
    private SudokuPresenter mSudokuPresenter = new SudokuPresenter();

    public SudokuView(Context context) {
        super(context);
        init();
    }

    public SudokuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SudokuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mSourcePaint.setAntiAlias(true);
        mSourcePaint.setTextAlign(Paint.Align.CENTER);

        mInputPaint.setTextAlign(Paint.Align.CENTER);
        mInputPaint.setColor(getResources().getColor(R.color.colorPrimary));

        mTipsPaint.setAntiAlias(true);
        mTipsPaint.setStrokeWidth(5);
        mTipsPaint.setTextAlign(Paint.Align.CENTER);

        mBoldLinePaint.setStrokeWidth(5);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mRectSize = w / 10;
        int rectPadding = mRectSize / 5;
        mPanelMarginLeft = mRectSize / 2;
        mPanelMarginTop = (h - w) / 2 + mPanelMarginLeft;
        mTextSize = mRectSize - rectPadding * 2;
        mSourcePaint.setTextSize(mTextSize);

        Paint.FontMetricsInt fontMetrics = mSourcePaint.getFontMetricsInt();
        mTextBaseline = (mRectSize - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;

        Paint.FontMetricsInt noteFontMetrics = mTipsPaint.getFontMetricsInt();
        for (int i = 0; i < mNoteTextBaseline.length; i++) {
            mNoteTextBaseline[i] = (mRectSize / 3 * (i * 2 + 1) - noteFontMetrics.bottom + noteFontMetrics.top) / 2 -
                    noteFontMetrics.top;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        for (int i = 0; i < SudokuPresenter.GRID_COUNT; i++) {
            int column = i % 9;
            int row = i / 9;

            GridBean gridBean = mSudokuPresenter.getGrid(i);
            if (gridBean.getSource() != 0) {
                drawSource(canvas, gridBean.getSource(), column, row);
            } else if (gridBean.getInput() != 0) {
                drawInput(canvas, gridBean.getInput(), column, row);
            } else {
                drawTips(canvas, gridBean.getTips(), column, row);
            }
        }
        for (int i = 0; i < 10; i++) {
            canvas.drawLine(mPanelMarginLeft, mPanelMarginTop + mRectSize * i, width - mPanelMarginLeft, mPanelMarginTop +
                    mRectSize * i, i % 3 == 0 ?
                    mBoldLinePaint : mThinLinePaint);
        }
        for (int i = 0; i < 10; i++) {
            canvas.drawLine(mPanelMarginLeft + mRectSize * i, mPanelMarginTop, mPanelMarginLeft + mRectSize * i, height -
                    mPanelMarginTop, i % 3 == 0 ?
                    mBoldLinePaint : mThinLinePaint);
        }
    }

    /**
     * 绘制原始的数字
     */
    private void drawSource(Canvas canvas, int num, int column, int row) {
        canvas.drawText(String.valueOf(num),
                mPanelMarginLeft + column * mRectSize + mRectSize / 2,
                mPanelMarginTop + mTextBaseline + row * mRectSize,
                mSourcePaint);
    }

    /**
     * 绘制输入的数字
     */
    private void drawInput(Canvas canvas, int num, int column, int row) {
        canvas.drawText(String.valueOf(num),
                mPanelMarginLeft + column * mRectSize + mRectSize / 2,
                mPanelMarginTop + mTextBaseline + row * mRectSize,
                mInputPaint);
    }

    /**
     * 绘制提示数字
     */
    private void drawTips(Canvas canvas, int[] tips, int column, int row) {
        for (int j = 0; j < 9; j++) {
            if (tips[j] == 0) {
                continue;
            }
            canvas.drawText(String.valueOf(j + 1),
                    mPanelMarginLeft + column * mRectSize + mRectSize / 6 * (j % 3 * 2 + 1),
                    mPanelMarginTop + mNoteTextBaseline[j / 3] + row * mRectSize,
                    mTipsPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mSudokuPresenter.updateTips();
        return super.onTouchEvent(event);
    }
}
