package com.ben.sudoku;

/**
 * 数独逻辑操作类
 *
 * @author chenbenbin
 * @date 2018/7/16
 */
public class SudokuPresenter {
    public static final int GRID_COUNT = 81;

    private int[] mData = new int[]{
            1, 2, 3, 9, 5, 6, 8, 7, 4,
            7, 4, 8, 2, 1, 3, 6, 5, 9,
            6, 5, 9, 8, 7, 4, 3, 1, 2,

            8, 1, 0, 4, 2, 5, 9, 3, 7,
            9, 7, 4, 3, 8, 1, 5, 2, 6,
            5, 3, 2, 6, 9, 7, 1, 4, 8,

            4, 0, 1, 7, 3, 0, 0, 6, 5,
            3, 8, 7, 5, 0, 2, 4, 9, 1,
            2, 6, 0, 1, 4, 9, 7, 8, 0
    };

    private GridBean[] mGridBeans = new GridBean[9 * 9];

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


    public SudokuPresenter() {
        for (int i = 0; i < mGridBeans.length; i++) {
            GridBean gridBean = new GridBean();
            int value = mData[i];
            gridBean.setSource(value);
            mGridBeans[i] = gridBean;
        }
        updateTips();
    }

    public GridBean getGrid(int i) {
        return mGridBeans[i];
    }

    // ***************************************** 设置提示 *******************************************//

    public void updateTips() {
        for (int i = 0; i < GRID_COUNT; i++) {
            GridBean gridBean = mGridBeans[i];
            if (gridBean.getSource() == 0) {
                int x = getXByDataIndex(i);
                int y = getYByDataIndex(i);
                updateTipsByRect(gridBean, x, y);
                updateTipsByRow(gridBean, x, y);
                updateTipsByColumn(gridBean, x, y);
            }
        }
    }

    private void updateTipsByRect(GridBean gridBean, int x, int y) {
        boolean isChecked = gridBean.getTipsCount() != 0;
        for (int i = 1; i < 10; i++) {
            int count = getNumCountInRect(x, y, i);
            boolean show = count < 1;
            if (isChecked) {
                if (gridBean.getTip(i - 1) != 0) {
                    gridBean.setTip(i, show);
                }
            } else {
                gridBean.setTip(i, show);
            }
        }
        gridBean.updateTipsCount();
    }

    private void updateTipsByColumn(GridBean gridBean, int x, int y) {
        boolean isChecked = gridBean.getTipsCount() != 0;
        for (int i = 1; i < 10; i++) {
            int count = getNumCountInColumn(x, y, i);
            boolean show = count < 1;
            if (isChecked) {
                if (gridBean.getTip(i - 1) != 0) {
                    gridBean.setTip(i, show);
                }
            } else {
                gridBean.setTip(i, show);
            }
        }
        gridBean.updateTipsCount();
    }

    private void updateTipsByRow(GridBean gridBean, int x, int y) {
        boolean isChecked = gridBean.getTipsCount() != 0;
        for (int i = 1; i < 10; i++) {
            int count = getNumCountInRow(x, y, i);
            boolean show = count < 1;
            if (isChecked) {
                if (gridBean.getTip(i - 1) != 0) {
                    gridBean.setTip(i, show);
                }
            } else {
                gridBean.setTip(i, show);
            }
        }
        gridBean.updateTipsCount();
    }

    // ***************************************** 检测提示 *******************************************//

    /**
     * 检查指定位置所在的行，某个数字的个数
     */
    public boolean checkColumn(int x, int y, int num) {
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
    public int getNumCountInColumn(int x, int y, int num) {
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
    public boolean checkRow(int x, int y, int num) {
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
    public int getNumCountInRow(int x, int y, int num) {
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
    public boolean checkRect(int x, int y, int num) {
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
    public int getNumCountInRect(int x, int y, int num) {
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
    public int[] getRect(int x, int y) {
        return getRect(getRectIndex(x, y));
    }

    /**
     * 获取指定索引的矩阵数据
     */
    public int[] getRect(int rectIndex) {
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
    public int getRectIndex(int x, int y) {
        int i = getDateIndex(x, y);
        // 在横向上的矩阵坐标
        int xRectIndex = i % 9 / 3;
        int yRectIndex = i / 9 / 3;
        return xRectIndex + yRectIndex * 3;
    }

    /**
     * 获取指定位置在数据中的索引
     */
    public int getDateIndex(int x, int y) {
        return x + y * 9;
    }


    public int getXByDataIndex(int index) {
        return index % 9;
    }

    public int getYByDataIndex(int index) {
        return index / 9;
    }
}
