package com.dopc

import com.dopc.DeliveryPriceService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

@SpringBootTest
class CalculateStraightLineDistanceTest {

    @Autowired
    private lateinit var deliveryPriceService: DeliveryPriceService

    val userLat = 60.17153 // Kiasma
    val userLong = 24.93677
    val venueLat = 60.17006 // Wolt HQ
    val venueLong = 24.92806

    @Test
    fun `should return correct length straight line distance`() {  // This test
        val straightLineDistance =
            deliveryPriceService.calculateStraightLineDistance(userLat, userLong, venueLat, venueLong)
        assertThat(straightLineDistance).isEqualTo(509)
    }

    @Test
    fun `should throw error with latitude over 90 degrees`() {
        val exception = assertThrows(ResponseStatusException::class.java) {
            deliveryPriceService.calculateStraightLineDistance(91.0001, userLong, venueLat, venueLong)
        }
        assertThat(exception).hasMessageContaining("Latitude must be between -90 and 90")
        assertThat(exception.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }
}