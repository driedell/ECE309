import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;


public class ExpressionCalculator2 {

	public ExpressionCalculator2() {

	}
	public static void main(String[] args) {
		int i;
		double q;
		char temp;
		char paren = '(';

		double secondValue=0;
		double firstValue=0;
		double totalValue=0;
		String operatorSymbol;

		Object tempStack;
		String expression = "2*(3*5+2)";

		// remove whitespace
		expression=expression.replaceAll("\\s+","");
		System.out.println(expression);

		Stack numberStack = new Stack();
		Stack operatorStack = new Stack();
		Stack noParenStack = new Stack();
		//Deque<String> numberStack = new ArrayDeque<String>();

		//Deque<String> operatorStack = new ArrayDeque<String>();

		for(i=0; i<expression.length();i++){
			if(expression.charAt(i)!='+' &&  expression.charAt(i)!='-' && expression.charAt(i)!='*' && expression.charAt(i)!='/' && expression.charAt(i)!='^' && expression.charAt(i)!='r' && expression.charAt(i)!='(' && expression.charAt(i)!=')'){
				//it is a number and gets pushed onto number stack

				temp = expression.charAt(i);
				q = Double.parseDouble(Character.toString(temp));

				numberStack.push(q);
				
				System.out.println("numbers: " + numberStack);

			}
			else{
				//it is an operator and gets pushed onto operator stack
				if(expression.charAt(i)=='*' ){
					operatorStack.push('*');
				}
				if(expression.charAt(i)=='+' ){
					operatorStack.push('+');
				}
				if(expression.charAt(i)=='-' ){
					operatorStack.push('-');
				}
				if(expression.charAt(i)=='/' ){
					operatorStack.push('/');
				}
				if(expression.charAt(i)=='^' ){
					operatorStack.push('^');
				}
				if(expression.charAt(i)=='r' ){
					operatorStack.push('r');
				}
				if(expression.charAt(i)=='(' ){
					operatorStack.push('(');
				}
				
				System.out.println("operators: " + operatorStack);
				
				if(expression.charAt(i)==')' ){
					for(i=operatorStack.size(); i>0; i--){
						//System.out.println(operatorStack.peek().toString());
						if(!(operatorStack.peek().toString().equals("("))){
							tempStack = operatorStack.pop();

							numberStack.push(tempStack);
							
							System.out.println("numbers: " + numberStack);
							System.out.println("operators: " + operatorStack);

							if(operatorStack.isEmpty()){
								break;
							}
						}
						else{
							//remove left and right parentheses from both stacks
							operatorStack.pop();
							for(i=0; i<expression.length(); i++){
								if(expression.charAt(i)==')'){
									expression = deleteCharAt(expression,i);
									break;
								}
							}

						}

					}
					noParenStack = numberStack;



					operatorStack.clear();

					while((noParenStack.peek().toString().equals("+")) || (noParenStack.peek().toString().equals("-")) || (noParenStack.peek().toString().equals("/")) || (noParenStack.peek().toString().equals("*")) || (noParenStack.peek().toString().equals("r")) || (noParenStack.peek().toString().equals("^"))){
						//operatorStack = (Stack) noParenStack.pop();
						operatorStack.push(noParenStack.peek());
						noParenStack.pop();
					}
					//operatorStack now has operators
					// noParenStack now has numbers
					while(!operatorStack.isEmpty()){
						secondValue = (double) noParenStack.pop();
						firstValue = (double) noParenStack.pop();

						operatorSymbol = operatorStack.peek().toString();
						operatorStack.pop();

						if(operatorSymbol.equals("+")){
							totalValue = firstValue + secondValue;
						}
						if(operatorSymbol.equals("-")){
							totalValue = firstValue - secondValue;
						}
						if(operatorSymbol.equals("*")){
							totalValue = firstValue*secondValue;
						}
						if(operatorSymbol.equals("/")){
							totalValue = firstValue/secondValue;
						}
						if(operatorSymbol.equals("r")){    //fix this later
							//totalValue = Math.sqrt(firstValue, secondValue);
						}
						if(operatorSymbol.equals("^")){
							totalValue = Math.pow(secondValue,firstValue); //check to see if this is right
						}

						noParenStack.push(totalValue);
					}
					System.out.println(noParenStack);


				}
			}

		}

	}


	private static String deleteCharAt(String expression, int i) {

		return expression.substring(0,  i) + expression.substring(i+1);
	}

}