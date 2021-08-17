package com.shared;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

public class Util {

    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
    public static final String SERVER_ERROR = "An error occurred while "
            + "attempting to contact the server. Please check your network "
            + "connection and try again.";

    private static final int HIGH_DIAPASON = 1001;

    private Util() {
        throw new UnsupportedOperationException("Cannot create utility class: " + getClass().getSimpleName());
    }

    /**
     * @param amount the amount of numbers to validate
     * @return true if valid, false if invalid
     */
    public static boolean isAmountValid(int amount) {
        return amount > 1 && amount <= 50;
    }

    /**
     * @param length length of array to generate
     * @return created array of elements
     */
    public static int[] generateArray(int length) {
        return IntStream.range(0, length)
                .map(i -> (int) (Math.random() * 1001))
                .toArray();
    }

//    public static int findMax(int[] array) {
//        return Arrays.stream(array)
//                .max()
//                .orElseThrow(NoSuchElementException::new);
//    }
//
//    public static int findMin(int[] array) {
//        return Arrays.stream(array)
//                .min()
//                .orElseThrow(NoSuchElementException::new);
//    }
}
