package com.github.peacetrue.flyway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author peace
 * @since 1.0
 **/
@SpringBootTest(classes = {
        FlywayReactiveAutoConfiguration.class,
})
class FlywayReactiveAutoConfigurationTest {

    @Test
    void init() {
        Assertions.assertTrue(true);
    }
}
