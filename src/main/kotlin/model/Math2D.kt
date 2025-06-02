package model

object Math2D {
    fun calculateIntersection(
        pointFirst: Point,
        angleFirst: Double,
        pointSecond: Point,
        angleSecond: Double,
    ): Point? {
        // Check for vertical lines (angle = π/2 or 3π/2)
        val isFirstVertical = kotlin.math.abs(kotlin.math.cos(angleFirst)) < 1e-10
        val isSecondVertical = kotlin.math.abs(kotlin.math.cos(angleSecond)) < 1e-10

        if (isFirstVertical && isSecondVertical) {
            return null // Both lines are vertical
        }

        // Line 1: y = m1 * x + b1
        val m1 = kotlin.math.tan(angleFirst)
        val b1 = pointFirst.y - m1 * pointFirst.x

        // Line 2: y = m2 * x + b2
        val m2 = kotlin.math.tan(angleSecond)
        val b2 = pointSecond.y - m2 * pointSecond.x

        // Check if lines are parallel (same slope)
        if (m1 == m2) return null // No intersection, lines are parallel

        if (isFirstVertical) {
            // Line 1 is vertical, use x from pointFirst
            val x = pointFirst.x
            val y = m2 * x + b2
            return Point(x, y)
        }

        if (isSecondVertical) {
            // Line 2 is vertical, use x from pointSecond
            val x = pointSecond.x
            val y = m1 * x + b1
            return Point(x, y)
        }
        // Calculate intersection point
        val x = (b2 - b1) / (m1 - m2)
        val y = m1 * x + b1

        return Point(x, y)
    }

    fun calculateAngle(
        pointFirst: Point,
        pointSecond: Point,
    ): Double =
        kotlin.math.atan2(
            (pointSecond.y - pointFirst.y),
            (pointSecond.x - pointFirst.x),
        )

    fun calculatePerpendicularAngle(angle: Double): Double = angle + kotlin.math.PI / 2

    fun calculateTranslation(
        point: Point,
        angle: Double,
        distance: Double,
    ): Point {
        val x = point.x + distance * kotlin.math.cos(angle)
        val y = point.y + distance * kotlin.math.sin(angle)
        return Point(x, y)
    }
}
