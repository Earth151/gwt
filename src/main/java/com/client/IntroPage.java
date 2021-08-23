package com.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.shared.Util;

import static com.shared.Util.NUMBER_CONSTANT_10;
import static com.shared.Util.NUMBER_CONSTANT_30;

public class IntroPage implements EntryPoint {

    private int[] currentArray;
    private int amountOfNumbers = 0;
    private FlexTable flexTable = new FlexTable();
    private boolean toSortByDescending = true;

    public void onModuleLoad() {
        flexTable.setCellSpacing(5);
        flexTable.setCellPadding(3);
        flexTable.setBorderWidth(1);
        flexTable.addStyleName("cw-FlexTable");

        Button sendButton = new Button("Send");
        TextBox amountField = new TextBox();
        Label errorLabel = new Label();

        Button sortButton = new Button("Sort");
        Button resetButton = new Button("Reset");

        // Add the amountField and sendButton to the RootPanel
        // Use RootPanel.get() to get the entire body element
        RootPanel.get("amountFieldContainer").add(amountField);
        RootPanel.get("sendButtonContainer").add(sendButton);
        RootPanel.get("errorLabelContainer").add(errorLabel);
        RootPanel.get("sortButtonContainer").add(sortButton);
        RootPanel.get("resetButtonContainer").add(resetButton);
        RootPanel.get("sortPageContainer").setVisible(false);

        amountField.setFocus(true);
        amountField.selectAll();

        //Creating all handlers---------------------------------------------------------------------------
        sendButton.addClickHandler(clickEvent -> {
            errorLabel.setText("");
            String amountToValid = amountField.getText();
            if (!Util.isAmountValid(amountToValid)) {
                errorLabel.setText("Amount invalid, must be between 2 and 50 and should not be a string");
                return;
            }
            amountOfNumbers = Integer.parseInt(amountToValid);
            currentArray = Util.generateArray(amountOfNumbers);
            RootPanel.get("introPageContainer").setVisible(false);
            RootPanel.get("sortPageContainer").add(createTable(currentArray));
            RootPanel.get("sortPageContainer").setVisible(true);
        });

        resetButton.addClickHandler(clickEvent -> {
            errorLabel.setText("");
            amountField.setText("");
            RootPanel.get("introPageContainer").setVisible(true);
            RootPanel.get("sortPageContainer").clear();
            RootPanel.get("sortPageContainer").setVisible(false);
            toSortByDescending = true;
        });

        sortButton.addClickHandler(clickEvent -> {
            //Sorting logic
            int[] array = currentArray;
            if (toSortByDescending) {
                quickSortDescending(array, 0, amountOfNumbers - 1);
                toSortByDescending = false;
            } else {
                quickSortAscending(array, 0, amountOfNumbers - 1);
                toSortByDescending = true;
            }
        });
    }

    private FlexTable createTable(int[] array) {
        flexTable.removeAllRows();
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
            if (Integer.parseInt(button.getText()) <= NUMBER_CONSTANT_30) {
                RootPanel.get("sortPageContainer").clear();
                currentArray = Util.generateArray(amountOfNumbers);
                RootPanel.get("sortPageContainer").add(createTable(currentArray));
                toSortByDescending = true;
            } else {
                Window.alert("Please select a value smaller or equal to 30");
            }
        });
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
}