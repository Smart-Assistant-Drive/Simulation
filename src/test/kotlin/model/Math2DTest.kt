package model

import model.math.Math2D
import model.math.Point
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class Math2DTest {
    @Test
    fun `test calculateIntersection with intersecting lines`() {
        val pointFirst = Point(0.0, 0.0)
        val angleFirst = Math.toRadians(45.0)
        val pointSecond = Point(1.0, 0.0)
        val angleSecond = Math.toRadians(135.0)

        val intersection =
            Math2D.calculateIntersection(
                pointFirst,
                angleFirst,
                pointSecond,
                angleSecond,
            )

        assertNotNull(intersection)
        assertEquals(0.5, intersection.x, 0.01)
        assertEquals(0.5, intersection.y, 0.01)
    }

    @Test
    fun `test calculateIntersection with parallel lines`() {
        val pointFirst = Point(0.0, 0.0)
        val angleFirst = Math.toRadians(45.0)
        val pointSecond = Point(1.0, 1.0)
        val angleSecond = Math.toRadians(45.0)

        val intersection =
            Math2D.calculateIntersection(
                pointFirst,
                angleFirst,
                pointSecond,
                angleSecond,
            )

        assertNull(intersection)
    }
}
