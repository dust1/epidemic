package com.dust;

import com.dust.logs.LogParser;
import com.dust.logs.entity.LayoutLog;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.font.LayoutPath;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Test
    public void parserTest() throws IOException {
        var file = new File("log/layout.log");
        var read = new BufferedReader(new FileReader(file));

        List<LayoutLog> list = new ArrayList<>();
        String line;
        long start = System.currentTimeMillis();
        while ((line = read.readLine()) != null) {
            LayoutLog layoutLog = LogParser.parser.parseLayout(line);
            list.add(layoutLog);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println(list);
    }

}
