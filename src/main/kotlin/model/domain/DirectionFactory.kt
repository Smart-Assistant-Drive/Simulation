package model.domain

import model.math.Math2D
import model.math.Math2D.calculateIntersection
import model.math.Point

typealias Lines = List<Point>

object DirectionFactory {
    /**
     * Creates a [Direction] object from a list of points and the number of lines.
     *
     * @param points A list of [Point] objects representing the points to create the direction.
     * @param lines The number of lines to create in the direction.
     *
     * @return A [Direction] object containing the created roads.
     *
     * @throws IllegalArgumentException if the number of points is less than 3.
     *
     * The given list of points is the first lines
     * example:
     * 1a | 2a | 3a || 3b | 2b | 1b
     *
     * 1a and 1b are lines given by the user
     *
     *
     */
    fun createDirection(
        points: List<Point>,
        lines: Int,
    ): Direction {
        if (points.size < 3) {
            throw IllegalArgumentException("At least three points are required to create a direction.")
        }
        val linesSize = 20.0
        val linesPoint = (0 until lines).map { mutableListOf<Point>() }
        for (i in 0 until points.size - 2) {
            for (lineIndex in 0 until lines) {
                val angleFirst = Math2D.calculateAngle(points[i], points[i + 1])
                val perpAngleFirst = Math2D.calculatePerpendicularAngle(angleFirst)
                val angleSecond = Math2D.calculateAngle(points[i + 1], points[i + 2])
                val perpAngleSecond = Math2D.calculatePerpendicularAngle(angleSecond)
                val pointFirst = Math2D.calculateTranslation(points[i], perpAngleFirst, linesSize * lineIndex)
                val pointSecond = Math2D.calculateTranslation(points[i + 1], perpAngleSecond, linesSize * lineIndex)
                if (i == 0) {
                    linesPoint[lineIndex].add(pointFirst)
                }
                val intersection =
                    calculateIntersection(
                        pointFirst,
                        angleFirst,
                        pointSecond,
                        angleSecond,
                    )
                if (intersection != null) {
                    linesPoint[lineIndex].add(intersection)
                } else {
                    linesPoint[lineIndex].add(pointSecond)
                }
                if (i == points.size - 3) {
                    val pointThird = Math2D.calculateTranslation(points[i + 2], perpAngleSecond, linesSize * lineIndex)
                    linesPoint[lineIndex].add(pointThird)
                }
            }
        }
        return Direction(
            roads = linesPoint.map { it },
        )
    }
}
