package com.festago.support;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;

@SpringBootTest
@TestExecutionListeners(value = {
    ResetMockTestExecutionListener.class,
    DatabaseClearTestExecutionListener.class,
    CacheClearTestExecutionListener.class
}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@Import({TestTimeConfig.class})
public abstract class ApplicationIntegrationTest {

}
