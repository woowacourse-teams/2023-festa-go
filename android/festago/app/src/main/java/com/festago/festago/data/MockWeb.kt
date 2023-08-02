package com.festago.festago.data

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

class MockWeb {
    private val mockWebServer = MockWebServer()

    lateinit var url: String

    init {
        val thread = Thread {
            mockWebServer.dispatcher = dispatcher
            mockWebServer.url("/")
            url = mockWebServer.url("").toString()
        }
        thread.start()
        thread.join()
    }

    companion object {

        val dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val path = request.path ?: return MockResponse().setResponseCode(404)
                return when (request.method) {
                    "POST" -> {
                        when {
                            path.startsWith("/member-tickets") -> MockResponse()
                                .setResponseCode(201)
                                .setBody(getQrCode())

                            else -> MockResponse().setResponseCode(404)
                        }
                    }

                    "GET" -> {
                        val path = request.path ?: return MockResponse().setResponseCode(404)
                        when {
                            path.startsWith("/member-tickets/") -> {
                                val ticketId = path.substringAfterLast("/").toLong()
                                MockResponse()
                                    .setHeader("Content-Type", "application/json")
                                    .setResponseCode(200)
                                    .setBody(getTicket(ticketId))
                            }

                            path.startsWith("/member-tickets") -> {
                                MockResponse()
                                    .setHeader("Content-Type", "application/json")
                                    .setResponseCode(200)
                                    .setBody(getTickets())
                            }

                            path.startsWith("/festivals") -> {
                                MockResponse()
                                    .setHeader("Content-Type", "application/json")
                                    .setResponseCode(200)
                                    .setBody(getFestivals())
                            }

                            else -> MockResponse().setResponseCode(404)
                        }
                    }

                    else -> MockResponse().setResponseCode(404)
                }
            }
        }

        private fun getQrCode(): String {
            return """
                {
                	"code": "https://github.com/woowacourse-teams/2023-festa-go",
                	"period": 30
                }
            """.trimIndent()
        }

        private fun getTicket(id: Long): String {
            return """
                {
                    "id": $id,
                    "number": 103,
                    "entryTime": "2023-07-09T16:00:00",
                    "state": "BEFORE_ENTRY",
                    "reservedAt": "2023-07-09T08:00:00",
                    "stage": {
                        "id": 1,
                        "startTime": "2023-07-09T18:00:00"
                    },
                    "festival": {
                        "id": 1,
                        "name": "테코대학교",
                        "thumbnail": "https://image.png"
                    }
                }
            """.trimIndent()
        }

        private fun getTickets(): String {
            return """ 
                {
	                "memberTickets": [
		            {
			            "id": 1,
			            "number": 103,
			            "entryTime": "2023-07-09T16:00:00",
			            "state": "BEFORE_ENTRY",
			            "reservedAt": "2023-07-09T08:00:00",
			            "stage": {
				        "id": 1,
				        "startTime": "2023-07-09T18:00:00"
			        },
			            "festival": {
				            "id": 1,
				            "name": "테코대학교",
				            "thumbnail": "https://image.png"
			            }
		            }
	            ]
            }
            """.trimIndent()
        }

        private fun getFestivals(): String {
            return """
                {
                	"festivals": [
                		{
                			"id": 1,
                			"name": "테코대학교1",
                			"startDate": "2023-06-26",
                			"endDate": "2023-06-30",
                			"thumbnail": "https://images.unsplash.com/photo-1506157786151-b8491531f063"
                		},
                        {       
                			"id": 2,
                			"name": "테코대학교2",
                			"startDate": "2023-06-26",
                			"endDate": "2023-06-30",
                			"thumbnail": "https://images.unsplash.com/photo-1506157786151-b8491531f063"
                		},
                        {
                			"id": 1,
                			"name": "테코대학교3",
                			"startDate": "2023-06-26",
                			"endDate": "2023-06-30",
                			"thumbnail": "https://images.unsplash.com/photo-1506157786151-b8491531f063"
                		}
                	]
                }
            """.trimIndent()
        }
    }
}
