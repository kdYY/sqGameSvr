package org.sq.gameDemo.lc.greed;

import java.util.Arrays;
import java.util.Comparator;

public class Solution_435 {
    /**
     * 负数没考虑！！！！！
     * 框架没写好就陷入细节！！！ 陷入细节框架容易乱！！！
     *
     * 给定一个区间的集合，找到需要移除区间的最小数量，使剩余区间互不重叠。
     *
     * 注意:
     *
     * 可以认为区间的终点总是大于它的起点。
     * 区间 [1,2] 和 [2,3] 的边界相互“接触”，但没有相互重叠。
     *
     * 链接：https://leetcode-cn.com/problems/non-overlapping-intervals
     */
    public int eraseOverlapIntervals(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(interval -> interval[0]));
        int result = 0;
        int lastIndex = 0;
        for (int i = 1; i < intervals.length; i++) {
            if(intervals[lastIndex][1] > intervals[i][0]) {
                if(intervals[lastIndex][1]  > intervals[i][1]) {
                    lastIndex = i;
                }
                result++;
            } else {
                lastIndex = i;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Solution_435 solution_435 = new Solution_435();
        int[][] ints = new int[][]{
                {0,2},{1,3},{2,4},{3,5},{4,6}
        };
        int[][] ints1 = new int[][]{
                {1,2},{2,3},{3,4},{1,3}
        };
        solution_435.eraseOverlapIntervals(ints1);
    }
}
