package repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.jackson.jackson
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule
import repository.dto.DrivingFlowResponseDto
import repository.dto.RoadResponseDto
import repository.dto.SemaphoreDto
import repository.dto.SignsResponseDto

class RemoteRepository(
    val brokerAdministration: String = "http://localhost:8080",
    val brokerPublic: String = "http://localhost:8081",
) {
    val mapper: ObjectMapper =
        ObjectMapper()
            .registerKotlinModule() // Kotlin support
            .registerModule(Jackson2HalModule()) // HAL/HATEOAS support

    val client =
        HttpClient(CIO) {
            install(ContentNegotiation) {
                jackson {
                    mapper
                }
            }
        }

    suspend fun getRoadsId(): List<String> {
        val response: HttpResponse = client.get("$brokerAdministration/roads_id")
        return response.body()
    }

    suspend fun getRoad(roadId: String): RoadResponseDto {
        val response: HttpResponse = client.get("$brokerPublic/road/$roadId")
        return response.body()
    }

    suspend fun getFlows(roadId: String): List<DrivingFlowResponseDto> {
        val response: HttpResponse = client.get("$brokerPublic/flows/$roadId")
        return response.body()
    }

    suspend fun getTrafficLights(
        roadId: String,
        direction: String,
    ): List<SemaphoreDto> {
        val response: HttpResponse =
            client.get("$brokerPublic/semaphores/filter") {
                parameter("road", roadId)
                parameter("direction", direction)
            }
        return response.body()
    }

    suspend fun getSigns(
        roadId: String,
        direction: String,
    ): SignsResponseDto {
        val response: HttpResponse = client.get("$brokerPublic/signs/$roadId/$direction")
        return response.body()
    }
}
