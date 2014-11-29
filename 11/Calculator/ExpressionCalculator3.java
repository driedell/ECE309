import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


public class ExpressionCalculator3 {

	public ExpressionCalculator3() {
		//build gui here
		
		
	}
	public static void main(String[] args) {
		
	Stack operatorStack = new Stack();	
	Queue<Object> output = new LinkedList<Object>();
	
	Stack numberStack = new Stack();
	
	
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
	int x=5; //negative numbers for x do not work
	char leftParen = '(';
	char rightParen = ')';
	
	String expression = "(1^5/5)(1+2)"; //tested with "9+24/(7-3)" // the only case it fails is when there is not a ')' at the end of the expression: to fix this just add in parentheses around the whole expression using substring after whitespace is removed
								   //precedence works
	// remove whitespace
	expression=expression.replaceAll("\\s+","");
	System.out.println("Entered expression is: " + expression);
	
	// replacing x with a number
	for(i=0; i<expression.length();i++){
		if(expression.charAt(i)=='x'){
	
			expression = expression.substring(0,i)+( x) + expression.substring(i+1, expression.length());
			
		}
	}
	// replacing e with a number
	for(i=0; i<expression.length();i++){
		if(expression.charAt(i)=='e'){
		
			expression = expression.substring(0,i)+(Math.E) + expression.substring(i+1, expression.length());
				
		}
		
	}
	
	// replacing pi with a number
	for(i=0; i<expression.length();i++){
		if(expression.charAt(i)=='p' && expression.charAt(i+1)=='i'){ 
		
			expression = expression.substring(0,i)+((double) (Math.PI)) + expression.substring(i+2, expression.length());
				
		}
	}
	
	// checking if ')(' case exists without an operator in between, if it does exist, throw error
	for(i=0; i<(expression.length()-1);i++){
		if(expression.charAt(i)==')' && expression.charAt(i+1)=='('){
			//throw new IllegalArgumentException;
			System.out.println("Enter an operator in between a left and right parentheses");
			System.exit(0);
		}
		
	}
		
	System.out.println("Entered expression is: " + expression + " with x, pi, or e substituted in if applicable");
	
	//adding in parentheses to enclose the entire expression just in case
	expression = leftParen + expression.substring(0, expression.length()) + rightParen;
	
	//read token
	for(i=0; i<expression.length();i++){ //breakpoint here for good testing
		
		//number goes to queue
		if(expression.charAt(i)!='+' &&  expression.charAt(i)!='-' && expression.charAt(i)!='*' && expression.charAt(i)!='/' && expression.charAt(i)!='^' && expression.charAt(i)!='r' && expression.charAt(i)!='(' && expression.charAt(i)!=')'){
			startI = i;
			j=i;
			while(expression.charAt(j+1)!='+' && expression.charAt(j+1)!='-' && expression.charAt(j+1)!='*' && expression.charAt(j+1)!='/' && expression.charAt(j+1)!='r' && expression.charAt(j+1)!='^' && expression.charAt(j+1)!=')'){ //added ')' case
				j++;
				i++;
			}
			number = expression.substring(startI,j+1);
			
			q = Double.parseDouble(number);
			
			output.add(new Double(q));
		}
		
		// operator processing
		if(expression.charAt(i)=='+' || expression.charAt(i)=='-' || expression.charAt(i)=='*' || expression.charAt(i)=='/' || expression.charAt(i)=='r' ||  expression.charAt(i)=='^'){
			
			//if the operatorStack is empty then push the first operator on the stack
			if(operatorStack.isEmpty()){
				operatorStack.push(expression.charAt(i));
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
			if(expression.charAt(i)=='+'){
				newOperator = add;
			}
			if(expression.charAt(i)=='-'){
				newOperator = subtract;
			}
			if(expression.charAt(i)=='*'){
				newOperator = multiply;
			}
			if(expression.charAt(i)=='/'){
				newOperator = divide;
			}
			if(expression.charAt(i)=='^'){
				newOperator = carat;
			}
			if(expression.charAt(i)=='r'){
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
			operatorStack.push(expression.charAt(i));
		}
		
		// if token is a "("
		if(expression.charAt(i)=='('){
			operatorStack.push(expression.charAt(i));	
		}
		
		//if token is a ")"
		if(expression.charAt(i)==')'){
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
	
	// print final answer
	System.out.println("Answer: " + numberStack);	
	}

}