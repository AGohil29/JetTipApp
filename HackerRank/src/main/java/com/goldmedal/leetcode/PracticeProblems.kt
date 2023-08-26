package com.goldmedal.leetcode

import com.goldmedal.leetcode.PracticeProbs.countSubstrings

fun main(args: Array<String>) {
    /* Enter your code here. Read input from STDIN. Print output to STDOUT. */
    val palindrome = Solution().longestPalindrome("babad")
    print(palindrome)

    val s = "abc de";
    //println(countSubstrings(s));
    //println(Solution().reversePreservingSpace(s))
}

/**
 * 5. Longest Palindromic Substring
 * Given a string s, return the longest palindromic substring in s.
Example 1:

Input: s = "babad"
Output: "bab"
Explanation: "aba" is also a valid answer.
 **/
class Solution {
    fun longestPalindrome(s: String): String {
        // method 1
        /*for (length in (s.length downTo 1)) {
            for (start in (0..s.length-length)) {
                if (checkPalindrome(start, start + length, s)) {
                    return s.substring(start, start+length)
                }
            }
        }
        return ""*/

        // method 2
        val n = s.length
        val dp: Array<BooleanArray> = Array(n) { BooleanArray(n) {false} }
        val ans = intArrayOf(0, 0)

        for (i in 0 until n) {
            dp[i][i] = true
        }

        for (i in 0 until n-1) {
            if (s[i] == s[i+1]) {
                dp[i][i+1] = true
                ans[0] = i
                ans[1] = i+1
            }
        }

        for (diff in 2 until n) {
            for (i in 0 until n-diff) {
                val j = i+diff
                if (s[i] == s[j] && dp[i+1][j-1]) {
                    dp[i][j] = true
                    ans[0] = i
                    ans[1] = j
                }
            }
        }

        val i = ans[0]
        val j = ans[1]

        return s.substring(i, j+1)
    }

    private fun checkPalindrome(start: Int, end: Int, sub: String): Boolean {
        var left = start
        var right = end - 1
        while (left < right) {
            if (sub[left] != sub[right]) {
                return false
            }

            left++
            right--
        }
        return true
    }

    /**
     * Write a program to reverse the given string while preserving the position of spaces.
     * Example: Input  : "abc de"
                Output : edc ba
     * */
    fun reversePreservingSpace(string: String): String {
        val builder = CharArray(string.length)
        if (string.isNotEmpty() && string.length == 1) {
            return string
        } else {
            for (i in string.indices) {
                if (string[i].isWhitespace()) {
                    builder[i] = string[i]
                }
            }

                var j = string.length - 1
                for (i in string.indices) {
                    if (!string[i].isWhitespace()) {
                        if (builder[j].isWhitespace()) {
                            j--
                        }
                        builder[j] = string[i]
                        j--
                    }
                }
        }
        return String(builder)
    }
}