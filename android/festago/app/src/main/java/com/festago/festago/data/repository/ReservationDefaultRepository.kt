package com.festago.festago.data.repository

import com.festago.festago.domain.model.Reservation
import com.festago.festago.domain.model.ReservationStage
import com.festago.festago.domain.model.ReservationTicket
import com.festago.festago.domain.repository.ReservationRepository
import kotlinx.coroutines.delay
import java.time.LocalDateTime

class ReservationDefaultRepository : ReservationRepository {
    private val fakeReservationTickets = listOf(
        ReservationTicket("재학생용", 219, 500),
        ReservationTicket("외부인용", 212, 300),
        ReservationTicket("외부인용", 212, 300),
    )
    private val fakeReservationStage = ReservationStage(
        id = 1,
        lineUp = "르세라핌, 아이브, 뉴진스",
        reservationTickets = fakeReservationTickets,
        startTime = LocalDateTime.now(),
        ticketOpenTime = LocalDateTime.now(),
    )
    private val fakeReservationStages = List(5) { fakeReservationStage }
    private val fakeReservation = Reservation(
        id = 1,
        name = "테코대학교",
        reservationStages = fakeReservationStages,
        startDate = LocalDateTime.now(),
        endDate = LocalDateTime.now(),
        thumbnail = "https://search2.kakaocdn.net/argon/656x0_80_wr/8vLywd3V06c",
    )

    override suspend fun loadReservation(): Result<Reservation> {
        delay(500)
//        return Result.failure(Exception())
        return Result.success(fakeReservation)
    }

    override suspend fun reserveTicket(stageId: Int, ticketId: Int): Result<Int> {
        delay(500)
        return Result.success(1)
    }
}
