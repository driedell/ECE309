
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ExpressionCalculator implements Calculator, ActionListener, FocusListener {

	@Override
	public String accumulate(String amount) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

		double x;

		JFrame window = new JFrame("Expression Calculator");  //need window
		JButton     clearButton     = new JButton("CLEAR");      
		JButton     evaluateButton     = new JButton("EVALUATE");      
		JLabel      amountLabel     = new JLabel("Enter expression",SwingConstants.LEFT);
		JLabel   	xLabel        = new JLabel("Enter variable x",SwingConstants.RIGHT);
		JRadioButton accumulatorRadioButton= new JRadioButton("Accumulator Mode",  true);
		JRadioButton expressionRadioButton = new JRadioButton("Expression Mode",   false);
		JTextField  amountTextField = new JTextField(8);   //test field for entering amount
		JTextField  errorTextField  = new JTextField(32);
		JTextField  xTextField = new JTextField(2);
		JTextArea logTextArea = new JTextArea(20,40); 
		JScrollPane logScrollPane = new JScrollPane(logTextArea);
		JPanel panel = new JPanel();
		ButtonGroup radioButtonGroup = new ButtonGroup();
		
		String newLine = System.lineSeparator(); 
		
		public ExpressionCalculator() {
			//build gui here
			panel.setLayout(new GridLayout(1,5));
			panel.add(clearButton);
			panel.add(evaluateButton);
			panel.add(amountLabel);
			panel.add(amountTextField);

			panel.add(xLabel);
			//panel.add(amountTextField);
			//panel.add(xTextField);
			//panel.add(amountTextField);
			panel.add(xTextField);
			//JPanel bottomPanel = new JPanel();
		    panel.add(accumulatorRadioButton);
		    panel.add(expressionRadioButton);
		    //bottomPanel.add(errorTextField);
		    //window.getContentPane().add(bottomPanel, "South");
			window.getContentPane().add(panel, "North");
			window.getContentPane().add(logScrollPane, "South");
			window.setSize(900, 500);
			window.setLocation(300,100);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setVisible(true);
		    window.getContentPane().add(errorTextField,"South");
		    xTextField.setText("0"); //must set text value in order for calc to work correctly

			logTextArea.setEditable(false);
			logTextArea.setFont(new Font("Times Roman", Font.BOLD, 12));
			amountTextField.setFont(new Font("Times Roman", Font.BOLD, 12));

			//give address to gui
			
			clearButton.addActionListener(this);
			evaluateButton.addActionListener(this);

			amountTextField.addActionListener(this);
			xTextField.addActionListener(this);
			amountTextField.requestFocus(); //set cursor in
		    xTextField.addFocusListener(this);
		    accumulatorRadioButton.addActionListener(this);
		    expressionRadioButton.addActionListener(this);
		    
		    radioButtonGroup.add(accumulatorRadioButton);
		    radioButtonGroup.add(expressionRadioButton);
		}
		
		public void clear()	{
			amountTextField.setText("");
			xTextField.setText("0");
		}
		
		//int x;
		
		
		public String calculate(String Expression, String x) throws IllegalArgumentException {
			
			Stack operatorStack = new Stack();	
			Queue<Object> output = new LinkedList<Object>();
			
			Stack numberStack = new Stack();
			this.x = Double.parseDouble(x);

			
			Object[] rpnArray;
			char operatorSymbol;
			
			//assigning precedence values. 10 is highest, 1 is lowest
			char carat = 10;
			char root = 10;
			char multiply = 8;
			char divide = 8;
			char add = 5;
			char subtract = 5;
			
			char paren = 0;
			
			double secondValue;
			double firstValue;
			double totalValue=0;
			
			double q;
			int startI;
			String number;
			int j=0;
			
			int topOfStackOperator = 0;
			int newOperator=0;
			int i;
			// implement x=number in gui
			//int x=5; //negative numbers for x do not work
			char leftParen = '(';
			char rightParen = ')';
			
			//String expression = "(1^5/5)(1+2)"; //tested with "9+24/(7-3)" // the only case it fails is when there is not a ')' at the end of the expression: to fix this just add in parentheses around the whole expression using substring after whitespace is removed
										   //precedence works

			// remove whitespace
			
			for(i = 0; i< Expression.length(); i++) {
				if(Expression.startsWith(" ")) {
					Expression = Expression.substring(1,Expression.length());
				}
				if(Expression.endsWith(" ")) {
					Expression = Expression.substring(0,Expression.length()-1);
				}
				
				while(Expression.charAt(i) == ' ' && Expression.charAt(i+1) == ' ') {
					Expression = Expression.substring(0,i) + Expression.substring(i+1,Expression.length());
				}
//				if(Expression.charAt(i) == ' ' && Expression)
				if(Expression.charAt(i) == ' ' &&
					  (Expression.charAt(i+1) == '(' || Expression.charAt(i+1) == ')' || Expression.charAt(i+1) == 'r' || 
					   Expression.charAt(i+1) == '^' || Expression.charAt(i+1) == '*' || Expression.charAt(i+1) == '/' ||
					   Expression.charAt(i+1) == '+' || Expression.charAt(i+1) == '-')) {
					Expression = Expression.substring(0,i) + Expression.substring(i+1,Expression.length());
				}
				if(Expression.charAt(i) == ' ' &&
						(Expression.charAt(i-1) == '(' || Expression.charAt(i-1) == ')' || Expression.charAt(i-1) == 'r' || 
						Expression.charAt(i-1) == '^' || Expression.charAt(i-1) == '*' || Expression.charAt(i-1) == '/' ||
						Expression.charAt(i-1) == '+' || Expression.charAt(i-1) == '-')){
					Expression = Expression.substring(0,i-1) + Expression.substring(i,Expression.length());

				}
				
//				if(Expression.charAt(i) == ' ' && Expression.charAt(i-1) >= '0' && Expression.charAt(i-1) <= '9' &&
//						Expression.charAt(i+1) >= '0' && Expression.charAt(i+1) <= '9') {
////Throw exception cause you have "number number"
//					
//					System.out.println("Error: number space number");
//				}
			}			
			
//			Expression=Expression.replaceAll("\\s+","");
			System.out.println("Entered expression is: " + Expression);
			
			// replacing x with a number
			for(i=0; i<Expression.length();i++){
				if(Expression.charAt(i)=='x'){
			
					Expression = Expression.substring(0,i) + "(0" + x + ")" + Expression.substring(i+1, Expression.length());
					
				}
			}
			// replacing e with a number
			for(i=0; i<Expression.length();i++){
				if(Expression.charAt(i)=='e'){
				
					Expression = Expression.substring(0,i)+(Math.E) + Expression.substring(i+1, Expression.length());
						
				}
				
			}
			
			// replacing pi with a number
			for(i=0; i<Expression.length();i++){
				if(Expression.charAt(i)=='p' && Expression.charAt(i+1)=='i'){ 
				
					Expression = Expression.substring(0,i)+((double) (Math.PI)) + Expression.substring(i+2, Expression.length());
						
				}
			}
			
			// checking if ')(' case exists without an operator in between, if it does exist, throw error
			for(i=0; i<(Expression.length()-1);i++){
				if(Expression.charAt(i)==')' && Expression.charAt(i+1)=='('){
					//throw new IllegalArgumentException;
					System.out.println("Enter an operator in between a left and right parentheses");
					System.exit(0);
				}
				
			}
				
			System.out.println("Entered expression is: " + Expression + " with x, pi, or e substituted in if applicable");
			
			//adding in parentheses to enclose the entire expression just in case
			Expression = leftParen + Expression.substring(0, Expression.length()) + rightParen;
			
			//read token
			for(i=0; i<Expression.length();i++){ //breakpoint here for good testing
				
				//number goes to queue
				if(Expression.charAt(i)!='+' &&  Expression.charAt(i)!='-' && Expression.charAt(i)!='*' && Expression.charAt(i)!='/' && Expression.charAt(i)!='^' && Expression.charAt(i)!='r' && Expression.charAt(i)!='(' && Expression.charAt(i)!=')'){
					startI = i;
					j=i;
					while(Expression.charAt(j+1)!='+' && Expression.charAt(j+1)!='-' && Expression.charAt(j+1)!='*' && Expression.charAt(j+1)!='/' && Expression.charAt(j+1)!='r' && Expression.charAt(j+1)!='^' && Expression.charAt(j+1)!=')'){ //added ')' case
						j++;
						i++;
					}
					number = Expression.substring(startI,j+1);
					
					q = Double.parseDouble(number);
					
					output.add(new Double(q));
				}
				
				// operator processing
				if(Expression.charAt(i)=='+' || Expression.charAt(i)=='-' || Expression.charAt(i)=='*' || Expression.charAt(i)=='/' || Expression.charAt(i)=='r' ||  Expression.charAt(i)=='^'){
					
					//if the operatorStack is empty then push the first operator on the stack
					if(operatorStack.isEmpty()){
						operatorStack.push(Expression.charAt(i));
						continue;
					}
					
					// setting precedence value for operator at top of operatorStack
					if(operatorStack.peek().toString().equals("(")){
						topOfStackOperator = paren;
					}
					if(operatorStack.peek().toString().equals("+")){
						topOfStackOperator = add;
					}
					if(operatorStack.peek().toString().equals("-")){
						topOfStackOperator = subtract;
					}
					if(operatorStack.peek().toString().equals("*")){
						topOfStackOperator = multiply;
					}
					if(operatorStack.peek().toString().equals("/")){
						topOfStackOperator = divide;
					}
					if(operatorStack.peek().toString().equals("^")){
						topOfStackOperator = carat;
					}
					if(operatorStack.peek().toString().equals("r")){
						topOfStackOperator = root;
					}
					
					// setting precedence value for the operator that was just read in
					if(Expression.charAt(i)=='+'){
						newOperator = add;
					}
					if(Expression.charAt(i)=='-'){
						newOperator = subtract;
					}
					if(Expression.charAt(i)=='*'){
						newOperator = multiply;
					}
					if(Expression.charAt(i)=='/'){
						newOperator = divide;
					}
					if(Expression.charAt(i)=='^'){
						newOperator = carat;
					}
					if(Expression.charAt(i)=='r'){
						newOperator = root;
					}
					
					// while there is an operator on top of the stack with greater precedence
					while(topOfStackOperator>=newOperator){
						//output=(Queue<Object>) operatorStack.pop(); //this might not work
						output.add(operatorStack.pop());
						
						//updating topOfStackOperator
						if(operatorStack.peek().toString().equals("(")){
							topOfStackOperator = paren;
						}
						if(operatorStack.peek().toString().equals("+")){
							topOfStackOperator = add;
						}
						if(operatorStack.peek().toString().equals("-")){
							topOfStackOperator = subtract;
						}
						if(operatorStack.peek().toString().equals("*")){
							topOfStackOperator = multiply;
						}
						if(operatorStack.peek().toString().equals("/")){
							topOfStackOperator = divide;
						}
						if(operatorStack.peek().toString().equals("^")){
							topOfStackOperator = carat;
						}
						if(operatorStack.peek().toString().equals("r")){
							topOfStackOperator = root;
						}
						
					}
					
					//push current operator onto the stack
					operatorStack.push(Expression.charAt(i));
				}
				
				// if token is a "("
				if(Expression.charAt(i)=='('){
					operatorStack.push(Expression.charAt(i));	
				}
				
				//if token is a ")"
				if(Expression.charAt(i)==')'){
					while(!(operatorStack.peek().toString().equals("("))){  //breakpoint here for good testing
						output.add(operatorStack.pop());
					}
					operatorStack.pop();		
				}
				
			}
			while(!operatorStack.isEmpty()){
				output.add(operatorStack.pop());
			}
			// printing the entered expression in postfix notation (this takes care of the order of operations)
			System.out.println("Entered expression in posfix notation: " + output);
			/////////////////////////////////////////////////////////////////////////////
			//now to read the reverse polish notation and calculate the solution
			
			//converting from queue to string
			rpnArray = output.toArray();
			
			for(i=0; i<rpnArray.length; i++){
				//if it's a number push to numberStack
				if(!(rpnArray[i].equals('+')) &&  !(rpnArray[i].equals('-')) && !(rpnArray[i].equals('*')) && !(rpnArray[i].equals('/')) && !(rpnArray[i].equals('^')) && !(rpnArray[i].equals('r'))){
					numberStack.push(rpnArray[i]);
				}
				
				//if it's an operator
				if(rpnArray[i].equals('+') || rpnArray[i].equals('-') || rpnArray[i].equals('*') || rpnArray[i].equals('/') || rpnArray[i].equals('r') ||  rpnArray[i].equals('^')){
					operatorSymbol = (char) rpnArray[i];
					
					secondValue = (double) numberStack.pop();
					firstValue = (double) numberStack.pop();
					
					if(operatorSymbol=='+'){
						totalValue = firstValue + secondValue;
					}
					if(operatorSymbol=='-'){
						totalValue = firstValue - secondValue;
					}
					if(operatorSymbol=='*'){
						totalValue = firstValue*secondValue;
					}
					if(operatorSymbol=='/'){
						totalValue = firstValue/secondValue;
					}
					if(operatorSymbol=='r'){    //fix this later
						totalValue = Math.pow(firstValue, (1/secondValue));
					}
					if(operatorSymbol=='^'){
						totalValue = Math.pow(firstValue,secondValue); //check to see if this is right
					}
					
					numberStack.push(totalValue);
					}	
				}
			return numberStack.peek().toString();
			// print final answer
			//System.out.println("Answer: " + numberStack);	
			}

		
		public static void main(String[] args) {
		
			new ExpressionCalculator();
			}
		
		// print final answer
		//System.out.println(numberStack);	
		//}

		
		
		//String enteredExpression;
		//String newTotal;
		
		public void actionPerformed(ActionEvent ae) {
			// TODO Auto-generated method stub
			if (ae.getSource() == clearButton)
			   {
			   // do clear function
			   clear();	
		       // show CLEAR in the log
		      // logTextArea.append(newLine + "CLEAR");
		       amountTextField.requestFocus(); // set cursor in.
			   return;
			   }
			
			if(ae.getSource() == evaluateButton){
				String enteredExpression = amountTextField.getText();
				String  xText = xTextField.getText();
			String newTotal = calculate(enteredExpression, xText);			
			    logTextArea.append(newLine + " " + enteredExpression + " = " + newTotal);
			    System.out.println(newTotal);
			}
			
			if (ae.getSource() == amountTextField)
			   {
				String enteredExpression = amountTextField.getText();
				String xText = xTextField.getText();
				System.out.println(xText);
				String newTotal = calculate(enteredExpression, xText);			
			    logTextArea.append(newLine + " " + enteredExpression + " = " + newTotal);
			    System.out.println(newTotal);
			   return;
			   }
			
			if(ae.getSource() == xTextField){
				
			}
			
			
		}

		@Override
		public void focusGained(FocusEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == xTextField)
			{
							
				String  xText = xTextField.getText();
				this.x = Double.parseDouble(xText);
				
				System.out.println(x);
				return;
				
			}
		}

	

	}