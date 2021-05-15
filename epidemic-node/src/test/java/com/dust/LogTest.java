package com.dust;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class LogTest {

    @Test
    public void logPrintTest() {
        log.info("show info {}", "hello");
        log.debug("show debug {}", "hello");
        log.warn("show warn {}", "hello");
    }

}
