package org.sq.gameDemo.lc.greed;

public class Solution_122 {
    public int maxProfit(int[] prices) {
        int n  = prices.length;
        if(n <= 1) {
            return 0;
        }
        int low = Integer.MAX_VALUE;
        int lowIn = 0;
        int high = 0;
        int highOut = 0;
        int result = 0;
        boolean sell = false;
        for(int i = 0; i < n; i++) {
            if(!sell) {
                if(low >= prices[i]) {
                    low = prices[i];
                } else {
                    //不连续了 买入
                    lowIn = low;
                    sell = true;
                }
            } else {
                if(high <= prices[i]) {
                    high = prices[i];
                } else {
                    //不连续了 卖出
                    highOut = high;
                    result += (highOut - lowIn);
                    high = 0;
                    low = Integer.MAX_VALUE;
                    sell = false;
                }
            }
        }
        return result;
    }
}
