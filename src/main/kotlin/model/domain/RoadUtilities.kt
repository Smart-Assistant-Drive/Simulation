package model.domain

import model.math.Point
import model.math.Vector2D

object RoadUtilities {
    fun carMoveSafety(
        car: Car,
        directions: List<Direction>,
        junctions: Set<Junction>,
    ): Car {
        if (car.changingDirectionTo != null) {
            if (car.changingDirectionTo == car.directionIndex) {
                return carMovementWithChangeLine(car, directions)
            }
            val newDirectionIndex = car.changingDirectionTo
            val newDirection = directions[newDirectionIndex]
            val junctionDistance =
                junctions.mapIndexed { index, junction ->
                    Vector2D.fromPoints(junction.position, car.position).magnitude() to index
                }
            val closestJunctionIndex = junctionDistance.minByOrNull { it.first }!!.second
            val closestJunction = junctions.elementAt(closestJunctionIndex)
            val selectedConnection =
                closestJunction.connections
                    .first { it.directionIndex == newDirectionIndex }
            val connectionPointIndex = selectedConnection.nextPositionIndex
            val connectionPoint = selectedConnection.nextPosition
            var newReachingPointIndex = connectionPointIndex
            val distance = Vector2D.fromPoints(car.position, connectionPoint).magnitude()
            var newDistance = distance
            var newReachingPoint = connectionPoint
            var newReachingPointIndexLoop = newReachingPointIndex
            do {
                newReachingPointIndexLoop = (newReachingPointIndexLoop + 1) % newDirection.roads[0].size
                val newReachingPointLoop = newDirection.roads[0][newReachingPointIndexLoop]
                val loopDistance = Vector2D.fromPoints(car.position, newReachingPointLoop).magnitude()
                if (loopDistance <= newDistance) {
                    newReachingPointIndex = newReachingPointIndexLoop
                    newReachingPoint = newReachingPointLoop
                    newDistance = loopDistance
                }
            } while (newDistance <= distance && newReachingPointIndexLoop - connectionPointIndex < 3)
            if (newDistance > distance) {
                newReachingPointIndex = connectionPointIndex
                newReachingPoint = connectionPoint
            }
            val newVector = Vector2D.fromPoints(car.position, newReachingPoint)
            if (newVector.magnitude() <= 10.0) {
                var nextPointIndex = newReachingPointIndex
                var nextPoint: Point
                do {
                    nextPointIndex = (nextPointIndex + 1) % newDirection.roads[0].size
                    nextPoint = newDirection.roads[0][nextPointIndex]
                } while (nextPoint == newReachingPoint)
                val nextVector = Vector2D.fromPoints(newReachingPoint, nextPoint).normalize()
                return car
                    .setPosition(newReachingPoint)
                    .setDirection(nextVector)
                    .setDirectionIndex(car.changingDirectionTo, 0, nextPointIndex)
            } else {
                return car.setDirection(newVector.normalize()).move()
            }
        } else {
            return carMovementWithChangeLine(car, directions)
        }
    }

    private fun carMovementWithChangeLine(
        car: Car,
        directions: List<Direction>,
    ): Car {
        val newCar = car.move()
        val newPosition = newCar.position
        val currentDirection = directions[car.directionIndex]
        val currentRoad = currentDirection.roads[car.lineIndex]
        val currentPoint = currentRoad[car.reachPointIndex]
        val vector = Vector2D.fromPoints(newPosition, currentPoint)
        if (car.changingLineTo != null) {
            val changingRoad = currentDirection.roads[car.changingLineTo]
            val changingPoint = changingRoad[car.reachPointIndex]
            val changeVector = Vector2D.fromPoints(newPosition, changingPoint)
            if (vector.normalize() == car.direction || vector == Vector2D.Companion.zero()) {
                if (vector == Vector2D.zero()) {
                    val newPointIndex = (car.reachPointIndex + 1) % changingRoad.size
                    val newPoint = changingRoad[newPointIndex]
                    val newVector = Vector2D.fromPoints(newPosition, newPoint)
                    return newCar.setDirection(newVector.normalize()).setClosesPointIndex(car.reachPointIndex)
                }
                return newCar.setDirection(changeVector.normalize())
            } else {
                return carMovement(newCar, changeVector, changingRoad, changingPoint) { car, newReachedPointIndex ->
                    car
                        .setLineIndex(car.changingLineTo!!, newReachedPointIndex)
                        .changeLineTo(null)
                }
            }
        } else {
            return carMovement(newCar, vector, currentRoad, currentPoint)
        }
    }

    private fun carMovement(
        car: Car,
        vector: Vector2D,
        currentRoad: List<Point>,
        currentPoint: Point,
        changedCarPosition: (Car, Int) -> Car = { c, index -> c.setClosesPointIndex(index) },
    ): Car {
        val distance = vector.magnitude()
        if (distance > 10.0) {
            return car
        } else {
            var newPointIndex = car.reachPointIndex
            var newPoint: Point
            do {
                newPointIndex = (newPointIndex + 1) % currentRoad.size
                newPoint = currentRoad[newPointIndex]
            } while (newPoint == currentPoint)

            val newVector = Vector2D.fromPoints(currentPoint, newPoint).normalize()
            return changedCarPosition(
                car
                    .setPosition(currentPoint)
                    .setDirection(newVector),
                newPointIndex,
            )
        }
    }

    fun changeLine(
        car: Car,
        directions: List<Direction>,
    ): Car {
        if (car.changingLineTo != null || car.changingDirectionTo != null) {
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

    fun changeDirection(
        car: Car,
        junctions: Set<Junction>,
    ): Car {
        if (car.changingDirectionTo != null && car.directionIndex != car.changingDirectionTo) {
            return car
        }
        val turn =
            junctions
                .map { junction ->
                    val connection = junction.connections.find { c -> c.directionIndex == car.directionIndex }!!
                    Vector2D.fromPoints(connection.nextPosition, car.position)
                }.any { vector -> vector.magnitude() <= 40.0 }
        if (turn && car.changingDirectionTo == null) {
            when (car.directionIndex) {
                0, 1 -> {
                    return if (car.position.y > 500) {
                        car.changeDirectionTo(3)
                    } else {
                        car.changeDirectionTo(2)
                    }
                }

                2, 3 -> {
                    (0..1).random().let { randomDirection ->
                        return car.changeDirectionTo(randomDirection)
                    }
                }
            }
        } else if (!turn && car.changingDirectionTo != null) {
            return car.changeDirectionTo(null).changeLineTo(null)
        }
        return car
    }
}
