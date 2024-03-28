package com.festago.admin.presentation;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import io.swagger.v3.oas.annotations.Hidden;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api")
@Hidden
@RequiredArgsConstructor
public class AdminController {

    private final Optional<BuildProperties> properties;

    @GetMapping("/version")
    public ResponseEntity<String> getVersion() {
        return properties.map(it -> ResponseEntity.ok(it.getTime().atZone(ZoneId.of("Asia/Seoul")).toString()))
            .orElseGet(() -> ResponseEntity.ok()
                .body(LocalDateTime.now().toString()));
    }

    @GetMapping("/error")
    public ResponseEntity<Void> getError() {
        throw new IllegalArgumentException("테스트용 에러입니다.");
    }

    @GetMapping("/warn")
    public ResponseEntity<Void> getWarn() {
        throw new InternalServerException(ErrorCode.FOR_TEST_ERROR);
    }

    @GetMapping("/info")
    public ResponseEntity<Void> getInfo() {
        throw new BadRequestException(ErrorCode.FOR_TEST_ERROR);
    }
}
