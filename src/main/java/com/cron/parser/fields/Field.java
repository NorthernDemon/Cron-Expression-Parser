package com.cron.parser.fields;

public abstract class Field {

    private final int min;
    private final int max;

    public Field(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "Field{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }

}
