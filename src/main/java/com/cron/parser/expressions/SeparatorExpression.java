package com.cron.parser.expressions;

import com.cron.parser.fields.Field;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class SeparatorExpression extends Expression {

    private static final String TOKEN = ",";

    public SeparatorExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    protected boolean match(String input) {
        return input.contains(TOKEN);
    }

    @Override
    protected Set<Integer> interpret(String input, Field field) {
        return Arrays
                .stream(input.split(TOKEN, -1))
                .map(slice -> expression.parse(slice, field))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

}
