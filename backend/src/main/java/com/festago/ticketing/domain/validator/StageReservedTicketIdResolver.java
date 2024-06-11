package com.festago.ticketing.domain.validator;

import jakarta.annotation.Nullable;

public interface StageReservedTicketIdResolver {

    /**
     * 사용자가 StageTicket에 대해 예매한 티켓의 식별자를 반환합니다. <br/> 예매한 이력이 없으면 null이 반환되고, 예매한 이력이 있으면 사용자가 예매했던 티켓의 식별자를 반환합니다.
     * <br/>
     *
     * @param memberId 티켓을 예매한 사용자의 식별자
     * @param stageId  StageTicket의 Stage 식별자
     * @return 예매한 이력이 없으면 null, 예매한 이력이 있으면 티켓 식별자 반환
     */
    @Nullable
    Long resolve(Long memberId, Long stageId);
}
