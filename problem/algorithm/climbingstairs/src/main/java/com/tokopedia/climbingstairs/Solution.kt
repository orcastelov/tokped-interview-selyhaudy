package com.tokopedia.climbingstairs

object Solution {
    fun climbStairs(n: Int): Long {
        // TODO, return in how many distinct ways can you climb to the top. Each time you can either climb 1 or 2 steps.
        // 1 <= n < 90
        val possibleCases = Array(90){
            0L
        }
        possibleCases[0] = 0 // 0 steps possible for 0-height stair
        possibleCases[1] = 1 // 1 possible case for 1-height stair
        possibleCases[2] = 2 // 2 possible cases for 2-height stair

        for( i in 3 until 90){
            possibleCases[i] = possibleCases[i-1] + possibleCases[i-2]
        }

        return possibleCases[n]
    }
}
