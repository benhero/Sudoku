package com.ben.sudoku

/**
 * 数独逻辑操作类
 *
 * @author Benhero
 * @date 2018/7/16
 */
class SudokuPresenter {

    private val mData = intArrayOf(
            1, 0, 3, 0, 5, 0, 8, 0, 4,
            0, 0, 0, 2, 0, 0, 6, 0, 0,
            0, 5, 0, 8, 0, 4, 0, 0, 2,

            0, 1, 0, 4, 0, 0, 9, 3, 0,
            0, 0, 4, 0, 0, 1, 5, 0, 0,
            0, 0, 2, 6, 0, 0, 1, 0, 8,

            4, 0, 1, 7, 0, 0, 0, 6, 5,
            0, 8, 0, 0, 0, 2, 0, 0, 1,
            2, 6, 0, 1, 0, 0, 0, 8, 0)

    private val mData2 = intArrayOf(
            0, 0, 1, 5, 0, 0, 0, 0, 3,
            0, 0, 5, 0, 0, 0, 6, 0, 0,
            0, 0, 0, 0, 9, 6, 7, 0, 0,

            6, 0, 0, 4, 0, 0, 1, 9, 0,
            1, 2, 0, 0, 3, 0, 0, 6, 7,
            0, 0, 0, 0, 0, 0, 0, 0, 0,

            0, 9, 0, 0, 0, 0, 0, 0, 0,
            4, 0, 6, 9, 0, 2, 0, 0, 0,
            7, 0, 0, 0, 0, 0, 0, 3, 0)

    private val mData3 = intArrayOf(
            5, 3, 0, 0, 7, 0, 0, 0, 0,
            6, 0, 0, 1, 9, 5, 0, 0, 0,
            0, 9, 8, 0, 0, 0, 0, 6, 0,

            8, 0, 0, 0, 6, 0, 0, 0, 3,
            4, 0, 0, 8, 0, 3, 0, 0, 1,
            7, 0, 0, 0, 2, 0, 0, 0, 6,

            0, 6, 0, 0, 0, 0, 2, 8, 0,
            0, 0, 0, 4, 1, 9, 0, 0, 5,
            0, 0, 0, 0, 8, 0, 0, 7, 9)

    private val mGridBeans = mutableListOf<GridBean>()

    init {
        for (i in mData3) {
            val gridBean = GridBean()
            gridBean.source = i
            mGridBeans.add(gridBean)
        }
    }

    fun getGrid(i: Int): GridBean {
        return mGridBeans[i]
    }

    // ***************************************** 设置提示 *******************************************//

    /**
     * 检测输入的值
     */
    fun checkInput(): Boolean {
        for (i in 0 until GRID_COUNT) {
            val x = getXByDataIndex(i)
            val y = getYByDataIndex(i)
            val gridValue = getGridValue(i)

            if (!checkColumn(x, gridValue) || !checkRow(y, gridValue) || !checkRect(x, y, gridValue)) {
                return false
            }
        }
        return true
    }

    fun updateTips() {
        for (i in 0 until GRID_COUNT) {
            val gridBean = mGridBeans[i]
            if (getGridValue(i) == 0) {
                val x = getXByDataIndex(i)
                val y = getYByDataIndex(i)
                updateTipsByRect(gridBean, x, y)
                updateTipsByRow(gridBean, y)
                updateTipsByColumn(gridBean, x)
                updateInput(gridBean)
            }
        }

        for (i in 0 until GRID_COUNT) {
            val gridBean = mGridBeans[i]
            if (getGridValue(i) == 0) {
                updateTipsByRectTips(gridBean, i)
                updateInput(gridBean)
            }
        }
    }

    /**
     * 更新提示：格子
     */
    private fun updateTipsByRect(gridBean: GridBean, x: Int, y: Int) {
        val isChecked = gridBean.tipsCount != 0
        for (i in 1..9) {
            val count = getNumCountInRect(x, y, i)
            val show = count < 1
            if (isChecked) {
                if (gridBean.getTip(i - 1) != 0) {
                    gridBean.setTip(i, show)
                }
            } else {
                gridBean.setTip(i, show)
            }
        }
        gridBean.updateTipsCount()
    }

    /**
     * 更新提示：列
     */
    private fun updateTipsByColumn(gridBean: GridBean, x: Int) {
        val isChecked = gridBean.tipsCount != 0
        for (i in 1..9) {
            val count = getNumCountInColumn(x, i)
            val show = count < 1
            if (isChecked) {
                if (gridBean.getTip(i - 1) != 0) {
                    gridBean.setTip(i, show)
                }
            } else {
                gridBean.setTip(i, show)
            }
        }
        gridBean.updateTipsCount()
    }

    /**
     * 更新提示：行
     */
    private fun updateTipsByRow(gridBean: GridBean, y: Int) {
        val isChecked = gridBean.tipsCount != 0
        for (i in 1..9) {
            val count = getNumCountInRow(y, i)
            val show = count < 1
            if (isChecked) {
                if (gridBean.getTip(i - 1) != 0) {
                    gridBean.setTip(i, show)
                }
            } else {
                gridBean.setTip(i, show)
            }
        }
        gridBean.updateTipsCount()
    }

    /**
     * 根据提示数字在格子中剩下的数字来更新提示数
     */
    private fun updateTipsByRectTips(gridBean: GridBean, i: Int) {
        if (gridBean.tipsCount == 0) {
            // 没有提示数字，直接退出
            return
        }
        val rectGrid = getRectGrid(getRectIndex(i))
        val tips = gridBean.tips
        for (tip in tips) {
            if (tip == 0) {
                continue
            }
            var isLastOne = true
            for (j in 0..8) {
                val bean = rectGrid[j]
                if (bean == gridBean) {
                    continue
                }
                if (bean.tips[tip - 1] != 0) {
                    isLastOne = false
                }
            }
            if (isLastOne) {
                gridBean.input = tip
                gridBean.clearTips()
            }
        }
        gridBean.updateTipsCount()
    }

    private fun updateInput(gridBean: GridBean) {
        if (gridBean.tipsCount == 1) {
            var input = 0
            for (i in gridBean.tips) {
                input = if (i != 0) i else input
            }
            gridBean.input = input
        }
    }

    // ***************************************** 检测提示 *******************************************//

    /**
     * 检查指定位置所在的列，某个数字的个数
     */
    private fun checkColumn(x: Int, num: Int): Boolean {
        val first = getDateIndex(x, 0)
        var count = 0
        for (i in 0..8) {
            count += if (getGridValue(first + i * 9) == num) 1 else 0
            if (count > 1) {
                return false
            }
        }
        return true
    }

    /**
     * 检查指定位置所在的列，某个数字的个数
     */
    private fun getNumCountInColumn(x: Int, num: Int): Int {
        val first = getDateIndex(x, 0)
        var count = 0
        for (i in 0..8) {
            count += if (getGridValue(first + i * 9) == num) 1 else 0
        }
        return count
    }

    /**
     * 检查指定位置所在的行，某个数字的个数
     */
    private fun checkRow(y: Int, num: Int): Boolean {
        val first = getDateIndex(0, y)
        var count = 0
        for (i in 0..8) {
            count += if (getGridValue(first + i) == num) 1 else 0
            if (count > 1) {
                return false
            }
        }
        return true
    }

    /**
     * 检查指定位置所在的行，某个数字的个数
     */
    private fun getNumCountInRow(y: Int, num: Int): Int {
        val first = getDateIndex(0, y)
        var count = 0
        for (i in 0..8) {
            count += if (getGridValue(first + i) == num) 1 else 0
        }
        return count
    }

    /**
     * 检查指定位置所在的方块，某个数字是否不重复
     */
    private fun checkRect(x: Int, y: Int, num: Int): Boolean {
        val rect = getRect(x, y)
        var count = 0
        for (i in rect) {
            if (i == num) {
                count++
                if (count > 1) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * 检查指定位置所在的方块，某个数字是否不重复
     */
    private fun getNumCountInRect(x: Int, y: Int, num: Int): Int {
        val rect = getRect(x, y)
        var count = 0
        for (i in rect) {
            if (i == num) {
                count++
            }
        }
        return count
    }

    /**
     * 获取指定位置所在的矩阵数据
     */
    private fun getRect(x: Int, y: Int): IntArray {
        return getRect(getRectIndex(x, y))
    }

    /**
     * 获取指定索引的矩阵数据
     */
    private fun getRect(rectIndex: Int): IntArray {
        val startItemX = rectIndex % 3 * 3
        val startItemY = rectIndex / 3 * 3
        val i = startItemX + startItemY * 9
        return intArrayOf(getGridValue(i), getGridValue(i + 1), getGridValue(i + 2),
                getGridValue(i + 9), getGridValue(i + 10), getGridValue(i + 11),
                getGridValue(i + 18), getGridValue(i + 19), getGridValue(i + 20))
    }

    /**
     * 获取指定索引的矩阵数据
     */
    private fun getRectGrid(rectIndex: Int): Array<GridBean> {
        val startItemX = rectIndex % 3 * 3
        val startItemY = rectIndex / 3 * 3
        val i = startItemX + startItemY * 9
        return arrayOf(mGridBeans[i], mGridBeans[i + 1], mGridBeans[i + 2],
                mGridBeans[i + 9], mGridBeans[i + 10], mGridBeans[i + 11],
                mGridBeans[i + 18], mGridBeans[i + 19], mGridBeans[i + 20])
    }

    /**
     * 获取指定位置所在的矩阵索引
     */
    private fun getRectIndex(x: Int, y: Int): Int {
        val i = getDateIndex(x, y)
        // 在横向上的矩阵坐标
        val xRectIndex = i % 9 / 3
        val yRectIndex = i / 9 / 3
        return xRectIndex + yRectIndex * 3
    }

    /**
     * 获取指定位置所在的矩阵索引
     */
    private fun getRectIndex(i: Int): Int {
        // 在横向上的矩阵坐标
        val xRectIndex = i % 9 / 3
        val yRectIndex = i / 9 / 3
        return xRectIndex + yRectIndex * 3
    }

    /**
     * 获取指定位置在数据中的索引
     */
    private fun getDateIndex(x: Int, y: Int): Int {
        return x + y * 9
    }

    /**
     * 获取指定数据的X坐标
     * @param index 数据的索引
     */
    private fun getXByDataIndex(index: Int): Int {
        return index % 9
    }

    /**
     * 获取指定数据的Y坐标
     * @param index 数据的索引
     */
    private fun getYByDataIndex(index: Int): Int {
        return index / 9
    }

    private fun getGridValue(index: Int): Int {
        val bean = mGridBeans[index]
        return if (bean.source != 0) bean.source else bean.input
    }

    fun reset() {
        for (i in 0 until GRID_COUNT) {
            val gridBean = mGridBeans[i]
            gridBean.input = 0
            gridBean.clearTips()
        }
    }

    companion object {
        const val GRID_COUNT = 81
    }
}
