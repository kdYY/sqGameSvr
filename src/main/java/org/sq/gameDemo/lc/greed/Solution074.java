package org.sq.gameDemo.lc.greed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * https://leetcode-cn.com/problems/SsGoHC/submissions/
 */
public class Solution074 {
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        int left = intervals[0][0];
        int right = intervals[0][1];
        List<int[]> result = new ArrayList<>();
        for (int i = 1; i < intervals.length; i++) {
            if(right < intervals[i][0]) {
                result.add(new int[]{left, right});
                left = intervals[i][0];
                right = intervals[i][1];
            } else {
                right = Math.max(right, intervals[i][1]);
                left = Math.min(left, intervals[i][0]);
            }
        }
        result.add(new int[]{left, right});
        return result.toArray(new int[result.size()][]);
    }
}
