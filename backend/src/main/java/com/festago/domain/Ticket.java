package com.festago.domain;

import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import org.hibernate.annotations.SortNatural;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Stage stage;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private TicketAmount ticketAmount = new TicketAmount();

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ticket_id")
    @SortNatural
    private SortedSet<TicketEntryTime> ticketEntryTimes = new TreeSet<>();

    protected Ticket() {
    }

    public Ticket(Stage stage, TicketType ticketType) {
        this(null, stage, ticketType);
    }

    public Ticket(Long id, Stage stage, TicketType ticketType) {
        this.id = id;
        this.stage = stage;
        this.ticketType = ticketType;
    }

    public void addTicketEntryTime(LocalDateTime entryTime, int amount) {
        TicketEntryTime ticketEntryTime = TicketEntryTime.of(entryTime, amount, stage);
        ticketAmount.addTotalAmount(amount);
        ticketEntryTimes.add(ticketEntryTime);
    }

    public MemberTicket createMemberTicket(Member member) {
        LocalDateTime entryTime = calculateEntryTime();
        return new MemberTicket(member, stage, 1, entryTime, ticketType);
    }

    private LocalDateTime calculateEntryTime() {
        int reservedAmount = ticketAmount.getReservedAmount();
        int tempAmount = 0;
        for (TicketEntryTime ticketEntryTime : ticketEntryTimes) {
            tempAmount += ticketEntryTime.getAmount();
            if (reservedAmount < tempAmount) {
                return ticketEntryTime.getEntryTime();
            }
        }
        throw new BadRequestException(ErrorCode.TICKET_SOLD_OUT);
    }

    public void increaseReservedAmount() {
        ticketAmount.increaseReservedAmount();
    }

    public Long getId() {
        return id;
    }

    public Stage getStage() {
        return stage;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public TicketAmount getTicketAmount() {
        return ticketAmount;
    }

    public Collection<TicketEntryTime> getTicketEntryTimes() {
        return ticketEntryTimes;
    }
}
