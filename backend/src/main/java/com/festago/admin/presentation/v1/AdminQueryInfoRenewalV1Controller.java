package com.festago.admin.presentation.v1;

import com.festago.admin.application.AdminQueryInfoRenewalService;
import com.festago.admin.dto.queryinfo.QueryInfoRenewalFestivalPeriodV1Request;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api/v1/query-info/renewal")
@RequiredArgsConstructor
@Hidden
public class AdminQueryInfoRenewalV1Controller {

    private final AdminQueryInfoRenewalService queryInfoRenewalService;

    @PostMapping("/festival-id/{festivalId}")
    public ResponseEntity<Void> renewalByFestivalId(
        @PathVariable Long festivalId
    ) {
        queryInfoRenewalService.renewalByFestivalId(festivalId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/festival-period")
    public ResponseEntity<Void> renewalByFestivalStartDatePeriod(
        @RequestBody @Valid QueryInfoRenewalFestivalPeriodV1Request request
    ) {
        queryInfoRenewalService.renewalByFestivalStartDatePeriod(request.to(), request.end());
        return ResponseEntity.ok().build();
    }
}
