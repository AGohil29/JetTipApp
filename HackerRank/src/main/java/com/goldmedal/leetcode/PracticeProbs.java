package com.goldmedal.leetcode;

import java.util.Arrays;

public class PracticeProbs {
    static int[][] dp = new int[1001][1001]; // 2D matrix

    /*public static void main(String[] args) {
        String s = "abbaeae";

        System.out.println(countSubstrings(s));
    }*/

    public static int countSubstrings(String s)
    {
        for (int[] row: dp)
        {
            Arrays.fill(row, -1);
        }
        int n = s.length();
        int count = 0;

        // 2 for loops are required to check for
        // all the palindromes in the string.
        for (int i = 0; i < n; i++)
        {
            for (int j = i + 1; j < n; j++)
            {
                // Increment count for every palindrome
                if (isPal(s, i, j) != 0)
                    count++;
            }
        }

        // return total palindromic substrings
        return count;
    }

    public static int isPal(String s, int i, int j)
    {
        // Base condition
        if (i > j)
            return 1;

        // check if the recursive tree
        // for given i, j
        // has already been executed
        if (dp[i][j] != -1)
            return dp[i][j];

        // If first and last characters of
        // substring are unequal
        if (s.charAt(i) != s.charAt(j))
            return dp[i][j] = 0;

        // memoization
        return dp[i][j] = isPal(s, i + 1, j - 1);
    }
}
