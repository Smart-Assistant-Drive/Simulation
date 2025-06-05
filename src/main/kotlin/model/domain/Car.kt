package model.domain

import model.math.Point
import model.math.Vector2D

data class Car(
    val position: Point,
    val speed: Int = 0,
    val direction: Vector2D = Vector2D.Companion.zero(),
    val directionIndex: Int = 0, // Index of the current direction in the road
    val lineIndex: Int = 0, // Line index on the road
    val reachPointIndex: Int = 0, // Index of the closest point on the road not yet reached
    val changingLineTo: Int? = null, // Index of the line to change to, if any
) {
    fun move(): Car {
        val newX = position.x + speed * direction.x
        val newY = position.y + speed * direction.y
        return this.copy(Point(newX, newY))
    }

    fun setPosition(point: Point): Car = this.copy(position = point)

    fun setSpeed(speed: Int): Car = this.copy(speed = speed)

    fun setDirection(vector2D: Vector2D): Car = this.copy(direction = vector2D.normalize())

    fun setDirectionIndex(
        directionIndex: Int,
        lineIndex: Int,
        closesPointIndex: Int,
    ): Car =
        this.copy(
            directionIndex = directionIndex,
            lineIndex = lineIndex,
            reachPointIndex = closesPointIndex,
        )

    fun setLineIndex(
        lineIndex: Int,
        closesPointIndex: Int,
    ): Car = this.copy(lineIndex = lineIndex, reachPointIndex = closesPointIndex)

    fun setClosesPointIndex(closesPointIndex: Int): Car = this.copy(reachPointIndex = closesPointIndex)

    fun changeLineTo(lineIndex: Int?): Car = this.copy(changingLineTo = lineIndex)
}
