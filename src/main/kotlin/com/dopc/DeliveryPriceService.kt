package com.dopc

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class DeliveryPriceService(@Value("\${delivery.service.baseUrl}") private val baseUrl: String) {

    enum class ApiDataType(val type: String) { // Type safety for building the valid URI
        DYNAMIC("dynamic"),
        STATIC("static");
    }

    fun getVenueData(apiDataType: ApiDataType, venueSlug: String): HttpResponse<String>? {
        val client = HttpClient.newBuilder().build() // Used for sending requests and retrieving responses
        val request = HttpRequest.newBuilder()
            .uri(URI("$baseUrl/$venueSlug/${apiDataType.type}")).build() // Request static or dynamic venue data from external API
        return client.send(request, HttpResponse.BodyHandlers.ofString()) // Return response object if request was successful
    }
}