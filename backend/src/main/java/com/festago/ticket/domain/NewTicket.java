package com.festago.ticket.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.util.Validator;
import com.festago.ticketing.domain.Booker;
import com.festago.ticketing.domain.ReserveTicket;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

// TODO NewTicket -> Ticket 이름 변경할 것
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class NewTicket extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected Long schoolId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar")
    protected TicketExclusive ticketExclusive;

    protected int amount = 0;

    /**
     * 사용자가 최대 예매할 수 있는 티켓의 개수
     */
    protected int maxReserveAmount = 1;

    protected NewTicket(Long id, Long schoolId, TicketExclusive ticketExclusive) {
        Validator.notNull(schoolId, "schoolId");
        Validator.notNull(ticketExclusive, "ticketExclusive");
        this.id = id;
        this.schoolId = schoolId;
        this.ticketExclusive = ticketExclusive;
    }

    protected void changeAmount(int amount) {
        Validator.notNegative(amount, "amount");
        this.amount = amount;
    }

    public boolean isStudentOnly() {
        return ticketExclusive == TicketExclusive.STUDENT;
    }

    public boolean isSchoolStudent(Booker booker) {
        return Objects.equals(this.schoolId, booker.getSchoolId());
    }

    public void changeMaxReserveAmount(int maxReserveAmount) {
        Validator.minValue(maxReserveAmount, 1, "maxReserveAmount");
        this.maxReserveAmount = maxReserveAmount;
    }

    public abstract void validateReserve(Booker booker, LocalDateTime currentTime);

    /**
     * 티켓을 예매한다. 해당 메서드를 호출하기 전 반드시 validateReserve() 메서드를 호출해야 한다.<br/> 반환된 ReserveTicket은 영속되지 않았으므로, 반드시 영속시켜야 한다.
     *
     * @param booker   예매할 사용자
     * @param sequence 예매할 티켓의 순번
     * @return 영속되지 않은 상태의 ReserveTicket
     */
    public abstract ReserveTicket reserve(Booker booker, int sequence);

    public abstract LocalDateTime getTicketingEndTime();

    public boolean isEmptyAmount() {
        return amount <= 0;
    }

    public Long getId() {
        return id;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public TicketExclusive getTicketExclusive() {
        return ticketExclusive;
    }

    public int getAmount() {
        return amount;
    }

    public int getMaxReserveAmount() {
        return maxReserveAmount;
    }
}
