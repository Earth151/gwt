package com.shared;

import java.util.stream.IntStream;

public class Util {

    private static final int HIGH_NUMBER_DIAPASON = 1001;
    private static final String NUMBER_REGEX = "^[0-9]+$";

    public static final int NUMBER_CONSTANT_2 = 2;
    public static final int NUMBER_CONSTANT_10 = 10;
    public static final int NUMBER_CONSTANT_30 = 30;
    public static final int NUMBER_CONSTANT_31 = 31;
    public static final int NUMBER_CONSTANT_50 = 50;

    private Util() {
        throw new UnsupportedOperationException("Cannot create utility class: " + getClass().getSimpleName());
    }

    /**
     * @param amount the amount to validate
     * @return true if valid, false if invalid
     */
    public static boolean isAmountValid(String amount) {
        return amount.matches(NUMBER_REGEX)
                && Integer.parseInt(amount) >= NUMBER_CONSTANT_2
                && Integer.parseInt(amount) <= NUMBER_CONSTANT_50;
    }

    /**
     * @param length length of array to generate
     * @return created array of elements
     */
    public static int[] generateArray(int length) {
        int[] array = IntStream
                .range(0, length)
                .map(i -> (int) (Math.random() * HIGH_NUMBER_DIAPASON))
                .toArray();
        array[length - 1] = (int) (Math.random() * NUMBER_CONSTANT_31);
        return array;
    }
}