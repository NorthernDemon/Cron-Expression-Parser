package com.cron.parser.expressions;

import com.cron.parser.fields.Field;

import java.util.HashSet;
import java.util.Set;

public class StepExpression extends Expression {

    private static final String TOKEN = "/";

    public StepExpression(Expression expression) {
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
            throw new ExpressionParserException("Input " + input + " is not valid for step expression for " + field);
        }

        Set<Integer> range = expression.parse(tokens[0], field);
        int step = parseInt(tokens[1]);

        if (step < 1) {
            throw new ExpressionParserException("Step " + step + " is not valid for " + field);
        }

        int min = range.stream().mapToInt(v -> v).min().getAsInt();
        int max = range.stream().mapToInt(v -> v).max().getAsInt();

        Set<Integer> result = new HashSet<>();
        for (int value = min; value <= max; value += step) {
            result.add(value);
        }
        return result;
    }

}
