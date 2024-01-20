package com.festago.support;

import com.festago.festival.repository.OldFestivalRepository;
import com.festago.festival.repository.V1FestivalRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class RepositoryBeanConfig {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Bean
    public OldFestivalRepository oldFestivalRepository() {
        return new OldFestivalRepository(jpaQueryFactory);
    }

    @Bean
    public V1FestivalRepository v1FestivalRepository() {
        return new V1FestivalRepository(jpaQueryFactory);
    }
}
