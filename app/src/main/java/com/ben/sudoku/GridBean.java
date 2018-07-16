package com.ben.sudoku;

import java.util.Arrays;

/**
 * 单个格子的数据Bean
 *
 * @author chenbenbin
 * @date 2018/7/16
 */
public class GridBean {
    /**
     * 原始值
     */
    private int mSource;
    /**
     * 填入的值
     */
    private int mInput;
    /**
     * 提示数字
     */
    private int[] mTips = new int[9];
    /**
     * 提示数字个数
     */
    private int mTipsCount = 0;

    public GridBean() {
        Arrays.fill(mTips, 0);
    }

    public int getSource() {
        return mSource;
    }

    public void setSource(int source) {
        mSource = source;
    }

    public int getInput() {
        return mInput;
    }

    public void setInput(int input) {
        mInput = input;
    }

    public int[] getTips() {
        return mTips;
    }

    public void setTips(int[] tips) {
        mTips = tips;
    }

    public void setTip(int num, boolean show) {
        mTips[num - 1] = show ? num : 0;
    }

    public int getTip(int index) {
        return mTips[index];
    }

    public int getTipsCount() {
        return mTipsCount;
    }

    public void updateTipsCount() {
        mTipsCount = 0;
        for (int tip : mTips) {
            mTipsCount += tip == 0 ? 0 : 1;
        }
    }

    public void clearTips() {
        Arrays.fill(mTips, 0);
        mTipsCount = 0;
    }
}
