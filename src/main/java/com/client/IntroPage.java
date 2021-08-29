package com.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Main cLass that responsible for creation of the intro and sort pages,
 * page refreshing during sort, elements creation and sort logic
 */
public class IntroPage implements EntryPoint {

    private static final String NUMBER_REGEX = "^[0-9]+$";

    private static final int NUMBER_CONSTANT_2 = 2;
    private static final int NUMBER_CONSTANT_10 = 10;
    private static final int NUMBER_CONSTANT_30 = 30;
    private static final int NUMBER_CONSTANT_50 = 50;
    private static final int NUMBER_CONSTANT_1000 = 1000;

    private static final String SORT_PAGE_CONTAINER = "sortPageContainer";
    private static final String INTRO_PAGE_CONTAINER = "introPageContainer";

    private static final String ERROR_LABEL_CONTAINER = "errorLabelContainer";
    private static final String ERROR_MESSAGE_WRONG_ENTERED_AMOUNT
            = "Amount invalid, must be between 2 and 50 and should not be a string";
    private static final String ERROR_MESSAGE_WRONG_BUTTON_NUMBER
            = "Please select a value smaller or equal to 30 and bigger than 1";

    private static final String AMOUNT_FIELD_CONTAINER = "amountFieldContainer";

    private static final String SEND_BUTTON_CONTAINER = "sendButtonContainer";
    private static final String RESET_BUTTON_CONTAINER = "resetButtonContainer";
    private static final String SORT_BUTTON_CONTAINER = "sortButtonContainer";

    private int[] currentArray;
    private int amountOfNumbers = 0;
    private FlexTable flexTable = new FlexTable();
    private List<Button> currentButtonsWithNumbers;

    private boolean toSortByDescending = true;
    private boolean isButtonsShouldBeEnabled = true;
    private Timer sortTimer;
    private Timer timer;

    public void onModuleLoad() {
        flexTable.setCellSpacing(5);
        flexTable.setCellPadding(3);
        flexTable.setBorderWidth(1);
        flexTable.addStyleName("cw-FlexTable");

        Button sendButton = new Button("Enter");
        TextBox amountField = new TextBox();
        Label errorLabel = new Label();

        Button sortButton = new Button("Sort");
        Button resetButton = new Button("Reset");

        RootPanel.get(AMOUNT_FIELD_CONTAINER).add(amountField);
        RootPanel.get(SEND_BUTTON_CONTAINER).add(sendButton);
        RootPanel.get(ERROR_LABEL_CONTAINER).add(errorLabel);
        RootPanel.get(SORT_BUTTON_CONTAINER).add(sortButton);
        RootPanel.get(RESET_BUTTON_CONTAINER).add(resetButton);
        RootPanel.get(SORT_PAGE_CONTAINER).setVisible(false);

        amountField.setFocus(true);
        amountField.selectAll();

        sendButton.addClickHandler(clickEvent -> {
            errorLabel.setText("");
            String amountToValid = amountField.getText();
            if (!isAmountValid(amountToValid)) {
                errorLabel.setText(ERROR_MESSAGE_WRONG_ENTERED_AMOUNT);
                return;
            }
            amountOfNumbers = Integer.parseInt(amountToValid);
            currentArray = generateArray(amountOfNumbers);
            RootPanel.get(INTRO_PAGE_CONTAINER).setVisible(false);
            RootPanel.get(SORT_PAGE_CONTAINER).add(createTable(currentArray));
            RootPanel.get(SORT_PAGE_CONTAINER).setVisible(true);
        });

        resetButton.addClickHandler(clickEvent -> {
            errorLabel.setText("");
            amountField.setText("");
            RootPanel.get(INTRO_PAGE_CONTAINER).setVisible(true);
            RootPanel.get(SORT_PAGE_CONTAINER).clear();
            RootPanel.get(SORT_PAGE_CONTAINER).setVisible(false);
            toSortByDescending = true;
        });

        sortButton.addClickHandler(clickEvent -> {
            resetButton.setEnabled(false);
            sortButton.setEnabled(false);
            isButtonsShouldBeEnabled = false;
            disableNumberButtons();
            quickSort(currentArray, 0, amountOfNumbers - 1);
            timer = new Timer() {
                @Override
                public void run() {
                    if (!sortTimer.isRunning()) {
                        resetButton.setEnabled(true);
                        sortButton.setEnabled(true);
                        isButtonsShouldBeEnabled = true;
                        if (sortTimer != null) {
                            sortTimer.cancel();
                            sortTimer = null;
                        }
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        toSortByDescending = !toSortByDescending;
                        RootPanel.get(SORT_PAGE_CONTAINER).clear();
                        RootPanel.get(SORT_PAGE_CONTAINER).add(createTable(currentArray));
                    }
                }
            };
            timer.scheduleRepeating(NUMBER_CONSTANT_1000);
        });
    }

    /**
     * Method creates new table for showing on the page
     *
     * @param array array of elements for table
     * @return {@link FlexTable} new table for showing on the page
     */
    private FlexTable createTable(int[] array) {
        flexTable.removeAllRows();
        currentButtonsWithNumbers = new ArrayList<>(NUMBER_CONSTANT_50);
        int amount = amountOfNumbers;
        int rowsToSet;
        int columnCount = (amount / NUMBER_CONSTANT_10);
        if (amount % NUMBER_CONSTANT_10 != 0) {
            columnCount++;
        }
        for (int i = 0; i < columnCount; i++) {
            rowsToSet = amount - NUMBER_CONSTANT_10 >= 0 ? NUMBER_CONSTANT_10 : amount;
            amount -= NUMBER_CONSTANT_10;
            addColumn(rowsToSet, i, array);
        }
        return flexTable;
    }

    private void addColumn(int rows, int column, int[] array) {
        int arrayIndex = column * NUMBER_CONSTANT_10;
        for (int i = arrayIndex, j = 0; i < rows + arrayIndex; i++, j++) {
            addCell(j, column, array[i]);
        }
    }

    /**
     * Method creates new {@link Button} with given number for the table
     * New button has onClick handler that will generate and load
     * new table on the page with length described on clicked button
     *
     * @param row    row position of a cell
     * @param column column position of a cell
     * @param number number to be showed on a button
     */
    private void addCell(int row, int column, int number) {
        Button button = new Button(String.valueOf(number));
        button.setSize("6em", "2em");
        button.setStyleName("numberButton");
        if (!isButtonsShouldBeEnabled) {
            button.setEnabled(false);
        }
        button.addClickHandler(clickEvent -> {
            int buttonNumber = Integer.parseInt(button.getText());
            if (buttonNumber <= NUMBER_CONSTANT_30 && buttonNumber >= NUMBER_CONSTANT_2) {
                RootPanel.get(SORT_PAGE_CONTAINER).clear();
                amountOfNumbers = buttonNumber;
                currentArray = generateArray(amountOfNumbers);
                RootPanel.get(SORT_PAGE_CONTAINER).add(createTable(currentArray));
                toSortByDescending = true;
            } else {
                Window.alert(ERROR_MESSAGE_WRONG_BUTTON_NUMBER);
            }
        });
        currentButtonsWithNumbers.add(button);
        flexTable.setWidget(row, column, button);
    }

    private void quickSort(int[] array, int low, int high) {
        if (array.length == 0 || low >= high) {
            return;
        }
        sortTimer = null;
        sortTimer = new Timer() {
            @Override
            public void run() {
                int pivot = array[low + (high - low) / 2];
                int i = low, j = high;
                while (i <= j) {
                    if (toSortByDescending) {
                        while (array[i] > pivot) {
                            i++;
                        }
                        while (array[j] < pivot) {
                            j--;
                        }
                    } else {
                        while (array[i] < pivot) {
                            i++;
                        }
                        while (array[j] > pivot) {
                            j--;
                        }
                    }
                    if (i <= j) {
                        int temp = array[i];
                        array[i] = array[j];
                        array[j] = temp;
                        i++;
                        j--;
                        RootPanel.get(SORT_PAGE_CONTAINER).clear();
                        RootPanel.get(SORT_PAGE_CONTAINER).add(createTable(array));
                    }
                }
                if (low < j) {
                    quickSort(array, low, j);
                }
                if (high > i) {
                    quickSort(array, i, high);
                }
            }
        };
        sortTimer.schedule(NUMBER_CONSTANT_1000);
    }

    /**
     * Method disables all current buttons with numbers in table
     */
    private void disableNumberButtons() {
        currentButtonsWithNumbers.forEach(b -> b.setEnabled(false));
    }

    /**
     * Method validates input text from {@link TextBox}
     * in order to check that input string is correct number in range between 2 and 50
     *
     * @param amount the input number to validate
     * @return <true> if given string is a number that in range between 2 and 50, otherwise return <false>
     */
    private boolean isAmountValid(String amount) {
        return amount.matches(NUMBER_REGEX)
                && Integer.parseInt(amount) >= NUMBER_CONSTANT_2
                && Integer.parseInt(amount) <= NUMBER_CONSTANT_50;
    }

    /**
     * Method generates int array with numbers in range between 0 and 1000
     *
     * @param length length of array to generate
     * @return created array of elements
     */
    private int[] generateArray(int length) {
        int[] array = IntStream
                .range(0, length)
                .map(i -> (int) (Math.random() * (NUMBER_CONSTANT_1000 + 1)))
                .toArray();
        array[length - 1] = (int) (Math.random() * (NUMBER_CONSTANT_30 + 1));
        return array;
    }
}