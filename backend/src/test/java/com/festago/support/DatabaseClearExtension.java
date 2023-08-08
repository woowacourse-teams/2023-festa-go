package com.festago.support;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstancePreConstructCallback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DatabaseClearExtension implements TestInstancePreConstructCallback, AfterEachCallback {

    private static final Namespace CLEANER = Namespace.create("festago.database.cleaner");
    private static final Set<String> TABLES = new HashSet<>();
    private static final String ENTITY_MANAGER = "entityManager";

    @Override
    public void preConstructTestInstance(TestInstanceFactoryContext factoryContext, ExtensionContext context)
        throws Exception {
        EntityManagerFactory emf = SpringExtension.getApplicationContext(context)
            .getBean(EntityManagerFactory.class);
        context.getStore(CLEANER).put(ENTITY_MANAGER, emf.createEntityManager());

        initialTable(context);
        truncate(context);
    }

    @SuppressWarnings("unchecked")
    private void initialTable(ExtensionContext context) {
        if (TABLES.isEmpty()) {
            EntityManager em = context.getStore(CLEANER).get(ENTITY_MANAGER, EntityManager.class);
            List<Object[]> tableInfos = em.createNativeQuery("SHOW TABLES").getResultList();
            for (Object[] tableInfo : tableInfos) {
                String tableName = (String) tableInfo[0];
                TABLES.add(tableName);
            }
            em.clear();
        }
    }

    private void truncate(ExtensionContext context) {
        EntityManager em = context.getStore(CLEANER).get(ENTITY_MANAGER, EntityManager.class);
        em.getTransaction().begin();
        em.createNativeQuery(String.format("SET FOREIGN_KEY_CHECKS %d", 0)).executeUpdate();
        for (String table : TABLES) {
            em.createNativeQuery(String.format("TRUNCATE TABLE %s", table)).executeUpdate();
        }
        em.createNativeQuery(String.format("SET FOREIGN_KEY_CHECKS %d", 1)).executeUpdate();
        em.getTransaction().commit();
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        truncate(context);
        context.getStore(CLEANER).remove(ENTITY_MANAGER);
    }
}
