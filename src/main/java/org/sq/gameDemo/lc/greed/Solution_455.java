package org.sq.gameDemo.lc.greed;

import java.util.Arrays;

/**
 * 假设你是一位很棒的家长，想要给你的孩子们一些小饼干。但是，每个孩子最多只能给一块饼干。
 * 对每个孩子 i，都有一个胃口值 g[i]，这是能让孩子们满足胃口的饼干的最小尺寸；并且每块饼干 j，都有一个尺寸 s[j] 。如果 s[j] >= g[i]，
 * 我们可以将这个饼干 j 分配给孩子 i ，这个孩子会得到满足。你的目标是尽可能满足越多数量的孩子，并输出这个最大数值。
 *
 * 链接：https://leetcode-cn.com/problems/assign-cookies
 */
public class Solution_455 {

    public int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);
        int result = 0;
        int sI = 0;
        for (int i = 0; i < g.length; i++) {
            if(sI >= s.length) {
                return result;
            }
            for(int j= sI; j < s.length; j++) {
                if(s[j] >= g[i]) {
                    result++;
                    sI = j + 1;
                    break;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Solution_455 solution_455 = new Solution_455();
        int contentChildren = solution_455.findContentChildren(new int[]{1, 2, 3}, new int[]{3});
        System.out.println(contentChildren);
    }
}
