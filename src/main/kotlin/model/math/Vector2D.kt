package model.math

import java.lang.Math.toDegrees
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

data class Vector2D(
    val x: Double,
    val y: Double,
) {
    operator fun plus(other: Vector2D): Vector2D = Vector2D(x + other.x, y + other.y)

    operator fun minus(other: Vector2D): Vector2D = Vector2D(x - other.x, y - other.y)

    operator fun times(scalar: Double): Vector2D = Vector2D(x * scalar, y * scalar)

    operator fun div(scalar: Double): Vector2D = Vector2D(x / scalar, y / scalar)

    fun magnitude(): Double = sqrt(x.pow(2) + y.pow(2))

    fun normalize(): Vector2D {
        val mag = magnitude()
        return if (mag == 0.0) Vector2D(0.0, 0.0) else Vector2D(x / mag, y / mag)
    }

    fun degree(): Double = toDegrees(atan2(y, x))

    companion object {
        fun fromPoints(
            p1: Point,
            p2: Point,
        ): Vector2D = Vector2D(p2.x - p1.x, p2.y - p1.y)

        fun zero(): Vector2D = Vector2D(0.0, 0.0)
    }
}
