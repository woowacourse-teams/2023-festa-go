package com.festago.auth.infrastructure.openid;

import com.festago.common.exception.UnexpectedException;
import io.jsonwebtoken.security.JwkSet;
import jakarta.annotation.Nullable;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CachedAppleOpenIdKeyProvider {

    private final Map<String, Key> cache = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * OpenId Key를 캐싱하여 반환하는 클래스 <br/> OpenID Id Token 헤더의 kid 값을 key로 가지도록 구현 <br/> Id Token을 검증할 때, 매번 공개키 목록을 조회하면
     * 요청이 차단될 수 있으므로 캐싱하는 과정이 필요. <br/> 따라서 kid에 대한 Key를 찾을 수 없으면, fallback을 통해 캐시를 업데이트함 <br/> 이때, 동시에 여러 요청이 들어오면 동시성
     * 문제가 발생할 수 있으므로 ReentrantLock을 사용하여 상호 배제 구현 <br/> 데드락을 방지하기 위해 ReentrantLock.tryLock() 메서드를 사용하였음 <br/> 또한 반드시
     * fallback에서 Timeout에 대한 예외 발생을 구현 해야함<br/> 존재하지 않는 kid로 계속 요청 시 fallback이 계속 호출되므로 공격 가능성이 있음. <br/>
     *
     * @param kid      캐시의 Key로 사용될 OpenId Id Token 헤더의 kid 값
     * @param fallback 캐시 미스 발생 시 캐시에 Key를 등록할 JwkSet을 반환하는 함수
     * @return 캐시 Hit의 경우 Key 반환, 캐시 Miss에서 fallback으로 반환된 JwkSet에 Key가 없으면 null 반환
     */
    @Nullable
    public Key provide(String kid, Supplier<JwkSet> fallback) {
        Key key = cache.get(kid);
        if (key != null) {
            return key;
        }
        log.info("kid에 대한 OpenId Key를 찾지 못해 Key 목록 조회를 시도합니다. kid={}", kid);
        try {
            if (lock.tryLock(5, TimeUnit.SECONDS)) {
                try {
                    key = cache.get(kid);
                    if (key != null) {
                        return key;
                    }
                    JwkSet jwkSet = fallback.get();
                    jwkSet.forEach(jwk -> cache.put(jwk.getId(), jwk.toKey()));
                    key = cache.get(kid);
                    if (key == null) {
                        log.warn("OpenId kid에 대한 Key를 찾을 수 없습니다. kid={}", kid);
                    }
                    return key;
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("스레드가 인터럽트 되었습니다.", e);
        }
        throw new UnexpectedException("OpenId Key를 가져오는 중, 락 대기로 인해 Key를 획득하지 못했습니다. kid=" + kid);
    }
}
