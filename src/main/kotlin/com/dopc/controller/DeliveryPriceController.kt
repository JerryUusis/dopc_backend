package com.dopc.controller

import com.dopc.QueryObjectDTO
import com.dopc.DeliveryPriceService
import com.dopc.DeliveryPriceService.ApiDataType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController()
class DeliveryPriceController(val deliveryPriceService: DeliveryPriceService) {
    @GetMapping("/api/v1/delivery-order-price")
    fun getDeliveryPrice(@RequestParam query: Map<String, Any>): String {
        val queryObject= QueryObjectDTO(
            venueSlug = query["venue_slug"] as String,
            cartValue = query["cart_value"].toString().toInt(),
            userLat = query["user_lat"].toString().toDouble(),
            userLon = query["user_lon"].toString().toDouble(),
        )

        val dynamicData = deliveryPriceService.getVenueData(apiDataType = ApiDataType.DYNAMIC, queryObject.venueSlug)
        return "I received your query: $queryObject"
    }
}