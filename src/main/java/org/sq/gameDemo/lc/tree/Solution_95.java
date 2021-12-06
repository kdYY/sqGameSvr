package org.sq.gameDemo.lc.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 不同的二叉搜索树 II
 * 给你一个整数 n ，请你生成并返回所有由 n 个节点组成且节点值从 1 到 n 互不相同的不同 二叉搜索树 。可以按 任意顺序 返回答案。
 * 输入：n = 3
 * 输出：[[1,null,2,null,3],[1,null,3,2],[2,1,3],[3,1,null,null,2],[3,2,null,1]]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/unique-binary-search-trees-ii
 */

public class Solution_95 {
    public List<TreeNode> generateTrees(int n) {
        if(n <= 0) {
            return new ArrayList<>();
        }
        return generateTrees(1, n);
    }

    public List<TreeNode> generateTrees(int start, int end) {
        List<TreeNode> results = new ArrayList<>();
        if(start > end) {
            results.add(null);
            return results;
        }
        for(int i = start; i <= end; i++) {
            List<TreeNode> lefts = generateTrees(start, i-1);
            List<TreeNode> rights = generateTrees(i+1, end);
            for(TreeNode left: lefts) {
                for(TreeNode right: rights) {
                    TreeNode root = new TreeNode(i);
                    root.left = left;
                    root.right = right;
                    results.add(root);
                }
            }
        }
        return results;
    }

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
