package com.tokopedia.minimumpathsum

import java.util.*

object Solution {

    fun minimumPathSum(matrix: Array<IntArray>): Int {
        // TODO, find a path from top left to bottom right which minimizes the sum of all numbers along its path, and return the sum

        /* Here we use Djikstra's shortest path without priority queue */
        val width = matrix.first().size
        val height = matrix.size
        val lastRow = height - 1
        val lastColumn = width - 1

        if(width == 0 || height == 0){ // if no data return zero
            return 0
        }

        val distance: Array<IntArray> = Array(height) { IntArray(width){ _ -> Int.MAX_VALUE} }
        val visited: Array<Array<Boolean>> = Array(height) { Array(width) { _ -> false } }
        val vertexList = LinkedList<Vertex>()

        // Put all vertex to list
        for (row in 0 until height){
            for (column in 0 until width){
                vertexList.addLast(Vertex(row, column))
            }
        }

        // assign source's distance directly
        distance[0][0] = matrix[0][0]
        visited[0][0] = true

        while(vertexList.isNotEmpty()){
            // pick nearest vertex
            val index = findMinimumDistanceVertex(vertexList, distance)
            val vertex = vertexList.removeAt(index)
            val vertexDistance = distance[vertex.row][vertex.column]

            visited[vertex.row][vertex.column] = true
            if(vertex.row == lastRow && vertex.column == lastColumn){
                break // already arrive target destination, stop algorithm
            }

            val neighbors = getUnvisitedNeighbors(vertex, visited, width, height)
            neighbors.forEach { neighbor ->
                val newDistance = vertexDistance + matrix[neighbor.row][neighbor.column]

                // update distance if we have the shorter one
                if(newDistance < distance[neighbor.row][neighbor.column]){
                    distance[neighbor.row][neighbor.column] = newDistance
                }
            }
        }

        return distance[lastRow][lastColumn] // target destination distance
    }

    private fun findMinimumDistanceVertex(vertexList: LinkedList<Vertex>, distance: Array<IntArray>): Int {
        var minimumDistance = Int.MAX_VALUE
        var vertexIndex = 0
        vertexList.forEachIndexed { index, vertex ->
            if(distance[vertex.row][vertex.column] <= minimumDistance){
                minimumDistance = distance[vertex.row][vertex.column]
                vertexIndex = index
            }
        }
        return vertexIndex
    }

    private fun getUnvisitedNeighbors(vertex: Vertex, visited: Array<Array<Boolean>>, width: Int, height: Int): List<Vertex> {
        val row = vertex.row
        val column = vertex.column
        val result = mutableListOf<Vertex>()

        if(row - 1 >= 0 && !visited[row - 1][column]){ // UP
            result.add(Vertex(row - 1, column))
        }
        if(row + 1 < height && !visited[row + 1][column]){ // DOWN
            result.add(Vertex(row + 1, column))
        }
        if(column - 1 >= 0 && !visited[row][column - 1]){ // LEFT
            result.add(Vertex(row, column - 1))
        }
        if(column + 1 < width && !visited[row][column + 1]){ // RIGHT
            result.add(Vertex(row, column + 1))
        }
        return result
    }

    data class Vertex(
            val row: Int, // start from 0
            val column: Int  // start from 0
    )
}
