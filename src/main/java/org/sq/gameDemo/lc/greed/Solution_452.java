package org.sq.gameDemo.lc.greed;

import java.util.Arrays;
import java.util.Comparator;

public class Solution_452 {


    /**
     * 输入：points = [[10,16],[2,8],[1,6],[7,12]]
     * [[1,6],[2,8],[7,12],[10,16]]
     * ----
     * ------
     *      ----
     *        --------
     * ------
     * ------
     *      ----
     *      ---------
     *
     *
     *
     * 输出：2
     * 解释：对于该样例，x = 6 可以射爆 [2,8],[1,6] 两个气球，以及 x = 11 射爆另外两个气球
     *
     * 链接：https://leetcode-cn.com/problems/minimum-number-of-arrows-to-burst-balloons
     */
    public int findMinArrowShots(int[][] points) {
        Arrays.sort(points, Comparator.comparingInt(a -> a[0]));
        int lastIndex = 0;
        int result = 1;
        for (int i = 1; i < points.length; i++) {
            // 前一个的右边小于当前的左边
            if(points[lastIndex][1] < points[i][0]) {
                result ++;
                lastIndex = i;
            } else {
                // 前一个的右边大于当前的左边同时找右边值小的
                if(points[lastIndex][1] > points[i][1]) {
                    lastIndex = i;
                }
            }
        }
        return result;
    }

    /**
     * [[3,9],[7,12],[3,8],[6,8],[9,10],[2,9],[0,9],[3,9],[0,6],[2,8]]
     * @param args
     */
    public static void main(String[] args) {
        Solution_452 solution_452 = new Solution_452();
        int[][] test = new int[][]{
                {3,9},{7,12},{3,8},{6,8},{9,10},{2,9},{0,9},{3,9},{0,6},{2,8}
        };
        solution_452.findMinArrowShots(test);
    }
}
