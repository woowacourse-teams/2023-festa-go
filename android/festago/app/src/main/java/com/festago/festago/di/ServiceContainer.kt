package com.festago.festago.di

import com.festago.festago.data.retrofit.AuthRetrofitClient
import com.festago.festago.data.retrofit.NormalRetrofitClient
import com.festago.festago.data.service.FestivalRetrofitService
import com.festago.festago.data.service.ReservationTicketRetrofitService
import com.festago.festago.data.service.TicketRetrofitService
import com.festago.festago.data.service.UserRetrofitService

class ServiceContainer(
    normalRetrofitClient: NormalRetrofitClient,
    authRetrofitClient: AuthRetrofitClient,
) {

    val festivalRetrofitService: FestivalRetrofitService =
        normalRetrofitClient.festivalRetrofitService

    val reservationTicketRetrofitService: ReservationTicketRetrofitService =
        normalRetrofitClient.reservationTicketRetrofitService

    val ticketRetrofitService: TicketRetrofitService =
        authRetrofitClient.ticketRetrofitService

    val userRetrofitService: UserRetrofitService =
        authRetrofitClient.userRetrofitService
}
