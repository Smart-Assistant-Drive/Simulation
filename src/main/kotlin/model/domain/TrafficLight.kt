package model.domain

import model.math.Point

data class TrafficLight(
    val position: Point,
    val id: String,
    val state: TrafficLightState, // 0: Green, 1: Yellow, 2: Red
) {
    fun changeState(state: TrafficLightState): TrafficLight = TrafficLight(position, id, state)
}

enum class TrafficLightState {
    GREEN,
    YELLOW,
    RED,
}
