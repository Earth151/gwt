package com.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.shared.Util;

import java.util.ArrayList;
import java.util.List;

import static com.shared.Util.AMOUNT_FIELD_CONTAINER;
import static com.shared.Util.ERROR_LABEL_CONTAINER;
import static com.shared.Util.ERROR_MESSAGE_WRONG_BUTTON_NUMBER;
import static com.shared.Util.ERROR_MESSAGE_WRONG_ENTERED_AMOUNT;
import static com.shared.Util.INTRO_PAGE_CONTAINER;
import static com.shared.Util.NUMBER_CONSTANT_10;
import static com.shared.Util.NUMBER_CONSTANT_2;
import static com.shared.Util.NUMBER_CONSTANT_30;
import static com.shared.Util.NUMBER_CONSTANT_50;
import static com.shared.Util.RESET_BUTTON_CONTAINER;
import static com.shared.Util.SEND_BUTTON_CONTAINER;
import static com.shared.Util.SORT_BUTTON_CONTAINER;
import static com.shared.Util.SORT_PAGE_CONTAINER;

public class IntroPage implements EntryPoint {

    private int[] currentArray;
    private int amountOfNumbers = 0;
    private FlexTable flexTable = new FlexTable();
    private List<Button> buttonsWithNumbers;
    private boolean toSortByDescending = true;

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

        // Add the amountField and sendButton to the RootPanel
        // Use RootPanel.get() to get the entire body element
        RootPanel.get(AMOUNT_FIELD_CONTAINER).add(amountField);
        RootPanel.get(SEND_BUTTON_CONTAINER).add(sendButton);
        RootPanel.get(ERROR_LABEL_CONTAINER).add(errorLabel);
        RootPanel.get(SORT_BUTTON_CONTAINER).add(sortButton);
        RootPanel.get(RESET_BUTTON_CONTAINER).add(resetButton);
        RootPanel.get(SORT_PAGE_CONTAINER).setVisible(false);

        amountField.setFocus(true);
        amountField.selectAll();

        //Creating all handlers---------------------------------------------------------------------------
        sendButton.addClickHandler(clickEvent -> {
            errorLabel.setText("");
            String amountToValid = amountField.getText();
            if (!Util.isAmountValid(amountToValid)) {
                errorLabel.setText(ERROR_MESSAGE_WRONG_ENTERED_AMOUNT);
                return;
            }
            amountOfNumbers = Integer.parseInt(amountToValid);
            currentArray = Util.generateArray(amountOfNumbers);
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
            enableOrDisableNumberButtons(false);
            if (toSortByDescending) {
                quickSortDescending(currentArray, 0, amountOfNumbers - 1);
                toSortByDescending = false;
            } else {
                quickSortAscending(currentArray, 0, amountOfNumbers - 1);
                toSortByDescending = true;
            }
            resetButton.setEnabled(true);
            sortButton.setEnabled(true);
            enableOrDisableNumberButtons(true);
            RootPanel.get(SORT_PAGE_CONTAINER).clear();
            RootPanel.get(SORT_PAGE_CONTAINER).add(createTable(currentArray));
        });
    }

    private FlexTable createTable(int[] array) {
        flexTable.removeAllRows();
        buttonsWithNumbers = new ArrayList<>(NUMBER_CONSTANT_50);
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
        int arrayIndex = column * 10;
        for (int i = arrayIndex, j = 0; i < rows + arrayIndex; i++, j++) {
            addCell(j, column, array[i]);
        }
    }

    private void addCell(int row, int column, int number) {
        Button button = new Button(String.valueOf(number));
        button.setSize("6em", "2em");
        button.setStyleName("numberButton");
        button.addClickHandler(clickEvent -> {
            int buttonNumber = Integer.parseInt(button.getText());
            if (buttonNumber <= NUMBER_CONSTANT_30 && buttonNumber >= NUMBER_CONSTANT_2) {
                RootPanel.get(SORT_PAGE_CONTAINER).clear();
                amountOfNumbers = buttonNumber;
                currentArray = Util.generateArray(amountOfNumbers);
                RootPanel.get(SORT_PAGE_CONTAINER).add(createTable(currentArray));
                toSortByDescending = true;
            } else {
                Window.alert(ERROR_MESSAGE_WRONG_BUTTON_NUMBER);
            }
        });
        buttonsWithNumbers.add(button);
        flexTable.setWidget(row, column, button);
    }

    private void quickSortDescending(int[] array, int low, int high) {
        if (array.length == 0 || low >= high) {
            return;
        }
        // выбрать опорный элемент
        int pivot = array[low + (high - low) / 2];

        // разделить на подмассивы, который больше и меньше опорного элемента
        int i = low, j = high;
        while (i <= j) {
            while (array[i] > pivot) {
                i++;
            }
            while (array[j] < pivot) {
                j--;
            }

            if (i <= j) { //меняем местами
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }
        // вызов рекурсии для сортировки левой и правой части
        if (low < j) {
            quickSortDescending(array, low, j);
        }
        if (high > i) {
            quickSortDescending(array, i, high);
        }
    }

    private void quickSortAscending(int[] array, int low, int high) {
        if (array.length == 0 || low >= high) {
            return;
        }
        int pivot = array[low + (high - low) / 2];
        int i = low, j = high;
        while (i <= j) {
            while (array[i] < pivot) {
                i++;
            }
            while (array[j] > pivot) {
                j--;
            }

            if (i <= j) {
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }
        if (low < j) {
            quickSortAscending(array, low, j);
        }
        if (high > i) {
            quickSortAscending(array, i, high);
        }
    }

    /**
     * Method enables or disables all buttons with numbers
     * in table depending on given boolean value,
     * if given value is <true> - buttons will be enabled, otherwise disabled
     *
     * @param toEnable value that means if buttons should be enabled or disabled
     */
    private void enableOrDisableNumberButtons(boolean toEnable) {
        buttonsWithNumbers.forEach(b -> b.setEnabled(toEnable));
    }
}