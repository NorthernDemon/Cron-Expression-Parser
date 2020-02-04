package com.cron.parser.expressions;

import com.cron.parser.fields.Field;

import java.util.Set;

public class ValueExpression extends Expression {

    @Override
    protected boolean match(String input) {
        return true;
    }

    @Override
    protected Set<Integer> interpret(String input, Field field) {
        int value = parseInt(input);

        if (value < field.getMin()) {
            throw new ExpressionParserException("Value " + value + " is below the lower limit for " + field);
        }

        if (value > field.getMax()) {
            throw new ExpressionParserException("Value " + value + " is above the upper limit for " + field);
        }

        return Set.of(value);
    }

}
