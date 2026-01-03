package repository.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class SignsResponseDto
    @JsonCreator
    constructor(
        @param:JsonProperty("signs")
        val signs: List<SignResponseDto>,
    )
