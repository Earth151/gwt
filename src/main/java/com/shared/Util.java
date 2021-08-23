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

    public static final String SORT_PAGE_CONTAINER = "sortPageContainer";
    public static final String INTRO_PAGE_CONTAINER = "introPageContainer";

    public static final String ERROR_LABEL_CONTAINER = "errorLabelContainer";
    public static final String ERROR_MESSAGE_WRONG_ENTERED_AMOUNT
            = "Amount invalid, must be between 2 and 50 and should not be a string";
    public static final String ERROR_MESSAGE_WRONG_BUTTON_NUMBER
            = "Please select a value smaller or equal to 30 and bigger than 1";

    public static final String AMOUNT_FIELD_CONTAINER = "amountFieldContainer";

    public static final String SEND_BUTTON_CONTAINER = "sendButtonContainer";
    public static final String RESET_BUTTON_CONTAINER = "resetButtonContainer";
    public static final String SORT_BUTTON_CONTAINER = "sortButtonContainer";

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