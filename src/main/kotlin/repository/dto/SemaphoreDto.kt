package repository.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class SemaphoreDto(
    @param:JsonProperty("link") var link: String,
    @param:JsonProperty("road") var road: String,
    @param:JsonProperty("direction") var direction: Int,
    @param:JsonProperty("position") var positionDto: PositionDto,
) {
    companion object {
        const val READY = "ready"
        const val OFFLINE = "offline"
    }

    var idIndex = 0
}
