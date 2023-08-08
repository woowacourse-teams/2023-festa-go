package com.festago.support;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstancePreConstructCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DatabaseClearExtension implements TestInstancePreConstructCallback, AfterEachCallback {

    private static final Namespace CLEANER = Namespace.create("festago.database.cleaner");
    private static final Set<String> TABLES = new HashSet<>();
    private static final String JDBC_TEMPLATE = "jdbcTemplate";

    @Override
    public void preConstructTestInstance(TestInstanceFactoryContext factoryContext, ExtensionContext context)
        throws Exception {
        JdbcTemplate jdbcTemplate = SpringExtension.getApplicationContext(context)
            .getBean(JdbcTemplate.class);
        context.getStore(CLEANER).put(JDBC_TEMPLATE, jdbcTemplate);

        initialTable(context);
        truncate(context);
    }

    @SuppressWarnings("unchecked")
    private void initialTable(ExtensionContext context) {
        if (TABLES.isEmpty()) {
            JdbcTemplate jdbcTemplate = context.getStore(CLEANER).get(JDBC_TEMPLATE, JdbcTemplate.class);
            List<String> tables = jdbcTemplate.query("SHOW TABLES", (rs, rowNum) -> rs.getString(1));
            TABLES.addAll(tables);
        }
    }

    private void truncate(ExtensionContext context) {
        JdbcTemplate jdbcTemplate = context.getStore(CLEANER).get(JDBC_TEMPLATE, JdbcTemplate.class);
        jdbcTemplate.update("SET FOREIGN_KEY_CHECKS = ?", 0);
        for (String table : TABLES) {
            jdbcTemplate.update("TRUNCATE TABLE " + table);
        }
        jdbcTemplate.update("SET FOREIGN_KEY_CHECKS = ?", 1);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        truncate(context);
        context.getStore(CLEANER).remove(JDBC_TEMPLATE);
    }
}
