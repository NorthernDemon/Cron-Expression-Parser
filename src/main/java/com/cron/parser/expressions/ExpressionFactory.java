package com.cron.parser.expressions;

public abstract class ExpressionFactory {

    public static Expression build() {
        Expression valueExpression = new ValueExpression();
        Expression rangeExpression = new RangeExpression(valueExpression);
        Expression anyValueExpression = new AnyValueExpression(rangeExpression);
        Expression stepExpression = new StepExpression(anyValueExpression);
        return new SeparatorExpression(stepExpression);
    }

}
