package com.cron.parser.expressions;

import com.cron.parser.fields.Field;

import java.util.Set;

public abstract class Expression {

    protected Expression expression;

    protected abstract boolean match(String input);

    protected abstract Set<Integer> interpret(String input, Field field);

    public Set<Integer> parse(String input, Field field) {
        if (match(input)) {
            return interpret(input, field);
        } else {
            return expression.parse(input, field);
        }
    }

    protected int parseInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new ExpressionParserException("Cannot parse input " + input + " to an integer value", e);
        }
    }

}
