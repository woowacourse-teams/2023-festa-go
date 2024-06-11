package com.festago.ticketing.domain;

import com.festago.common.exception.BadRequestException;

/**
 * 티켓의 재고를 관리하는 도메인 <br/> 해당 도메인을 구현하는 구현체는 반드시 원자적인 연산을 사용해야 한다. <br/>
 */
public interface TicketQuantity {

    /**
     * 티켓의 매진 여부를 반환한다.
     *
     * @return 티켓이 매진이면 true, 매진이 아니면 false
     */
    boolean isSoldOut();

    /**
     * 티켓의 재고를 하나 감소시킨다. <br/> 해당 메서드의 연산은 atomic 해야 한다. <br/> 감소 시킨 뒤 값이 음수인 경우에는 매진이 된 상태에 요청이 들어온 것이므로, 예외를 던져야 한다.
     * <br/>
     *
     * @throws BadRequestException 감소 시킨 뒤 값이 음수이면
     */
    void decreaseQuantity() throws BadRequestException;

    /**
     * 티켓의 재고를 하나 증가시킨다. <br/> 해당 메서드의 연산은 atomic 해야 한다. <br/>
     */
    void increaseQuantity();

    /**
     * 티켓의 남은 재고를 반환한다. <br/>
     *
     * @return 티켓의 남은 재고
     */
    int getQuantity();
}
