package model.domain

import model.math.Point

data class TrafficSign(
    val position: Point,
    val signType: String, // e.g., "stop", "yield", "speed_limit"
)
