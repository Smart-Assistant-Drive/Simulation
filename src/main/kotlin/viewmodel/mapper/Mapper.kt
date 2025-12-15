package viewmodel.mapper

import model.domain.Direction
import model.domain.TrafficLight
import model.domain.TrafficSign
import repository.dto.DrivingFlowResponseDto
import repository.dto.SemaphoreDto
import repository.dto.SignResponseDto

object Mapper {
    fun DrivingFlowResponseDto.convert(): Direction =
        model.domain.DirectionFactory.createDirection(
            this.coordinates.map {
                model.math.Point(it.first.toDouble(), it.second.toDouble())
            },
            this.numOfLanes,
        )

    fun SemaphoreDto.convert(): TrafficLight =
        TrafficLight(
            position = model.math.Point(this.positionDto.x.toDouble(), this.positionDto.y.toDouble()),
            id = this.idIndex.toString(),
            state = model.domain.TrafficLightState.RED,
        )

    fun SignResponseDto.convert(): TrafficSign =
        TrafficSign(
            position = model.math.Point(this.latitude, this.longitude),
            signType = this.type + (this.speedLimit?.let { "_$it" } ?: ""),
        )
}
