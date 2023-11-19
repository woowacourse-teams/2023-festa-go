package com.festago.support;

import com.festago.config.QuerydslConfig;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest
@Import(QuerydslConfig.class)
public @interface RepositoryTest {

}
