package com.cron.parser.fields;

import com.cron.parser.expressions.Expression;
import com.cron.parser.expressions.ExpressionFactory;
import com.cron.parser.expressions.ExpressionParserException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class MinuteParserTest {

    private Field field;

    private Expression expression;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() {
        field = new MinuteField();
        expression = ExpressionFactory.build();
    }

    @Test
    public void testBoundaries() {
        assertEquals(0, field.getMin());
        assertEquals(59, field.getMax());
    }

    @Test
    public void testValue() {
        Set<Integer> expected = Set.of(5);
        Set<Integer> result = expression.parse("5", field);

        assertEquals(expected, result);
    }

    @Test
    public void testValueLowerBound() {
        Set<Integer> expected = Set.of(0);
        Set<Integer> result = expression.parse("0", field);

        assertEquals(expected, result);
    }

    @Test
    public void testValueUpperBound() {
        Set<Integer> expected = Set.of(59);
        Set<Integer> result = expression.parse("59", field);

        assertEquals(expected, result);
    }

    @Test
    public void testValueLowerBoundOverflow() {
        exceptionRule.expect(ExpressionParserException.class);
        exceptionRule.expectMessage("Cannot parse input  to an integer value");

        expression.parse("-1", field);
    }

    @Test
    public void testValueUpperBoundOverflow() {
        exceptionRule.expect(ExpressionParserException.class);
        exceptionRule.expectMessage("Value 60 is above the upper limit for Field{min=0, max=59}");

        expression.parse("60", field);
    }

    @Test
    public void testValueInvalidInput() {
        exceptionRule.expect(ExpressionParserException.class);
        exceptionRule.expectMessage("Cannot parse input a to an integer value");

        expression.parse("a", field);
    }

    @Test
    public void testRange() {
        Set<Integer> expected = Set.of(15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25);
        Set<Integer> result = expression.parse("15-25", field);

        assertEquals(expected, result);
    }

    @Test
    public void testRangeBalanced() {
        Set<Integer> expected = Set.of(15);
        Set<Integer> result = expression.parse("15-15", field);

        assertEquals(expected, result);
    }

    @Test
    public void testRangeLowerBoundary() {
        Set<Integer> expected = Set.of(0, 1, 2, 3, 4, 5);
        Set<Integer> result = expression.parse("0-5", field);

        assertEquals(expected, result);
    }

    @Test
    public void testRangeUpperBoundary() {
        Set<Integer> expected = Set.of(55, 56, 57, 58, 59);
        Set<Integer> result = expression.parse("55-59", field);

        assertEquals(expected, result);
    }

    @Test
    public void testRangeUnbalanced() {
        exceptionRule.expect(ExpressionParserException.class);
        exceptionRule.expectMessage("Input 10-5 has starting value greater than ending value");

        expression.parse("10-5", field);
    }

    @Test
    public void testRangeMissingInput() {
        exceptionRule.expect(ExpressionParserException.class);
        exceptionRule.expectMessage("Input 5- is not valid for range expression for Field{min=0, max=59}");

        expression.parse("5-", field);
    }

    @Test
    public void testRangeInvalidInput() {
        exceptionRule.expect(ExpressionParserException.class);
        exceptionRule.expectMessage("Cannot parse input * to an integer value");

        expression.parse("5-*", field);
    }

    @Test
    public void testAnyValue() {
        Set<Integer> expected = IntStream
                .rangeClosed(0, 59)
                .boxed()
                .collect(Collectors.toSet());
        Set<Integer> result = expression.parse("*", field);

        assertEquals(expected, result);
    }

    @Test
    public void testValueStep() {
        Set<Integer> expected = Set.of(15);
        Set<Integer> result = expression.parse("15/1", field);

        assertEquals(expected, result);
    }

    @Test
    public void testRangeStepLarge() {
        Set<Integer> expected = Set.of(15);
        Set<Integer> result = expression.parse("15-25/11", field);

        assertEquals(expected, result);
    }

    @Test
    public void testRangeStep() {
        Set<Integer> expected = Set.of(15, 17, 19, 21, 23, 25);
        Set<Integer> result = expression.parse("15-25/2", field);

        assertEquals(expected, result);
    }

    @Test
    public void testRangeStepMissingLeftInput() {
        exceptionRule.expect(ExpressionParserException.class);
        exceptionRule.expectMessage("Cannot parse input  to an integer value");

        expression.parse("/5", field);
    }

    @Test
    public void testRangeStepMissingRightInput() {
        exceptionRule.expect(ExpressionParserException.class);
        exceptionRule.expectMessage("Input 15-25/ is not valid for step expression for Field{min=0, max=59}");

        expression.parse("15-25/", field);
    }

    @Test
    public void testRangeStepInvalidInput() {
        exceptionRule.expect(ExpressionParserException.class);
        exceptionRule.expectMessage("Cannot parse input a to an integer value");

        expression.parse("15-25/a", field);
    }

    @Test
    public void testRangeStepInputUnderflow() {
        exceptionRule.expect(ExpressionParserException.class);
        exceptionRule.expectMessage("Step 0 is not valid for Field{min=0, max=59}");

        expression.parse("15-25/0", field);
    }

    @Test
    public void testAnyValueStep() {
        Set<Integer> expected = Set.of(0, 10, 20, 30, 40, 50);
        Set<Integer> result = expression.parse("*/10", field);

        assertEquals(expected, result);
    }

    @Test
    public void testValueSeparator() {
        Set<Integer> expected = Set.of(5, 7, 15);
        Set<Integer> result = expression.parse("5,7,15", field);

        assertEquals(expected, result);
    }

    @Test
    public void testRangeSeparator() {
        Set<Integer> expected = Set.of(5, 6, 7, 8, 9, 10, 15, 16, 17);
        Set<Integer> result = expression.parse("5-10,15-17", field);

        assertEquals(expected, result);
    }

    @Test
    public void testValueAndRangeSeparator() {
        Set<Integer> expected = Set.of(5, 15, 16, 17);
        Set<Integer> result = expression.parse("5,15-17", field);

        assertEquals(expected, result);
    }

    @Test
    public void testAnyValueSeparator() {
        Set<Integer> expected = IntStream
                .rangeClosed(0, 59)
                .boxed()
                .collect(Collectors.toSet());
        Set<Integer> result = expression.parse("*,15", field);

        assertEquals(expected, result);
    }

    @Test
    public void testStepSeparator() {
        Set<Integer> expected = Set.of(5, 8, 15, 17, 19, 21, 23, 25);
        Set<Integer> result = expression.parse("5-10/3,15-25/2", field);

        assertEquals(expected, result);
    }

    @Test
    public void testSeparatorMissingLeft() {
        exceptionRule.expect(ExpressionParserException.class);
        exceptionRule.expectMessage("Cannot parse input  to an integer value");

        expression.parse(",15-17/2", field);
    }

    @Test
    public void testSeparatorMissingRight() {
        exceptionRule.expect(ExpressionParserException.class);
        exceptionRule.expectMessage("Cannot parse input  to an integer value");

        expression.parse("15-17/2,", field);
    }

}
