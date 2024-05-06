package com.festago.admin.application;

import com.festago.admin.repository.AdminFestivalIdResolverQueryDslRepository;
import com.festago.admin.repository.AdminStageIdResolverQueryDslRepository;
import com.festago.festival.application.FestivalQueryInfoArtistRenewService;
import com.festago.stage.application.StageQueryInfoService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminQueryInfoRenewalService {

    private final FestivalQueryInfoArtistRenewService festivalQueryInfoArtistRenewService;
    private final StageQueryInfoService stageQueryInfoService;
    private final AdminStageIdResolverQueryDslRepository adminStageIdResolverQueryDslRepository;
    private final AdminFestivalIdResolverQueryDslRepository adminFestivalIdResolverQueryDslRepository;

    public void renewalByFestivalId(Long festivalId) {
        festivalQueryInfoArtistRenewService.renewArtistInfo(festivalId);
        adminStageIdResolverQueryDslRepository.findStageIdsByFestivalId(festivalId)
            .forEach(stageQueryInfoService::renewalStageQueryInfo);
    }

    public void renewalByFestivalStartDatePeriod(LocalDate to, LocalDate end) {
        List<Long> festivalIds = adminFestivalIdResolverQueryDslRepository.findFestivalIdsByStartDatePeriod(to, end);
        log.info("{}개의 축제에 대해 QueryInfo를 새로 갱신합니다.", festivalIds.size());
        festivalIds.forEach(festivalQueryInfoArtistRenewService::renewArtistInfo);
        adminStageIdResolverQueryDslRepository.findStageIdsByFestivalIdIn(festivalIds)
            .forEach(stageQueryInfoService::renewalStageQueryInfo);
    }
}
