package com.cron.parser.expressions;

public class ExpressionParserException extends RuntimeException {

    public ExpressionParserException(String message) {
        super(message);
    }

    public ExpressionParserException(String message, RuntimeException exception) {
        super(message, exception);
    }

}
