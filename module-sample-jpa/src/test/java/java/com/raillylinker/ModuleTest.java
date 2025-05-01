package java.com.raillylinker;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ModuleTest {
    @org.jetbrains.annotations.NotNull
    private static final Logger classLogger = LoggerFactory.getLogger(ModuleTest.class);

    @Test
    public void test() {
        classLogger.info("test");
    }
}
