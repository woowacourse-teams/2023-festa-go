package com.festago.festagostaff.data

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
                            path.startsWith("/staff/tickets/validation") -> MockResponse()
                                .setResponseCode(201)
                                .setBody(getTicketValidationResponse())

                            else -> MockResponse().setResponseCode(404)
                        }
                    }

                    else -> MockResponse().setResponseCode(404)
                }
            }
        }

        private fun getTicketValidationResponse(): String {
            return """
                    {
                        "updatedState": "AFTER_ENTRY"
                    }
            """.trimIndent()
        }
    }
}
