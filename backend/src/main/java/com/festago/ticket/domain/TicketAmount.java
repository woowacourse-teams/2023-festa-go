package com.festago.ticket.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.util.Validator;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Min;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class TicketAmount extends BaseTimeEntity {

    @Id
    private Long id;

    @Min(value = 0)
    private int reservedAmount = 0;

    @Min(value = 0)
    private int totalAmount = 0;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public TicketAmount(Ticket ticket) {
        validate(ticket);
        this.ticket = ticket;
    }

    private void validate(Ticket ticket) {
        Validator.notNull(ticket, "ticket");
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
