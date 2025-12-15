package repository.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.hateoas.RepresentationModel

data class SignsResponseDto
    @JsonCreator
    constructor(
        @param:JsonProperty("signs")
        val signs: List<SignResponseDto>,
    ) : RepresentationModel<SignsResponseDto>()
