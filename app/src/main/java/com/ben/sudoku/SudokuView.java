package com.ben.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 数独视图
 *
 * @author chenbenbin
 * @date 2018/7/11
 */
public class SudokuView extends View {
    private int[] mData = new int[]{
            1, 2, 3, 9, 5, 6, 8, 7, 4,
            7, 4, 8, 2, 1, 3, 6, 5, 9,
            6, 5, 9, 8, 7, 4, 3, 1, 2,

            8, 1, 6, 4, 2, 5, 9, 3, 7,
            9, 7, 4, 3, 8, 1, 5, 2, 6,
            5, 3, 2, 6, 9, 7, 1, 4, 8,

            4, 9, 1, 7, 3, 8, 2, 6, 5,
            3, 8, 7, 5, 6, 2, 4, 9, 1,
            2, 6, 5, 1, 4, 9, 7, 8, 3
    };
    //    private int[] mData = new int[]{
//            1, 2, 3, 4, 5, 6, 7, 8, 9,
//            1, 4, 3, 4, 5, 6, 7, 8, 9,
//            1, 2, 3, 4, 5, 6, 7, 8, 9,
//
//            2, 1, 3, 5, 7, 5, 9, 6, 4,
//            2, 1, 3, 6, 7, 8, 9, 6, 4,
//            2, 1, 3, 5, 7, 8, 9, 6, 4,
//
//            1, 1, 1, 4, 5, 1, 8, 9, 3,
//            7, 1, 2, 4, 5, 6, 8, 9, 3,
//            7, 1, 2, 4, 5, 6, 8, 9, 3,
//    };
    private Paint mTextPaint = new Paint();
    private Paint mNotePaint = new Paint();
    private Paint mBoldLinePaint = new Paint();
    private Paint mThinLinePaint = new Paint();
    private int mRectSize;
    private int mPanelMarginLeft;
    private int mPanelMarginTop;
    private int mTextSize;
    private int mTextBaseline;
    private int[] mNoteTextBaseline = new int[3];

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
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mNotePaint.setAntiAlias(true);
        mNotePaint.setStrokeWidth(5);
        mNotePaint.setTextAlign(Paint.Align.CENTER);

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
        mTextPaint.setTextSize(mTextSize);

        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        mTextBaseline = (mRectSize - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;

        Paint.FontMetricsInt noteFontMetrics = mNotePaint.getFontMetricsInt();
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

        for (int i = 0; i < mData.length; i++) {
            int column = i % 9;
            int row = i / 9;

//            drawText(canvas, mData[i], column, row);
            for (int j = 0; j < 9; j++) {
                canvas.drawText(String.valueOf(j + 1),
                        mPanelMarginLeft + column * mRectSize + mRectSize / 6 * (j % 3 * 2 + 1),
                        mPanelMarginTop + mNoteTextBaseline[j / 3] + row * mRectSize,
                        mNotePaint);
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
     * 绘制数字
     */
    private void drawText(Canvas canvas, int num, int column, int row) {
        canvas.drawText(String.valueOf(num),
                mPanelMarginLeft + column * mRectSize + mRectSize / 2,
                mPanelMarginTop + mTextBaseline + row * mRectSize,
                mTextPaint);
    }

    /**
     * 检查指定位置所在的行，某个数字的个数
     */
    private boolean checkColumn(int x, int y, int num) {
        int first = getDateIndex(x, 0);
        int count = 0;
        for (int i = 0; i < 9; i++) {
            count += mData[first + i * 9] == num ? 1 : 0;
            if (count > 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查指定位置所在的行，某个数字的个数
     */
    private int getNumCountColumn(int x, int y, int num) {
        int first = getDateIndex(x, 0);
        int count = 0;
        for (int i = 0; i < 9; i++) {
            count += mData[first + i * 9] == num ? 1 : 0;
        }
        return count;
    }

    /**
     * 检查指定位置所在的行，某个数字的个数
     */
    private boolean checkRow(int x, int y, int num) {
        int first = getDateIndex(0, y);
        int count = 0;
        for (int i = 0; i < 9; i++) {
            count += mData[first * 9 + i] == num ? 1 : 0;
            if (count > 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查指定位置所在的行，某个数字的个数
     */
    private int getNumCountInRow(int x, int y, int num) {
        int first = getDateIndex(0, y);
        int count = 0;
        for (int i = 0; i < 9; i++) {
            count += mData[first + i] == num ? 1 : 0;
        }
        return count;
    }

    /**
     * 检查指定位置所在的方块，某个数字是否不重复
     */
    private boolean checkRect(int x, int y, int num) {
        int[] rect = getRect(x, y);
        int count = 0;
        for (int i : rect) {
            if (i == num) {
                count++;
                if (count > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 检查指定位置所在的方块，某个数字是否不重复
     */
    private int getNumCountInRect(int x, int y, int num) {
        int[] rect = getRect(x, y);
        int count = 0;
        for (int i : rect) {
            if (i == num) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取指定位置所在的矩阵数据
     */
    private int[] getRect(int x, int y) {
        return getRect(getRectIndex(x, y));
    }

    /**
     * 获取指定索引的矩阵数据
     */
    private int[] getRect(int rectIndex) {
        int startItemX = rectIndex % 3 * 3;
        int startItemY = rectIndex / 3 * 3;
        int i = startItemX + startItemY * 9;
        return new int[]{
                mData[i], mData[i + 1], mData[i + 2],
                mData[i + 9], mData[i + 10], mData[i + 11],
                mData[i + 18], mData[i + 19], mData[i + 20]
        };
    }

    /**
     * 获取指定位置所在的矩阵索引
     */
    private int getRectIndex(int x, int y) {
        int i = getDateIndex(x, y);
        // 在横向上的矩阵坐标
        int xRectIndex = i % 9 / 3;
        int yRectIndex = i / 9 / 3;
        return xRectIndex + yRectIndex * 3;
    }

    /**
     * 获取指定位置在数据中的索引
     */
    private int getDateIndex(int x, int y) {
        return x + y * 9;
    }
}
