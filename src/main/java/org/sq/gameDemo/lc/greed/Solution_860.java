package org.sq.gameDemo.lc.greed;

import java.util.*;

/**
 * 柠檬水找零
 * 在柠檬水摊上，每一杯柠檬水的售价为 5 美元。顾客排队购买你的产品，（按账单 bills 支付的顺序）一次购买一杯。
 * 每位顾客只买一杯柠檬水，然后向你付 5 美元、10 美元或 20 美元。你必须给每个顾客正确找零，也就是说净交易是每位顾客向你支付 5 美元。
 * 注意，一开始你手头没有任何零钱。
 * 给你一个整数数组 bills ，其中 bills[i] 是第 i 位顾客付的账。如果你能给每位顾客正确找零，返回 true ，否则返回 false 。
 * 链接：https://leetcode-cn.com/leetbook/read/greedy/rvmuse/
 */
public class Solution_860 {
    public boolean lemonadeChange1(int[] bills) {
        if(bills == null) {
            return false;
        }
        if(bills[0] != 5) {
            return false;
        }
        List<Integer> purse = new ArrayList<>();
        for (int bill : bills) {
            if(lemonChange(bill - 5, purse)) {
                purse.add(bill);
            } else {
                return false;
            }
        }
        return true;
    }


    /**
     * 在bills中寻找balance的钱数
     * @param balance
     * @return
     */
    public boolean lemonChange(int balance, List<Integer> purse) {
        if(balance == 0) {
            return true;
        }
        Collections.sort(purse);
        //从最大的数字开始减去
        for (int i = purse.size() - 1; i >= 0; i--) {
            if(purse.get(i) <= balance) {
                balance -= purse.get(i);
                purse.remove(i);
                if(balance == 0) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 2
     */
    public boolean lemonadeChange(int[] bills) {
        int five = 0, ten = 0;
        for (int bill : bills) {
            if (bill == 5) {
                five++;
            } else if (bill == 10) {
                if (five == 0) {
                    return false;
                }
                five--;
                ten++;
            } else {
                if (five > 0 && ten > 0) {
                    five--;
                    ten--;
                } else if (five >= 3) {
                    five -= 3;
                } else {
                    return false;
                }
            }
        }
        return true;
    }
}
