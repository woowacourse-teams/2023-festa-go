package com.festago.domain;

import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Entity
public class TicketAmount {

    @Id
    private Long id;

    private int reservedAmount = 0;

    private int totalAmount = 0;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    protected TicketAmount() {
    }

    public TicketAmount(Ticket ticket) {
        this.ticket = ticket;
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

    public int calculateRemainAmount() {
        return totalAmount - reservedAmount;
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
