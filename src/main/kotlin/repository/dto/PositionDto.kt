package repository.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PositionDto(
    @param:JsonProperty("x") val x: Float,
    @param:JsonProperty("y") val y: Float,
)
