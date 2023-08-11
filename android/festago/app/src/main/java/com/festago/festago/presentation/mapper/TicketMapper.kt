package com.festago.festago.presentation.mapper

import com.festago.festago.domain.model.Ticket
import com.festago.festago.presentation.model.MemberTicketUiModel
import com.festago.festago.presentation.model.TicketUiModel
import java.time.LocalDateTime

fun Ticket.toPresentation(): TicketUiModel = TicketUiModel(
    id = id,
    number = number,
    entryTime = entryTime,
    condition = condition.toPresentation(),
    stage = stage.toPresentation(),
    reserveAt = reserveAt,
    festivalId = festivalTicket.id,
    festivalName = festivalTicket.name,
    festivalThumbnail = festivalTicket.thumbnail,
)

fun TicketUiModel.toMemberTicketModel(): MemberTicketUiModel = MemberTicketUiModel(
    id = id,
    number = number,
    entryTime = entryTime,
    condition = condition,
    stage = stage,
    reserveAt = reserveAt,
    festivalId = festivalId,
    festivalName = festivalName,
    festivalThumbnail = festivalThumbnail,
    canEntry = LocalDateTime.now().isAfter(entryTime),
)

fun List<Ticket>.toPresentation(): List<TicketUiModel> =
    this.map { ticket -> ticket.toPresentation() }

fun List<TicketUiModel>.toMemberTicketModel(): List<MemberTicketUiModel> =
    this.map { ticketUiModel -> ticketUiModel.toMemberTicketModel() }
