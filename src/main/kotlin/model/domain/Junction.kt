package model.domain

import model.math.Point

data class Junction(
    val position: Point,
    val connections: Set<JunctionConnection> = emptySet(),
// Directions are pairs of Direction indexes and Point, where Point is the next position in that direction
)

data class JunctionConnection(
    val directionIndex: Int,
    val nextPositionIndex: Int,
    val nextPosition: Point,
    val isEntry: Boolean,
    val isExit: Boolean,
)
