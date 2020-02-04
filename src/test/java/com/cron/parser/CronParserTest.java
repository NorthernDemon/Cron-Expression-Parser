package com.cron.parser;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class CronParserTest {

    private CronParser cronParser;

    @Before
    public void setUp() {
        cronParser = new CronParser("*/15","0","1,15", "*", "1-5", "/usr/bin/find");
    }

    @Test
    public void testStats() throws IOException {
        String expected = Files.readString(Paths.get("./src/test/resources/cron_parser.txt"));
        String result = cronParser.getStats();

        assertEquals(expected, result);
    }

}
