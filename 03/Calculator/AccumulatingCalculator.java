// David Riedell, dariedel@ncsu.edu

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class AccumulatingCalculator implements ActionListener, Accumulator {

public static void main(String[] args) {
		System.out.println("Main thread enters main() from commmand line loader.");
		new AccumulatingCalculator();	// Load the rest of the program!
		System.out.println("Main thread returning to the command line loader.");

	}

	JFrame window				= new JFrame("Accumulating Calculator - David Riedell, dariedel@ncsu.edu");
	JButton clearButton			= new JButton("Clear");
	JLabel amountLabel			= new JLabel("Enter amount", SwingConstants.RIGHT);
	JLabel totalLabel			= new JLabel("Total", SwingConstants.RIGHT);
	JTextField amountTextField	= new JTextField(8);
	JTextField totalTextField	= new JTextField(8);
	JTextField errorTextField	= new JTextField(32);
	JPanel panel				= new JPanel();
	JLabel drop00Label			= new JLabel("Drop .00", SwingConstants.RIGHT);
	JCheckBox drop00			= new JCheckBox();

	JTextArea logTextArea		= new JTextArea(20,40);	//rows, characters
	JScrollPane logScrollPane	= new JScrollPane(logTextArea);

	double total;

public AccumulatingCalculator() {
		System.out.println("Main thread enters constructor method, called from the NEW program loader.");
		// TODO Build GUI

		panel.setLayout(new GridLayout(1,5));// 1 row, 5 cols

		panel.add(clearButton);
		panel.add(amountLabel);
		panel.add(amountTextField);
		panel.add(totalLabel);
		panel.add(totalTextField);
		panel.add(drop00Label);
		panel.add(drop00);

		window.getContentPane().add(panel, "North");
		window.getContentPane().add(errorTextField, "South");
		window.getContentPane().add(logScrollPane, "Center");
		window.setSize(800, 600);
		window.setLocation(300, 200);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

		totalTextField.setEditable(false);	// don't let cursor in
		errorTextField.setEditable(false);	// don't let cursor in
		logTextArea.setEditable(false);	// don't let cursor in
		logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
		
		clearButton.setFont(new Font("Times Roman", Font.BOLD, 15));
		amountLabel.setFont(new Font("Times Roman", Font.BOLD, 15));
		totalLabel.setFont(new Font("Times Roman", Font.BOLD, 15));
		amountTextField.setFont(new Font("Times Roman", Font.BOLD, 15));
		totalTextField.setFont(new Font("Times Roman", Font.BOLD, 15));
		errorTextField.setFont(new Font("Times Roman", Font.BOLD, 15));
		drop00Label.setFont(new Font("Times Roman", Font.BOLD, 15));
		logTextArea.setFont(new Font("Times Roman", Font.BOLD, 20));

		
		clearButton.addActionListener(this);//give button our address
		amountTextField.addActionListener(this);// give text field our address

		amountTextField.requestFocus();	// set cursor here

		System.out.println("After building GUI, main thread returning from constructor to NEW program loader.");
	}

	@Override
public String accumulate(String enteredAmount)	throws IllegalArgumentException {

		enteredAmount = enteredAmount.trim();
		
		if(enteredAmount.startsWith("+ ") || enteredAmount.startsWith("- ")) {
			enteredAmount = enteredAmount.substring(0, 1) + enteredAmount.substring(1).trim();
		}
		
		if(enteredAmount.startsWith("0")) {
			throw new IllegalArgumentException(CalculatorConstants.LEADING_ZERO);
		}

		if((enteredAmount.startsWith("/")) || (enteredAmount.startsWith("x")) || (enteredAmount.startsWith("*"))) {
			// reject user input with an error message
			throw new IllegalArgumentException(CalculatorConstants.ONLY_ADD);
		}
		if((enteredAmount == null) || (enteredAmount.length() == 0)) {
			throw new IllegalArgumentException(CalculatorConstants.MISSING_AMOUNT);
		}
		
		if(enteredAmount.contains(".")) {
			int periodOffset = enteredAmount.indexOf(".");
			String decimalPortion = enteredAmount.substring(periodOffset + 1);
			if(decimalPortion.length() != 2) {
				throw new IllegalArgumentException(CalculatorConstants.TWO_DECIMALS);
			}
		}

		double amount;
		try {
			amount = Double.parseDouble(enteredAmount);
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException(CalculatorConstants.NOT_NUMERIC);
		}
		
		total = total + amount;
		String newTotal = String.valueOf(total);
		
		if(newTotal.contains(".")) {
			int periodOffset = newTotal.indexOf(".");
			String decimalPortion = newTotal.substring(periodOffset+1);
			if(decimalPortion.length() == 0) newTotal += "00";
			if(decimalPortion.length() == 1) newTotal += "0";
		}
		errorTextField.setBackground(Color.white);
		errorTextField.setForeground(Color.black);
		errorTextField.setText("");


		return newTotal;
	}

	@Override
public void clear() {
		// TODO Auto-generated method stub
		amountTextField.setText("");//set to blank on GUI
		totalTextField.setText(""); //blank (better than "0")
		total = 0; // reset accumulator sum in memory

		String newLine = System.lineSeparator();
		logTextArea.append("Clear Used" + newLine + newLine);
		
		errorTextField.setBackground(Color.white);
		errorTextField.setForeground(Color.black);
		errorTextField.setText("");
		amountTextField.requestFocus();	// set cursor here
	}

	@Override
public void actionPerformed(ActionEvent ae) {	// buttons and text fields call here!
		// TODO Auto-generated method stub
		if(ae.getSource() == clearButton) {
			// do clear function
			clear();
			return;
		}
		if(ae.getSource() == amountTextField) {
			// add amount to total
			
			try {
				String enteredAmount	= amountTextField.getText();
				String newTotal = accumulate(enteredAmount);
				
				if(newTotal.endsWith(".00") && drop00.isSelected()) {
					newTotal = newTotal.substring(0, newTotal.length()-3);
				}
				
				String newLine = System.lineSeparator();
				String amount = totalTextField.getText();
				if(amount.length() == 0) {
					amount = "0";
				}
				logTextArea.append(enteredAmount + " + " + amount + " = " + newTotal + newLine);

				totalTextField.setText(newTotal);
				amountTextField.setText("");		// clear
			} catch(IllegalArgumentException iae) {
				errorTextField.setText(iae.toString());
				errorTextField.setText(iae.getMessage());
				errorTextField.setBackground(Color.pink);
				errorTextField.setForeground(Color.red);
			}
			return;
		}

	}



}