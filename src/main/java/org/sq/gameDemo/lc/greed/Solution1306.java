package org.sq.gameDemo.lc.greed;

public class Solution1306 {
    public int jump(int[] nums) {
        int n = nums.length;
        if(n <= 1) {
            return 0;
        }
        int times = 0;
        int endI = 0;
        int now = 0;
        int next = 0;
        int reach = nums[0];
        for(int i = 0; i < n; i++) {
            if(reach < i + nums[i]) {
                reach = i + nums[i];
                next = i;
            }
            if(endI == 0) {
                endI = reach;
            }
            if(endI <= i) {
                times ++;
                endI = reach;
                now = next;
            }
            if(reach >= n -1 && endI >= n - 1) {
                if(now < n - 1) {
                    times++;
                }
                break;
            }
        }
        return times;
    }

    public static void main(String[] args) {
        int[] a = new int[]{0};
        Solution1306 solution1306 = new Solution1306();
        System.out.println(solution1306.jump(a));
    }

}
