package com.cron.parser.expressions;

import com.cron.parser.fields.Field;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AnyValueExpression extends Expression {

    private static final String TOKEN = "*";

    public AnyValueExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    protected boolean match(String input) {
        return input.equals(TOKEN);
    }

    @Override
    protected Set<Integer> interpret(String input, Field field) {
        return IntStream
                .rangeClosed(field.getMin(), field.getMax())
                .boxed()
                .collect(Collectors.toSet());
    }

}
