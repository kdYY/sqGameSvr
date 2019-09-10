package org.sq.gameDemo.svr.game.task.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class test {



    @Data
    @AllArgsConstructor
    public static class Attack {
        private  int hurt;
        //持续时长
        private Long time;
        private Long cd;
        //增益伤害
        private int hurtAdd;
        //
        private double baifenbi;

        public Attack(int hurt, Long time, Long cd) {
            this.hurt = hurt;
            this.time = time;
            this.cd = cd;
        }

        public Attack(int hurt, Long time, Long cd, double baifenbi) {
            this.hurt = hurt;
            this.time = time;
            this.cd = cd;
            this.baifenbi = baifenbi;
        }
    }

    public static Map<Integer, Attack> skillMap = new ConcurrentHashMap<>();

    static {
        skillMap.put(1, new Attack(-100, 5000L, 5000L));
        skillMap.put(2, new Attack(-300, -1L, 3000L));
        skillMap.put(3, new Attack(-200, 3000L, 15000L));
        skillMap.put(4, new Attack(0, -1L, 30000L,0.3));
        skillMap.put(5, new Attack(-100, -1L, 1000L));
    }

    public static void main1(String[] args) {

        List<Integer> buffList = new ArrayList<>();
        long[] cdRemain = new long[5];
        // 当前总伤害
        int curTotalHurt = 0;

        // 技能使用策略
        int[][] skillUse = new int[105][5];
        int i = 0;
        if(i < 105) {
            for(int k = 1; k < 6; k++ ) {
                // 1-5分别代表五个技能
                for(int  j=k; j<6; ) {
                    // 冷却好了，使用技能
                    int skillHurt = 0;

                    //buff在同一秒生成的伤害
                    if(!buffList.isEmpty()) {
                        //遍历buffList的伤害
                    }
                    if(skillUse[i][j] > 3900) {
                        curTotalHurt -= skillHurt;
                        break;
                    }
                    //释放技能
                    if(cdRemain[j] == 0) {
                        skillHurt = skillHurtAttack(j, buffList);
                        //debuffRemain = 5;
                        curTotalHurt += skillHurt;
                        skillUse[i][j] = curTotalHurt;
                        // 开始cd
                        cdRemain[j] = skillMap.get(j).cd;
                        // 怪物死亡

                        j++;
                    } else {
                        // cd-1
                        cdRemain[j] --;
                    }
                    i++;
                }
            }

        }

        //之后的遍历...

    }

    //技能之间的影响
    private static int skillHurtAttack(int j, List<Integer> buffList) {
        //如果是buff类型技能


        //是否有增益伤害


        return skillMap.get(j).hurt;
    }


}
