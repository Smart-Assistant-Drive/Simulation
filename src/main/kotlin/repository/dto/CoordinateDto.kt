package repository.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

class CoordinateDto
    @JsonCreator
    constructor(
        @param:JsonProperty("x")
        val x: Float,
        @param:JsonProperty("y")
        val y: Float,
    )
