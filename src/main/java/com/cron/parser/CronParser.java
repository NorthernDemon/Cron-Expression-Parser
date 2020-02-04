package com.cron.parser;

import com.cron.parser.expressions.*;
import com.cron.parser.fields.*;

import java.util.Set;
import java.util.stream.Collectors;

public class CronParser {

    private final Set<Integer> minute;
    private final Set<Integer> hour;
    private final Set<Integer> dayOfMonth;
    private final Set<Integer> month;
    private final Set<Integer> dayOfWeek;
    private final String command;

    private final Expression expression = ExpressionFactory.build();

    public CronParser(
        String minute,
        String hour,
        String dayOfMonth,
        String month,
        String dayOfWeek,
        String command
    ) {
        this.minute = expression.parse(minute, new MinuteField());
        this.hour = expression.parse(hour, new HourField());
        this.dayOfMonth = expression.parse(dayOfMonth, new DayOfMonthField());
        this.month = expression.parse(month, new MonthField());
        this.dayOfWeek = expression.parse(dayOfWeek, new DayOfWeekField());
        this.command = command;
    }

    public String getStats() {
        return
                "minute        " + collect(minute) + "\n" +
                "hour          " + collect(hour) + "\n" +
                "day of month  " + collect(dayOfMonth) + "\n" +
                "month         " + collect(month) + "\n" +
                "day of week   " + collect(dayOfWeek) + "\n" +
                "command       " + command;
    }

    private String collect(Set<Integer> range) {
        return range.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
    }

}
