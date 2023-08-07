package com.festago.support;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstancePreConstructCallback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DatabaseClearExtension implements TestInstancePreConstructCallback, AfterEachCallback {

    @Override
    public void preConstructTestInstance(TestInstanceFactoryContext factoryContext, ExtensionContext context)
        throws Exception {
        clear(context);
    }

    private void clear(ExtensionContext context) {
        DatabaseCleaner databaseCleaner = getDataCleaner(context);
        databaseCleaner.clear();
    }

    private DatabaseCleaner getDataCleaner(ExtensionContext extensionContext) {
        return SpringExtension.getApplicationContext(extensionContext)
            .getBean(DatabaseCleaner.class);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        clear(context);
    }
}
