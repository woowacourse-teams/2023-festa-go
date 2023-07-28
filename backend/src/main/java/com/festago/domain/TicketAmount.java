package com.festago.domain;

import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TicketAmount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int reservedAmount = 0;

    private int totalAmount = 0;

    public TicketAmount() {
    }

    public void increaseReservedAmount() {
        if (reservedAmount >= totalAmount) {
            throw new BadRequestException(ErrorCode.TICKET_SOLD_OUT);
        }
        reservedAmount++;
    }

    public void addTotalAmount(int amount) {
        totalAmount += amount;
    }

    public Long getId() {
        return id;
    }

    public int getReservedAmount() {
        return reservedAmount;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }
}
