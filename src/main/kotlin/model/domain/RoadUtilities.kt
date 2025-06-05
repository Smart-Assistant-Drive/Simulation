package model.domain

import model.math.Point
import model.math.Vector2D

object RoadUtilities {
    fun carMoveSafety(
        car: Car,
        directions: List<Direction>,
    ): Car {
        val newCar = car.move()
        val newPosition = newCar.position
        val currentDirection = directions[car.directionIndex]
        val currentRoad = currentDirection.roads[car.lineIndex]
        val currentPoint = currentRoad[car.reachPointIndex]
        val vector = Vector2D.Companion.fromPoints(newPosition, currentPoint)
        if (car.changingLineTo != null) {
            val changingRoad = currentDirection.roads[car.changingLineTo]
            val changingPoint = changingRoad[car.reachPointIndex]
            val changeVector = Vector2D.Companion.fromPoints(newPosition, changingPoint)
            if (vector.normalize() == car.direction || vector == Vector2D.Companion.zero()) {
                if (vector == Vector2D.Companion.zero()) {
                    val newPointIndex = (car.reachPointIndex + 1) % changingRoad.size
                    val newPoint = changingRoad[newPointIndex]
                    val newVector = Vector2D.Companion.fromPoints(newPosition, newPoint)
                    return newCar.setDirection(newVector.normalize()).setClosesPointIndex(car.reachPointIndex)
                }
                return newCar.setDirection(changeVector.normalize())
            } else {
                val changeDistance = changeVector.magnitude()
                if (changeDistance < 10.0) {
                    val newReachedPointIndex = (car.reachPointIndex + 1) % changingRoad.size
                    val newReachedPoint = changingRoad[newReachedPointIndex]
                    val newVector = Vector2D.Companion.fromPoints(changingPoint, newReachedPoint)
                    return newCar
                        .setPosition(changingPoint)
                        .setDirection(newVector.normalize())
                        .setLineIndex(car.changingLineTo, newReachedPointIndex)
                        .changeLineTo(null)
                } else {
                    return newCar
                }
            }
        } else {
            val distance = vector.magnitude()
            if (distance > 10.0) {
                return newCar
            } else {
                var newPointIndex = car.reachPointIndex
                var newPoint: Point
                do {
                    newPointIndex = (newPointIndex + 1) % currentRoad.size
                    newPoint = currentRoad[newPointIndex]
                } while (newPoint == currentPoint)

                val newVector = Vector2D.Companion.fromPoints(currentPoint, newPoint).normalize()
                return newCar
                    .setPosition(currentPoint)
                    .setDirection(newVector)
                    .setClosesPointIndex(newPointIndex)
            }
        }
    }

    fun changeLine(
        car: Car,
        directions: List<Direction>,
    ): Car {
        if (car.changingLineTo != null) {
            return car
        }
        val currentDirection = directions[car.directionIndex]
        return if (car.lineIndex == currentDirection.roads.size - 1) {
            if (car.lineIndex - 1 >= 0) {
                car.changeLineTo(car.lineIndex - 1)
            } else {
                car
            }
        } else {
            car.changeLineTo(car.lineIndex + 1)
        }
    }
}
