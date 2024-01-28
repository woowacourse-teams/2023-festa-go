package com.festago.support;

import com.festago.config.JpaAuditingConfig;
import com.festago.config.QuerydslConfig;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest(showSql = false)
@Import({QuerydslConfig.class, JpaAuditingConfig.class, RepositoryBeanConfig.class})
public @interface RepositoryTest {

}
