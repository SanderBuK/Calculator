package calculator;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GUICalculator extends JFrame {

    private ArrayList<Double> numbers; // <- Holds the two current numbers that are being used for calculation
    private StringBuilder currentNum; // <- Holds the current number being displayed on the calculator
    private Double total; // <- Holds the total value of the number being calculated

    private JPanel mainCalc; // <- Holds all the panels that make out the calculator part but not the history area

    private JButton[] butNumbers; // <- Array for all of the number buttons
    private JPanel buttons; // <- Holds all the JButtons with numbers together with the comman and equals button
    private ButtonHandlerClass[] butHandlers; // <- Array with all the handlers for the number buttons
    private JButton butComma;
    private JButton but0;
    private JButton butNegative;

    private JPanel functions; // <- Panel for holding the operatorPanel and the functionsPanel

    private JPanel functionsPanel; // <- Panel for holding the cAndSquarePanel and the equals button
    private JPanel cAndSquarePanel; // <- Panel for holding butC and butSquare
    private JButton butC;
    private JButton butSquare;
    private JButton butEqual;

    private JPanel operatorPanel; // <- The panel that holds all the operators like; plus, minus etc.
    private JButton[] operators; // <- Array for all the operator buttons
    private JButton butPlus;
    private JButton butMinus;
    private JButton butTimes;
    private JButton butDivide;
    private Queue<Character> operatorQ = new LinkedList<>(); // <- Queue that gives out the operator that was
    //first entered in, because you want the first chosen operator to handle the first two entered numbers
    private OperationHandlerClass[] operatorHandlers; // <- Array with all handlers for the operator buttons

    private JTextField result; // <- Text field to show the number you are currently entering
    private JButton butDelete; // <- Button for deleting everything you have typed in
    private JPanel resultDisplay; // Panel to hold the receding two objects

    private TextArea history; // <- Text area to display the history of what you previously calculated
    private StringBuilder historyText; // <- Holds the text to be displayed in the history text area

    GUICalculator(String title) {

        super(title);

        total = 0.0;
        mainCalc = new JPanel(new BorderLayout());
        currentNum = new StringBuilder();
        numbers = new ArrayList();
        historyText = new StringBuilder();

        /*||The Main Numbers||*/
        //Initializing all the number buttons and the panel to hold all the buttons together with '=' and ','
        butNumbers = new JButton[10];
        but0 = new JButton("0");

        buttons = new JPanel();
        buttons.setLayout(new GridLayout(4, 3));

        butNumbers[0] = but0;
        for (int i = 1; i < 10; i++) {
            butNumbers[i] = new JButton(String.format("  %d  ", i));
            butNumbers[i].setToolTipText(String.format("%d", i));
        }

        butComma = new JButton(",");
        butNegative = new JButton("+/-");

        //Add all the buttons to the panel
        for (JButton but : butNumbers) {
            buttons.add(but);
        }

        //Assign the handlers to the buttons 1-9
        butHandlers = new ButtonHandlerClass[10];
        for (int i = 0; i < 10; i++) {
            //Add handler objects to the handlerclass array and assign each to their respective button
            butHandlers[i] = new ButtonHandlerClass(i);
            butNumbers[i].addActionListener(butHandlers[i]);
        }

        buttons.add(butComma);
        butComma.setToolTipText("Comma");
        buttons.add(but0);
        but0.setToolTipText("0");
        buttons.add(butNegative);
        butNegative.setToolTipText("Set negative");

        //Add the inner ActionListener for the comma button
        butComma.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (!currentNum.toString().contains(".")) {
                    currentNum.append('.');
                    result.setText(currentNum.toString());
                }
            }
        }
        );

        //Add the inner ActionListener for the negative button
        butNegative.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (currentNum.length() != 0) {
                    if (currentNum.charAt(0) != '-') {
                        currentNum.insert(0, '-');
                    } else {
                        currentNum.deleteCharAt(0);
                    }
                } else {
                    currentNum.append('-');
                }

                result.setText(currentNum.toString());
            }
        }
        );

        mainCalc.add(buttons, BorderLayout.CENTER);

        /*||The Functions||*/
        cAndSquarePanel = new JPanel(new GridLayout(2, 1));

        functionsPanel = new JPanel(new GridLayout(2, 1));

        functions = new JPanel(new GridLayout(1, 2));

        butC = new JButton("C");
        butC.setToolTipText("Delete everything");
        butSquare = new JButton("√");
        butSquare.setToolTipText("Squareroot");
        butEqual = new JButton("=");
        butEqual.setToolTipText("Equals");

        //Configure all the panels for the C, Square and Equal buttons
        cAndSquarePanel.add(butC);
        cAndSquarePanel.add(butSquare);
        functionsPanel.add(cAndSquarePanel);
        functionsPanel.add(butEqual);

        //Add inner ActionListener for butC
        butC.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                clearCalc();
            }
        }
        );

        //Add inner ActionListener for butSquare
        butSquare.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (currentNum.length() != 0 && currentNum.charAt(0) == '-') {
                    clearCalc();
                    result.setText("Number can't be negative");
                }
                if (currentNum.length() != 0) {
                    double square;
                    square = Math.sqrt(Double.parseDouble(currentNum.toString()));
                    currentNum.delete(0, currentNum.length());
                    currentNum.append(square);
                    result.setText(currentNum.toString());
                }
            }
        }
        );

        //Add the Operation ActionListener to the equal button
        butEqual.addActionListener(new OperationHandlerClass('='));

        operatorPanel = new JPanel(new GridLayout(4, 1));
        operators = new JButton[4];
        //Add buttons to the operators array
        operators[0] = (butPlus = new JButton("+"));
        butPlus.setToolTipText("Addition");

        operators[1] = (butMinus = new JButton("-"));
        butMinus.setToolTipText("Subtraction");

        operators[2] = (butTimes = new JButton("*"));
        butTimes.setToolTipText("Multiplication");

        operators[3] = (butDivide = new JButton("/"));
        butDivide.setToolTipText("Division");

        operatorHandlers = new OperationHandlerClass[4];
        for (int i = 0; i < 4; i++) {
            operatorHandlers[i] = new OperationHandlerClass(operators[i].getText().charAt(0));
            operators[i].addActionListener(operatorHandlers[i]);
        }

        operatorPanel.add(butPlus);
        operatorPanel.add(butMinus);
        operatorPanel.add(butTimes);
        operatorPanel.add(butDivide);

        functions.add(operatorPanel);
        functions.add(functionsPanel);

        mainCalc.add(functions, BorderLayout.EAST);

        /*||The Label and Delete button||*/
        result = new JTextField("0");
        result.setEditable(false);
        butDelete = new JButton("Del");
        butDelete.setToolTipText("Delete");

        butDelete.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (currentNum.length() != 0) {
                    currentNum.deleteCharAt(currentNum.length() - 1);
                }
                result.setText(currentNum.toString());
            }
        }
        );

        resultDisplay = new JPanel(new BorderLayout());
        resultDisplay.add(butDelete, BorderLayout.EAST);
        resultDisplay.add(result);

        mainCalc.add(resultDisplay, BorderLayout.NORTH);

        /*||History Area||*/
        history = new TextArea("Your history is displayed here:\n\n");
        history.setEditable(false);

        /*||Add all the components on to the main JFrame||*/
        super.add(mainCalc, BorderLayout.EAST);
        super.add(history);//Calculate the two numbers using the chosen operator

    }

    private void clearCalc() {
        numbers.clear();
        currentNum.delete(0, currentNum.length());
        result.setText("0");
        historyText.delete(0, historyText.length());
        operatorQ.clear();
    }

    private double calcNumber(ArrayList<Double> list, char operator) {
        switch (operator) {
            case '+':
                return list.get(0) + list.get(1);
            case '-':
                return list.get(0) - list.get(1);
            case '*':
                return list.get(0) * list.get(1);
            case '/':
                return list.get(0) / list.get(1);
            default:
                return 0.0;
        }
    }

    //Class for handling the different operations
    private class OperationHandlerClass implements ActionListener {

        private char currentOperation;

        OperationHandlerClass(char operation) {
            currentOperation = operation;
            System.out.println("Handler called " + operation);
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            if (currentNum.length() != 0) {
                operatorQ.add(currentOperation);
                numbers.add(Double.parseDouble(currentNum.toString()));
                System.out.println("numbers size " + numbers.size());
                System.out.println(currentOperation);
                if (numbers.size() > 1) {
                    System.out.println(operatorQ.peek());
                    total = calcNumber(numbers, operatorQ.poll());
                    numbers.set(0, total);
                    numbers.remove(1);
                    numbers.trimToSize();
                    result.setText(total.toString());
                }
                historyText.append((currentNum.charAt(0) == '-') ? "(" + currentNum + ") " : currentNum + " ");
                System.out.println(historyText);
                if (currentOperation != '=') {
                    historyText.append(currentOperation + " ");
                }
                System.out.println(numbers);
                currentNum.delete(0, currentNum.length());
            }
            if (currentOperation == '=') {
                //Show the calculation and reset to default if numbers isn't empty
                if (!numbers.isEmpty()) {
                    historyText.append(currentNum).append(" = ").append(total + "\n\n");
                    history.append(historyText.toString());
                    clearCalc();
                    result.setText(total.toString());
                    total = 0.0;
                }
            }
        }
    }

    //Class for handling all the number buttons
    private class ButtonHandlerClass implements ActionListener {

        private int num;

        ButtonHandlerClass(int num) {
            //Get the buttons number and assign it to num, so it can be added on to the currentNum
            this.num = num;
            System.out.println(num);
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            currentNum.append(num);
            System.out.println(currentNum);
            result.setText(currentNum.toString());
        }
    }
}
