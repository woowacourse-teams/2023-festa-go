package com.festago.festival.presentation.v1;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.dto.ErrorResponse;
import com.festago.festival.application.FestivalV1QueryService;
import com.festago.festival.dto.FestivalV1ListRequest;
import com.festago.festival.dto.FestivalV1ListResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/festivals")
@RequiredArgsConstructor
public class FestivalV1Controller {

    private final FestivalV1QueryService festivalV1QueryService;

    @GetMapping
    public ResponseEntity<FestivalV1ListResponse> findFestivals(
        FestivalV1ListRequest festivalListRequest) {
        return ResponseEntity.ok(festivalV1QueryService.findFestivals(festivalListRequest));
    }

    // TODO : Serializer에서 BadReqeustException 발생하면 Exception으로 예외 처리되기에 임시로 배치하였음.
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handle(BadRequestException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.from(e));
    }
}
