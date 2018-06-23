package calculator;

import javax.swing.JFrame;

public class Calculator {

    public static void main(String[] args) {
        GUICalculator calculator = new GUICalculator("Calculator");
        
        calculator.setSize(500,250);
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        calculator.setLocationRelativeTo(null);
        calculator.setVisible(true);
    }
}
