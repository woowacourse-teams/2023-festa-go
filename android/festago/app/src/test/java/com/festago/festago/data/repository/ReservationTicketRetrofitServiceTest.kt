package com.festago.festago.data.repository

import com.festago.festago.data.dto.ReservationFestivalResponse
import com.festago.festago.data.dto.ReservationStageResponse
import com.festago.festago.data.dto.ReservationTicketResponse
import com.festago.festago.data.dto.ReservationTicketsResponse
import com.festago.festago.data.dto.ReservedTicketRequest
import com.festago.festago.data.dto.ReservedTicketResponse
import com.festago.festago.data.service.FestivalRetrofitService
import com.festago.festago.data.service.ReservationTicketRetrofitService
import com.festago.festago.data.service.TicketRetrofitService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.net.HttpURLConnection

@OptIn(ExperimentalCoroutinesApi::class)
class ReservationTicketRetrofitServiceTest {
    private lateinit var fakeServer: MockWebServer
    private lateinit var reservationTicketRetrofitService: ReservationTicketRetrofitService
    private lateinit var festivalRetrofitService: FestivalRetrofitService
    private lateinit var ticketRetrofitService: TicketRetrofitService

    @Before
    fun setUp() {
        fakeServer = MockWebServer()
        fakeServer.start()

        reservationTicketRetrofitService = Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(fakeServer.url("/"))
            .build()
            .create(ReservationTicketRetrofitService::class.java)

        festivalRetrofitService = Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(fakeServer.url("/"))
            .build()
            .create(FestivalRetrofitService::class.java)

        ticketRetrofitService = Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(fakeServer.url("/"))
            .build()
            .create(TicketRetrofitService::class.java)
    }

    @Test
    fun `대학 축제 번호를 넣으면 해당 무대 예약 목록들을 받아온다`() = runTest {
        // given
        val fakeResponse = MockResponse()
            .setHeader("Content-Type", "application/json")
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(getFestival())

        fakeServer.enqueue(fakeResponse)

        // when
        val response = festivalRetrofitService.getFestivalDetail(1)

        // then
        assertThat(response.isSuccessful).isTrue
        assertThat(response.body()).isEqualTo(getFakeFestival())
    }

    @Test
    fun `대학 축제 번호를 넣고 json 형식이 아니면 에러를 던진다`() = runTest {
        // given
        val fakeResponse = MockResponse()
            .setHeader("Content-Type", "application/json")
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("")

        fakeServer.enqueue(fakeResponse)

        // when
        val result = runCatching { festivalRetrofitService.getFestivalDetail(1) }

        // then
        result.onSuccess { assertThat(false).isTrue }
            .onFailure {
                assertThat(true).isTrue
                println(it)
            }
    }

    @Test
    fun `대학 축제 번호를 넣고 값을 못받아오면 에러를 던진다`() = runTest {
        // given
        val fakeResponse = MockResponse()
            .setHeader("Content-Type", "application/json")
            .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)

        fakeServer.enqueue(fakeResponse)

        // when
        val response = festivalRetrofitService.getFestivalDetail(1)

        // then
        assertThat(response.isSuccessful).isFalse
        assertThat(response.body()).isNull()
    }

    @Test
    fun `무대 예약 번호를 넣으면 티켓 종료를 받아온다`() = runTest {
        // given
        val fakeResponse = MockResponse()
            .setHeader("Content-Type", "application/json")
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(getReservationTickets())

        fakeServer.enqueue(fakeResponse)

        // when
        val response = reservationTicketRetrofitService.getReservationTickets(1)

        // then
        assertThat(response.isSuccessful).isTrue
        assertThat(response.body()).isEqualTo(getFakeTickets())
    }

    @Test
    fun `무대 예약 번호를 넣고 json 형식이 아니면 에러를 던진다`() = runTest {
        // given
        val fakeResponse = MockResponse()
            .setHeader("Content-Type", "application/json")
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("")

        fakeServer.enqueue(fakeResponse)

        // when
        val result = runCatching { reservationTicketRetrofitService.getReservationTickets(1) }

        // then
        result.onSuccess { assertThat(false).isTrue }
            .onFailure {
                assertThat(true).isTrue
                println(it)
            }
    }

    @Test
    fun `무대 예약 번호를 넣고 값을 못받아오면 에러를 던진다`() = runTest {
        // given
        val fakeResponse = MockResponse()
            .setHeader("Content-Type", "application/json")
            .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)

        fakeServer.enqueue(fakeResponse)

        // when
        val response = reservationTicketRetrofitService.getReservationTickets(1)

        // then
        assertThat(response.isSuccessful).isFalse
        assertThat(response.body()).isNull()
    }

    @Test
    fun `티켓 번호를 넣으면 예약된 티켓을 받아온다`() = runTest {
        // given
        val fakeResponse = MockResponse()
            .setHeader("Content-Type", "application/json")
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(getReservedTicket())

        fakeServer.enqueue(fakeResponse)

        // when
        val response = ticketRetrofitService.postReserveTicket(ReservedTicketRequest(1))

        // then
        assertThat(response.isSuccessful).isTrue
        assertThat(response.body()).isEqualTo(getFakeReservedTicket())
    }

    companion object {
        private fun getFakeFestival(): ReservationFestivalResponse {
            return ReservationFestivalResponse(
                id = 1,
                schoolId = 1,
                name = "테코대학교",
                startDate = "2023-07-03",
                endDate = "2023-07-09",
                thumbnail = "https://image.png",
                stages = listOf(
                    ReservationStageResponse(
                        id = 1,
                        startTime = "2023-07-09T16:00:00",
                        ticketOpenTime = "2023-07-08T14:00:00",
                        lineUp = "르세라핌,아이브,뉴진스",
                        tickets = listOf(
                            ReservationTicketResponse(
                                id = 1,
                                ticketType = "STUDENT",
                                totalAmount = 500,
                                remainAmount = 219,
                            ),
                            ReservationTicketResponse(
                                id = 2,
                                ticketType = "VISITOR",
                                totalAmount = 300,
                                remainAmount = 212,
                            ),
                        ),
                    ),
                    ReservationStageResponse(
                        id = 2,
                        startTime = "2023-07-09T16:00:00",
                        ticketOpenTime = "2023-07-08T14:00:00",
                        lineUp = "르세라핌,아이브,뉴진스",
                        tickets = listOf(
                            ReservationTicketResponse(
                                id = 3,
                                ticketType = "STUDENT",
                                totalAmount = 500,
                                remainAmount = 219,
                            ),
                            ReservationTicketResponse(
                                id = 4,
                                ticketType = "VISITOR",
                                totalAmount = 300,
                                remainAmount = 212,
                            ),
                        ),
                    ),
                ),
            )
        }

        private fun getFestival(): String {
            return """
{
	"id": 1,
    "schoolId": 1,
	"name": "테코대학교",
	"startDate": "2023-07-03",
	"endDate": "2023-07-09",	
	"thumbnail": "https://image.png",
	"stages" : [
		{
			"id": 1,
			"startTime": "2023-07-09T16:00:00",
			"ticketOpenTime": "2023-07-08T14:00:00",
			"lineUp": "르세라핌,아이브,뉴진스",
			"tickets" : [
				{
					"id": 1,
					"ticketType" : "STUDENT",
					"totalAmount" : 500,
					"remainAmount" : 219
				},
				{
					"id": 2,
					"ticketType" : "VISITOR",
					"totalAmount" : 300,
					"remainAmount" : 212
				}
			]
		},
		{
				"id": 2,
				"startTime": "2023-07-09T16:00:00",
				"ticketOpenTime": "2023-07-08T14:00:00",
				"lineUp": "르세라핌,아이브,뉴진스",
				"tickets" : [
					{
						"id": 3,
						"ticketType" : "STUDENT",
						"totalAmount" : 500,
						"remainAmount" : 219
					},
					{
						"id": 4,
						"ticketType" : "VISITOR",
						"totalAmount" : 300,
						"remainAmount" : 212
					}
				]
			}
	]
}
            """.trimIndent()
        }

        private fun getFakeTickets(): ReservationTicketsResponse {
            return ReservationTicketsResponse(
                tickets = listOf(
                    ReservationTicketResponse(
                        id = 1,
                        ticketType = "STUDENT",
                        totalAmount = 500,
                        remainAmount = 219,
                    ),
                    ReservationTicketResponse(
                        id = 2,
                        ticketType = "VISITOR",
                        totalAmount = 300,
                        remainAmount = 212,
                    ),
                ),
            )
        }

        private fun getReservationTickets(): String {
            return """
{
			"tickets" : [
				{
					"id": 1,
					"ticketType" : "STUDENT",
					"totalAmount" : 500,
					"remainAmount" : 219
				},
				{
					"id": 2,
					"ticketType" : "VISITOR",
					"totalAmount" : 300,
					"remainAmount" : 212
				}
			]
}
            """.trimIndent()
        }

        fun getFakeReservedTicket(): ReservedTicketResponse {
            return ReservedTicketResponse(
                id = 1,
                number = 103,
                entryTime = "2023-07-09T16:00:00",
            )
        }

        fun getReservedTicket(): String {
            return """
                {
                  "id": 1,
                  "number": 103,
                  "entryTime": "2023-07-09T16:00:00"
                }
            """.trimIndent()
        }
    }
}
