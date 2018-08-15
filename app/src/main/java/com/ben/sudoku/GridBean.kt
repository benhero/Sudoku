package com.ben.sudoku

import java.util.*

/**
 * 单个格子的数据Bean
 *
 * @author Benhero
 * @date 2018/7/16
 */
class GridBean {
    /**
     * 原始值
     */
    var source: Int = 0
    /**
     * 填入的值
     */
    var input: Int = 0
    /**
     * 提示数字
     */
    var tips = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    /**
     * 提示数字个数
     */
    var tipsCount = 0
        private set

    fun setTip(num: Int, show: Boolean) {
        tips[num - 1] = if (show) num else 0
    }

    fun getTip(index: Int): Int {
        return tips[index]
    }

    fun updateTipsCount() {
        tipsCount = 0
        for (tip in tips) {
            tipsCount += if (tip == 0) 0 else 1
        }
    }

    fun clearTips() {
        Arrays.fill(tips, 0)
        tipsCount = 0
    }
}
