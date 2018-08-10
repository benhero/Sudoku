package com.ben.sudoku;

/**
 * 数独逻辑操作类
 *
 * @author Benhero
 * @date 2018/7/16
 */
public class SudokuPresenter {
    public static final int GRID_COUNT = 81;

    private int[] mData = new int[]{
            1, 0, 3, 0, 5, 0, 8, 0, 4,
            0, 0, 0, 2, 0, 0, 6, 0, 0,
            0, 5, 0, 8, 0, 4, 0, 0, 2,

            0, 1, 0, 4, 0, 0, 9, 3, 0,
            0, 0, 4, 0, 0, 1, 5, 0, 0,
            0, 0, 2, 6, 0, 0, 1, 0, 8,

            4, 0, 1, 7, 0, 0, 0, 6, 5,
            0, 8, 0, 0, 0, 2, 0, 0, 1,
            2, 6, 0, 1, 0, 0, 0, 8, 0
    };

    private GridBean[] mGridBeans = new GridBean[9 * 9];

    private int[] mData2 = new int[]{
            0, 0, 1, 5, 0, 0, 0, 0, 3,
            0, 0, 5, 0, 0, 0, 6, 0, 0,
            0, 0, 0, 0, 9, 6, 7, 0, 0,

            6, 0, 0, 4, 0, 0, 1, 9, 0,
            1, 2, 0, 0, 3, 0, 0, 6, 7,
            0, 0, 0, 0, 0, 0, 0, 0, 0,

            0, 9, 0, 0, 0, 0, 0, 0, 0,
            4, 0, 6, 9, 0, 2, 0, 0, 0,
            7, 0, 0, 0, 0, 0, 0, 3, 0
    };


    public SudokuPresenter() {
        for (int i = 0; i < mGridBeans.length; i++) {
            GridBean gridBean = new GridBean();
            int value = mData[i];
            gridBean.setSource(value);
            mGridBeans[i] = gridBean;
        }
    }

    public GridBean getGrid(int i) {
        return mGridBeans[i];
    }

    // ***************************************** 设置提示 *******************************************//

    /**
     * 检测输入的值
     */
    public boolean checkInput() {
        for (int i = 0; i < GRID_COUNT; i++) {
            int x = getXByDataIndex(i);
            int y = getYByDataIndex(i);
            int gridValue = getGridValue(i);

            if (!checkColumn(x, y, gridValue) || !checkRow(x, y, gridValue) || !checkRect(x, y, gridValue)) {
                return false;
            }
        }
        return true;
    }

    public void updateTips() {
        for (int i = 0; i < GRID_COUNT; i++) {
            GridBean gridBean = mGridBeans[i];
            if (getGridValue(i) == 0) {
                int x = getXByDataIndex(i);
                int y = getYByDataIndex(i);
                updateTipsByRect(gridBean, x, y);
                updateTipsByRow(gridBean, x, y);
                updateTipsByColumn(gridBean, x, y);
                updateInput(gridBean);
            }
        }

        for (int i = 0; i < GRID_COUNT; i++) {
            GridBean gridBean = mGridBeans[i];
            if (getGridValue(i) == 0) {
                updateTipsByRectTips(gridBean, i);
                updateInput(gridBean);
            }
        }
    }

    /**
     * 更新提示：格子
     */
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

    /**
     * 更新提示：列
     */
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

    /**
     * 更新提示：行
     */
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

    /**
     * 根据提示数字在格子中剩下的数字来更新提示数
     */
    private void updateTipsByRectTips(GridBean gridBean, int i) {
        if (gridBean.getTipsCount() == 0) {
            // 没有提示数字，直接退出
            return;
        }
        GridBean[] rectGrid = getRectGrid(getRectIndex(i));
        int[] tips = gridBean.getTips();
        for (int tip : tips) {
            if (tip == 0) {
                continue;
            }
            boolean isLastOne = true;
            for (int j = 0; j < 9; j++) {
                GridBean bean = rectGrid[j];
                if (bean == gridBean) {
                    continue;
                }
                if (bean.getTips()[tip - 1] != 0) {
                    isLastOne = false;
                }
            }
            if (isLastOne) {
                gridBean.setInput(tip);
                gridBean.clearTips();
            }
        }
        gridBean.updateTipsCount();
    }

    private void updateInput(GridBean gridBean) {
        if (gridBean.getTipsCount() == 1) {
            int input = 0;
            for (int i : gridBean.getTips()) {
                input = i != 0 ? i : input;
            }
            gridBean.setInput(input);
        }
    }

    // ***************************************** 检测提示 *******************************************//

    /**
     * 检查指定位置所在的列，某个数字的个数
     */
    public boolean checkColumn(int x, int y, int num) {
        int first = getDateIndex(x, 0);
        int count = 0;
        for (int i = 0; i < 9; i++) {
            count += getGridValue(first + i * 9) == num ? 1 : 0;
            if (count > 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查指定位置所在的列，某个数字的个数
     */
    public int getNumCountInColumn(int x, int y, int num) {
        int first = getDateIndex(x, 0);
        int count = 0;
        for (int i = 0; i < 9; i++) {
            count += getGridValue(first + i * 9) == num ? 1 : 0;
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
            count += getGridValue(first + i) == num ? 1 : 0;
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
            count += getGridValue(first + i) == num ? 1 : 0;
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
                getGridValue(i), getGridValue(i + 1), getGridValue(i + 2),
                getGridValue(i + 9), getGridValue(i + 10), getGridValue(i + 11),
                getGridValue(i + 18), getGridValue(i + 19), getGridValue(i + 20)
        };
    }

    /**
     * 获取指定索引的矩阵数据
     */
    public GridBean[] getRectGrid(int rectIndex) {
        int startItemX = rectIndex % 3 * 3;
        int startItemY = rectIndex / 3 * 3;
        int i = startItemX + startItemY * 9;
        return new GridBean[]{
                mGridBeans[i], mGridBeans[i + 1], mGridBeans[i + 2],
                mGridBeans[i + 9], mGridBeans[i + 10], mGridBeans[i + 11],
                mGridBeans[i + 18], mGridBeans[i + 19], mGridBeans[i + 20]
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
     * 获取指定位置所在的矩阵索引
     */
    public int getRectIndex(int i) {
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

    private int getGridValue(int index) {
        GridBean bean = mGridBeans[index];
        return bean.getSource() != 0 ? bean.getSource() : bean.getInput();
    }

    public void reset() {
        for (int i = 0; i < GRID_COUNT; i++) {
            GridBean gridBean = mGridBeans[i];
            gridBean.setInput(0);
            gridBean.clearTips();
        }
    }
}
