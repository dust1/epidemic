package com.dust;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {

    private static final Logger logger1 = LoggerFactory.getLogger("StorageOperation");
    private static final Logger logger2 = LoggerFactory.getLogger("LayoutOperation");
    private static final Logger logger3 = LoggerFactory.getLogger("SystemOperation");

    @Test
    public void logPrintTest() {
        logger1.info("log1 info {}", "hello");
        logger2.info("log2 info {}", "hello");
        logger3.info("log3 info {}", "hello");

        logger1.debug("log1 info {}", "hello");
        logger2.debug("log2 info {}", "hello");
        logger3.debug("log3 info {}", "hello");

        throw new RuntimeException("null");
    }

}
