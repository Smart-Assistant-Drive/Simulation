package repository.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.hateoas.RepresentationModel

/**
 *
 */
class DrivingFlowResponseDto
    @JsonCreator
    constructor(
        /**
         *
         */
        @param:JsonProperty("flowId") val flowId: String,
        /**
         *
         */
        @param:JsonProperty("roadId") val roadId: String,
        /**
         *
         */
        @param:JsonProperty("idDirection") val idDirection: Int,
        /**
         *
         */
        @param:JsonProperty("numOfLanes") val numOfLanes: Int,
        /**
         *
         */
        @param:JsonProperty("roadCoordinates") val roadCoordinates: List<CoordinateDto>,
    ) : RepresentationModel<DrivingFlowResponseDto>()
