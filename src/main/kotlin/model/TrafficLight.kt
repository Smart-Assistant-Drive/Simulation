package model

data class TrafficLight(
    val position: Point,
    val state: Int = 0, // 0: Green, 1: Yellow, 2: Red
) {
    fun changeState(): TrafficLight {
        val state = (state + 1) % 3 // Cycle through states
        return TrafficLight(position, state)
    }
}
