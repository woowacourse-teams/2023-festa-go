package com.festago.ticketing.domain;

import com.festago.common.exception.BadRequestException;

/**
 * 티켓의 재고와 순서를 관리하는 도메인 <br/> 해당 도메인을 구현하는 구현체는 반드시 원자적인 연산을 사용해야 한다. <br/>
 */
public interface TicketSequence {

    /**
     * 티켓의 매진 여부를 반환한다.
     *
     * @return 티켓이 매진이면 true, 매진이 아니면 false
     */
    boolean isSoldOut();

    /**
     * 티켓의 재고를 하나 감소시키고 순서를 반환한다. <br/> 해당 메서드의 연산은 atomic 해야 한다. <br/> 티켓의 재고가 비어있을 때 해당 메서드를 호출하면 BadRequestException을
     * 던져야 한다. <br/>
     *
     * @throws BadRequestException 티켓의 재고가 비어있으면
     */
    int reserve() throws BadRequestException;

    /**
     * 티켓의 재고를 하나 증가시키고 인자로 들어온 순서를 다시 보관한다. <br/> 해당 메서드의 연산은 atomic 해야 한다. <br/>
     *
     * @param sequence 티켓의 순서
     */
    void cancel(int sequence);

    /**
     * 티켓의 남은 재고를 반환한다. <br/>
     *
     * @return 티켓의 남은 재고
     */
    int getQuantity();
}
