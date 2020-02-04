package com.cron.parser;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Invalid number of input parameters. Usage example: program \"*/15 0 1,15 * 1-5 /usr/bin/find\"");
        }

        String[] fields = args[0].split("\\s+");

        if (fields.length != 6) {
            throw new IllegalArgumentException("Invalid number of cron fields. Usage example: program \"*/15 0 1,15 * 1-5 /usr/bin/find\"");
        }

        CronParser cronParser = new CronParser(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5]);

        System.out.println(cronParser.getStats());
    }

}
