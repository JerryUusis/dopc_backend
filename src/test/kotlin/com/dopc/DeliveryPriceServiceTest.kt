package com.dopc

import org.junit.jupiter.api.Test
import com.dopc.DeliveryPriceService.ApiDataType
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import java.net.http.HttpClient
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import java.net.URI
import java.net.http.HttpResponse

@SpringBootTest
@TestPropertySource(properties = ["delivery.service.baseUrl=https://test-api.com"]) // Use mock URL for testing
class DeliveryPriceServiceTest {

    @MockkBean
    private lateinit var httpClient: HttpClient // Mock HTTP Client

    @Autowired
    private lateinit var service: DeliveryPriceService

    @Test
    fun `should call the correct URL for venue data`() {

        // Mock response object
        val mockResponse = mockk<HttpResponse<String>>()
        every { mockResponse.body()} returns "Mocked API response"

        // Mock httpClient's send method to return the fake response
        // https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html#send(java.net.http.HttpRequest,java.net.http.HttpResponse.BodyHandler)
        every { httpClient.send(any(), any<HttpResponse.BodyHandler<String>>()) } returns mockResponse

        // Call the method under test
        val response = service.getVenueData(ApiDataType.DYNAMIC, "test-venue-slug")

        // Verify the correct API call was made
        // https://mockk.io/#verification-atleast-atmost-or-exactly-times
        verify(exactly = 1) {
            httpClient.send(
                match { it.uri() == URI("https://test-api.com/test-venue-slug/dynamic") },
                any<HttpResponse.BodyHandler<String>>()
            )
        }

        assertThat(response).isNotNull
        assertThat(response?.body()).isEqualTo("Mocked API response")
    }

}
