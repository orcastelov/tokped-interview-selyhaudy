package com.tokopedia.oilreservoir

/**
 * Created by fwidjaja on 2019-09-24.
 */
object Solution {
    fun collectOil(height: IntArray): Int {
        // TODO, return the amount of oil blocks that could be collected

        var totalOil = 0

        height.forEachIndexed { index, elevation -> // scan from left to right

            if(index < height.size - 1){ // excluding last block

                for (testHeight in 1 .. elevation){ // count for each height
                    var possibleOil = 0

                    for (j in index + 1 until height.size){ // run to the right until find block
                        if(height[j] < testHeight){ // if lower than test height, continue the counting
                            possibleOil ++
                        } else {
                            totalOil += possibleOil // if find a block, add all possible oil
                            break
                        }
                    }
                }
            }
        }
        println("Total OIL: " + totalOil)
        return totalOil
    }
}
