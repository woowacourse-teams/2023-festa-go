package com.festago.acceptance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("test")
public class DataInitializer implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    private List<String> tableNames;

    private List<String> metaTableNames = List.of(
        "sys_config",
        "flyway_schema_history",
        "CONSTANTS",
        "ENUM_VALUES",
        "INDEXES",
        "INDEX_COLUMNS",
        "INFORMATION_SCHEMA_CATALOG_NAME",
        "IN_DOUBT",
        "LOCKS",
        "QUERY_STATISTICS",
        "RIGHTS",
        "ROLES",
        "SESSIONS",
        "SESSION_STATE",
        "SETTINGS",
        "SYNONYMS",
        "USERS"
    );

    @Override
    public void afterPropertiesSet() {
        tableNames = new ArrayList<>();
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                if (metaTableNames.contains(tableName)) {
                    continue;
                }
                tableNames.add(tableName);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Transactional
    public void execute() {
        truncateAllTables();
    }

    private void truncateAllTables() {
        entityManager.flush();
        entityManager.createNativeQuery("SET foreign_key_checks = 0;").executeUpdate();
        tableNames.forEach(
            tableName -> executeQueryWithTable(tableName)
        );
        entityManager.createNativeQuery("SET foreign_key_checks = 1;").executeUpdate();
    }

    private void executeQueryWithTable(String tableName) {
        entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
    }
}

