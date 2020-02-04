package com.cron.parser.expressions;

import com.cron.parser.fields.Field;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RangeExpression extends Expression {

    private static final String TOKEN = "-";

    public RangeExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    protected boolean match(String input) {
        return input.contains(TOKEN);
    }

    @Override
    protected Set<Integer> interpret(String input, Field field) {
        String[] tokens = input.split(TOKEN);

        if (tokens.length != 2) {
            throw new ExpressionParserException("Input " + input + " is not valid for range expression for " + field);
        }

        int start = expression.parse(tokens[0], field).iterator().next();
        int end = expression.parse(tokens[1], field).iterator().next();

        if (start > end) {
            throw new ExpressionParserException("Input " + input + " has starting value greater than ending value");
        }

        return IntStream
                .rangeClosed(start, end)
                .boxed()
                .collect(Collectors.toSet());
    }

}
