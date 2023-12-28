package com.festago.festival.dto;

public record DetailTicketResponse(
    Long id,
    String ticketType,
    Integer totalAmount,
    Integer remainAmount) {

}
