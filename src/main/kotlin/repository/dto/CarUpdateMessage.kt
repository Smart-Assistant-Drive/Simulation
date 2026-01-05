package repository.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CarUpdateMessage(
    @param:JsonProperty("idCar")
    val idCar: String,
    @param:JsonProperty("currentSpeed")
    val currentSpeed: Double,
    @param:JsonProperty("indexLane")
    val indexLane: Int,
    @param:JsonProperty("positionX")
    val positionX: Double,
    @param:JsonProperty("positionY")
    val positionY: Double,
    @param:JsonProperty("indexP")
    val indexP: Int,
    @param:JsonProperty("dPointX")
    val dPointX: Double,
    @param:JsonProperty("dPointY")
    val dPointY: Double,
)
