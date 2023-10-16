package com.festago.festago.data.repository

import com.festago.festago.model.Festival
import com.festago.festago.model.Reservation
import com.festago.festago.model.ReservationStage
import com.festago.festago.model.ReservationTicket
import com.festago.festago.model.ReservationTickets
import com.festago.festago.model.TicketType
import com.festago.festago.repository.FestivalRepository
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class FestivalFakeRepository @Inject constructor() : FestivalRepository {

    override suspend fun loadFestivals(): Result<List<Festival>> =
        Result.success(getFakeFestivals())

    private fun getFakeFestivals() = List(50) {
        Festival(
            id = it.toLong(),
            name = "${2000 + it}년 축제",
            startDate = LocalDate.now().minusDays(it.toLong()),
            endDate = LocalDate.now().plusDays(it.toLong()),
            thumbnail = "https://picsum.photos/150/150/?random",
        )
    }

    override suspend fun loadFestivalDetail(festivalId: Long): Result<Reservation> =
        Result.success(getFakeReservation(festivalId.toInt()))

    private fun getFakeReservation(id: Int) = Reservation(
        id = id,
        name = "${2000 + id}년 축제",
        startDate = LocalDate.now().minusDays(id.toLong()),
        endDate = LocalDate.now().plusDays(id.toLong()),
        thumbnail = "https://picsum.photos/150/150/?random",
        reservationStages = getFakeReservationStages(id),
    )

    private fun getFakeReservationStages(id: Int): List<ReservationStage> = List(10) {
        ReservationStage(
            id = id,
            lineUp = "라인업 $id",
            startTime = LocalDateTime.now(),
            ticketOpenTime = LocalDateTime.now().minusDays(1),
            reservationTickets = ReservationTickets(
                tickets = getFakeTicketTypes(id),
            ),
        )
    }

    private fun getFakeTicketTypes(id: Int) = listOf(
        ReservationTicket(
            id = id,
            ticketType = TicketType.STUDENT,
            remainAmount = 100,
            totalAmount = 100,
        ),
        ReservationTicket(
            id = id,
            ticketType = TicketType.VISITOR,
            remainAmount = 100,
            totalAmount = 100,
        ),
    )
}
