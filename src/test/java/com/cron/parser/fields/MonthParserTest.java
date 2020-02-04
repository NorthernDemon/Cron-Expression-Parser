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

public class MonthParserTest {

    private Field field;

    private Expression expression;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() {
        field = new MonthField();
        expression = ExpressionFactory.build();
    }

    @Test
    public void testBoundaries() {
        assertEquals(1, field.getMin());
        assertEquals(12, field.getMax());
    }

    @Test
    public void testValue() {
        Set<Integer> expected = Set.of(5);
        Set<Integer> result = expression.parse("5", field);

        assertEquals(expected, result);
    }

    @Test
    public void testValueLowerBound() {
        Set<Integer> expected = Set.of(1);
        Set<Integer> result = expression.parse("1", field);

        assertEquals(expected, result);
    }

    @Test
    public void testValueUpperBound() {
        Set<Integer> expected = Set.of(12);
        Set<Integer> result = expression.parse("12", field);

        assertEquals(expected, result);
    }

    @Test
    public void testValueLowerBoundOverflow() {
        exceptionRule.expect(ExpressionParserException.class);
        exceptionRule.expectMessage("Value 0 is below the lower limit for Field{min=1, max=12}");

        expression.parse("0", field);
    }

    @Test
    public void testValueUpperBoundOverflow() {
        exceptionRule.expect(ExpressionParserException.class);
        exceptionRule.expectMessage("Value 13 is above the upper limit for Field{min=1, max=12}");

        expression.parse("13", field);
    }

    @Test
    public void testValueInvalidInput() {
        exceptionRule.expect(ExpressionParserException.class);
        exceptionRule.expectMessage("Cannot parse input a to an integer value");

        expression.parse("a", field);
    }

    @Test
    public void testRange() {
        Set<Integer> expected = Set.of(5, 6, 7, 8, 9, 10);
        Set<Integer> result = expression.parse("5-10", field);

        assertEquals(expected, result);
    }

    @Test
    public void testRangeBalanced() {
        Set<Integer> expected = Set.of(5);
        Set<Integer> result = expression.parse("5-5", field);

        assertEquals(expected, result);
    }

    @Test
    public void testRangeLowerBoundary() {
        Set<Integer> expected = Set.of(1, 2, 3, 4, 5);
        Set<Integer> result = expression.parse("1-5", field);

        assertEquals(expected, result);
    }

    @Test
    public void testRangeUpperBoundary() {
        Set<Integer> expected = Set.of(10, 11, 12);
        Set<Integer> result = expression.parse("10-12", field);

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
        exceptionRule.expectMessage("Input 5- is not valid for range expression for Field{min=1, max=12}");

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
                .rangeClosed(1, 12)
                .boxed()
                .collect(Collectors.toSet());
        Set<Integer> result = expression.parse("*", field);

        assertEquals(expected, result);
    }

    @Test
    public void testValueStep() {
        Set<Integer> expected = Set.of(5);
        Set<Integer> result = expression.parse("5/1", field);

        assertEquals(expected, result);
    }

    @Test
    public void testRangeStepLarge() {
        Set<Integer> expected = Set.of(5);
        Set<Integer> result = expression.parse("5-10/11", field);

        assertEquals(expected, result);
    }

    @Test
    public void testRangeStep() {
        Set<Integer> expected = Set.of(5, 7, 9);
        Set<Integer> result = expression.parse("5-10/2", field);

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
        exceptionRule.expectMessage("Input 5-10/ is not valid for step expression for Field{min=1, max=12}");

        expression.parse("5-10/", field);
    }

    @Test
    public void testRangeStepInvalidInput() {
        exceptionRule.expect(ExpressionParserException.class);
        exceptionRule.expectMessage("Cannot parse input a to an integer value");

        expression.parse("5-10/a", field);
    }

    @Test
    public void testRangeStepInputUnderflow() {
        exceptionRule.expect(ExpressionParserException.class);
        exceptionRule.expectMessage("Step 0 is not valid for Field{min=1, max=12}");

        expression.parse("5-10/0", field);
    }

    @Test
    public void testAnyValueStep() {
        Set<Integer> expected = Set.of(1, 11);
        Set<Integer> result = expression.parse("*/10", field);

        assertEquals(expected, result);
    }

    @Test
    public void testValueSeparator() {
        Set<Integer> expected = Set.of(5, 7, 10);
        Set<Integer> result = expression.parse("5,7,10", field);

        assertEquals(expected, result);
    }

    @Test
    public void testRangeSeparator() {
        Set<Integer> expected = Set.of(1, 2, 3, 4, 5, 10, 11, 12);
        Set<Integer> result = expression.parse("1-5,10-12", field);

        assertEquals(expected, result);
    }

    @Test
    public void testValueAndRangeSeparator() {
        Set<Integer> expected = Set.of(5, 10, 11, 12);
        Set<Integer> result = expression.parse("5,10-12", field);

        assertEquals(expected, result);
    }

    @Test
    public void testAnyValueSeparator() {
        Set<Integer> expected = IntStream
                .rangeClosed(1, 12)
                .boxed()
                .collect(Collectors.toSet());
        Set<Integer> result = expression.parse("*,12", field);

        assertEquals(expected, result);
    }

    @Test
    public void testStepSeparator() {
        Set<Integer> expected = Set.of(1, 4, 10, 12);
        Set<Integer> result = expression.parse("1-5/3,10-12/2", field);

        assertEquals(expected, result);
    }

    @Test
    public void testSeparatorMissingLeft() {
        exceptionRule.expect(ExpressionParserException.class);
        exceptionRule.expectMessage("Cannot parse input  to an integer value");

        expression.parse(",5-10/2", field);
    }

    @Test
    public void testSeparatorMissingRight() {
        exceptionRule.expect(ExpressionParserException.class);
        exceptionRule.expectMessage("Cannot parse input  to an integer value");

        expression.parse("5-10/2,", field);
    }

}
