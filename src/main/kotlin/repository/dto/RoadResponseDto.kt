package repository.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.hateoas.RepresentationModel

/**
 *
 */
class RoadResponseDto
    @JsonCreator
    constructor(
        /**
         *
         */
        @param:JsonProperty("roadId") val roadId: String,
        /**
         *
         */
        @param:JsonProperty("roadNumber") val roadNumber: String,
        /**
         *
         */
        @param:JsonProperty("roadName") val roadName: String,
        /**
         *
         */
        @param:JsonProperty("category") val category: String,
    ) : RepresentationModel<RoadResponseDto>()
