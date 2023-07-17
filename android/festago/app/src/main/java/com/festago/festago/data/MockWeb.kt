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
                            path.startsWith("/tickets") -> MockResponse()
                                .setResponseCode(201)
                                .setBody(getQrCode())

                            else -> MockResponse().setResponseCode(404)
                        }
                    }

                    "GET" -> {
                        val path = request.path ?: return MockResponse().setResponseCode(404)
                        when {
                            path.startsWith("/tickets/") -> {
                                val ticketId = path.substringAfterLast("/").toLong()
                                MockResponse()
                                    .setHeader("Content-Type", "application/json")
                                    .setResponseCode(200)
                                    .setBody(getTicket(ticketId))
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
                      "stage": {
                            "id": 1,
                            "name": "테코대학교 무슨 축제",
                            "startTime": "2023-07-09T18:00:00"
                      }
                }
            """.trimIndent()
        }
    }
}
