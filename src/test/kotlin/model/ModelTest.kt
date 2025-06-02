package model

import kotlin.test.Test
import kotlin.test.assertEquals

class ModelTest {
    @Test
    fun `test createDirection with simple points`() {
        val points =
            listOf(
                Point(0.0, 0.0),
                Point(1.0, 0.0),
                Point(2.0, 1.0),
            )
        val lines = 2

        val direction = DirectionFactory.createDirection(points, lines)

        assertEquals(lines, direction.roads.size)
        assertEquals(3, direction.roads[0].lines.size)
        assertEquals(3, direction.roads[1].lines.size)
    }

    @Test
    fun `test createDirection test Points simple lines`() {
        val points =
            listOf(
                Point(0.0, 0.0),
                Point(1.0, 0.0),
                Point(2.0, 0.0),
            )
        val lines = 3

        val direction = DirectionFactory.createDirection(points, lines)
        assertEquals(0.0, direction.roads[0].lines[0].x, 0.01)
        assertEquals(0.0, direction.roads[0].lines[0].y, 0.01)
        assertEquals(0.0, direction.roads[1].lines[0].x, 0.01)
        assertEquals(20.0, direction.roads[1].lines[0].y, 0.01)
        assertEquals(0.0, direction.roads[2].lines[0].x, 0.01)
        assertEquals(40.0, direction.roads[2].lines[0].y, 0.01)
    }

    @Test
    fun `test createDirection test Points complex lines`() {
        val points =
            listOf(
                Point(0.0, 0.0),
                Point(1.0, 1.0),
                Point(2.0, 0.0),
            )
        val lines = 2

        val direction = DirectionFactory.createDirection(points, lines)
        assertEquals(0.0, direction.roads[0].lines[0].x, 0.01)
        assertEquals(0.0, direction.roads[0].lines[0].y, 0.01)
        assertEquals(-14.14, direction.roads[1].lines[0].x, 0.01)
        assertEquals(14.14, direction.roads[1].lines[0].y, 0.01)
    }
}
