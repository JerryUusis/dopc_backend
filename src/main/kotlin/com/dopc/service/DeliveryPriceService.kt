package com.dopc.service

import com.dopc.exception.InvalidCoordinatesException
import com.dopc.exception.InvalidSurchargeParametersException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.math.*

@Service
class DeliveryPriceService(
    @Value("\${delivery.service.baseUrl}") private val baseUrl: String,
    private val httpClient: HttpClient
) {

    enum class ApiDataType(val type: String) { // Type safety for building the valid URI
        DYNAMIC("dynamic"),
        STATIC("static");
    }

    fun getVenueData(apiDataType: ApiDataType, venueSlug: String): HttpResponse<String>? {
        val request = HttpRequest.newBuilder()
            .uri(URI("$baseUrl/$venueSlug/${apiDataType.type}"))
            .build() // Request static or dynamic venue data from external API

        return httpClient.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        ) // Return response object if request was successful
    }

    // Use haversine formula for calculating straight line distance
    // https://mapsplatform.google.com/resources/blog/how-calculate-distances-map-maps-javascript-api/
    fun calculateStraightLineDistance(userLat: Double, userLong: Double, venueLat: Double, venueLong: Double): Int {
        // Validate latitude
        // https://kotlinlang.org/docs/ranges.html#ranges
        if (userLat !in -90.0..90.0 || venueLat !in -90.0..90.0) {
            throw InvalidCoordinatesException("Latitude must be between -90 and 90")
        }
        // Validate longitude
        if (userLong !in -180.0..180.0 || venueLong !in -180.0..180.0) {
            throw InvalidCoordinatesException("Longitude must be between -180 and 180")
        }

        val radius = 6371.071 // Radius of the Earth in kilometers

        val userLatRad = Math.toRadians(userLat)  // Convert degrees to radians
        val venueLatRad = Math.toRadians(venueLat)
        val diffLat = venueLatRad - userLatRad  // Radian difference (latitudes)
        val diffLong = Math.toRadians(venueLong - userLong) // Radian difference (longitudes)

        val a = sin(diffLat / 2).pow(2) +
                cos(userLatRad) * cos(venueLatRad) * sin(diffLong / 2).pow(2)

        val distance = 2 * radius * asin(sqrt(a))

        return (distance * 1000).roundToInt() // Return in meters
    }

    fun getSurcharge(valueInCents: Int, max: Int): Int {
        if (valueInCents > max) {
            throw InvalidSurchargeParametersException("valueInCents: $valueInCents can't be larger than max: $max")
        }
        return max - valueInCents
    }
}