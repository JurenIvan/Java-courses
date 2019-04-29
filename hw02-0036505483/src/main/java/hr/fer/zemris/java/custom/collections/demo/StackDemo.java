package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Class containing main method whose sole purpose is demonstrating what stack
 * structure can be used for. Input is ONE command line argument which
 * represents mathematical expression in postfix notation. Argument should be
 * surrounded by " " so that it's interpreted as one String. Program displays
 * result of mathematical expression.
 * 
 * @author juren
 *
 */
public class StackDemo {

	// supported operators
	private static final String ADDITION_OPERATOR = "+";
	private static final String SUBSTRACTION_OPERATOR = "-";
	private static final String MULTIPLICATION_OPERATOR = "*";
	private static final String DIVISION_OPERATOR = "/";
	private static final String MOD_OPERATOR = "%";

	/**
	 * Main method used to start program and demonstrate working stack.
	 * 
	 * @param args ONE argument resembling mathematical expression in postfix
	 *             notation.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println(
					"Lenght of args should be ONE and the args[0] should be the whole expression. Please use \" ...expression... \" ");
			return;
		}
		
		ObjectStack stack = new ObjectStack();
		String tokens[] = args[0].trim().split("\\s+");
		
		int counter=0;
		for (var token : tokens) {
			counter++;
			try {
				evaluateToken(token, stack);
			} catch (EmptyStackException e) {
				System.out.println("Irregular postfix notation! After "+counter+" characters there was nothing to take of stack! (Add a few numbers before " + token+" operator)");
				return;
			} catch (ArithmeticException e) {
				System.out.println(e.getMessage());
				return;
			} catch (NumberFormatException e) {
				System.out.print(e.getMessage());
				return;
			}
		}

		try {
			int result = (int) stack.pop();
			if (!stack.isEmpty()) {
				System.out.println("Form of expression is invalid! Stack is not empty after using all of operators! (Add more operators to expression)");
				return;
			}
			System.out.println("Expression evaluates to " + result + ".");
		} catch (EmptyStackException e) {
			System.out.println("No Idea how you got here mate! Congrats!");
			return;
		}
	}

	/**
	 * Method that takes one String token, and if it's number puts it on provided
	 * stack, otherwise if token represents predefined mathematical operand it
	 * executes the operation and saves the result to the provided stack.
	 * 
	 * 
	 * @param token String thats either number of mathematical operand
	 * @param stack Stack used for saving operations
	 * @throws EmptyStackException   thrown if there isn't enough arguments on stack
	 * @throws ArithmeticException   thrown if division by zero occurs
	 * @throws NumberFormatException thrown if token isn't one of predefined
	 *                               operators or a number
	 */
	private static void evaluateToken(String token, ObjectStack stack)
			throws EmptyStackException, ArithmeticException, NumberFormatException {
		int number1, number2;
		switch (token) {
		case ADDITION_OPERATOR:
			stack.push((int) stack.pop() + (int) stack.pop());
			return;

		case SUBSTRACTION_OPERATOR:
			stack.push((-1) * (int) stack.pop() + (int) stack.pop());
			return;

		case MULTIPLICATION_OPERATOR:
			stack.push((int) stack.pop() * (int) stack.pop());
			return;

		case DIVISION_OPERATOR:
			number1 = (int) stack.pop();
			number2 = (int) stack.pop();
			if (number1 == 0) {
				throw new ArithmeticException("It's not possible to divide by zero!");
			}
			stack.push((int) (number2 / number1));
			return;

		case MOD_OPERATOR:
			number1 = (int) stack.pop();
			number2 = (int) stack.pop();
			if (number1 == 0) {
				throw new ArithmeticException(
						"It's not possible to divide by zero and therefore remainder is unknown!");
			}
			stack.push((int) number2 % number1);
			return;

		default:
			try {
				stack.push(Integer.parseInt(token));
			} catch (NumberFormatException e) {
				throw new NumberFormatException("Unknown char in expression: " + token);
			}
		}

	}
}