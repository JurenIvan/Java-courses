package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Class that contains main method which reads users input through keyboard,
 * accepts only positive whole numbers in predefined range (3<=x<=20),
 * calculates their factorial and prints it. Application ends when user inputs
 * "kraj".
 * 
 * Args are not used for input.
 *
 * @author juren
 *
 */
public class Factorial {

	/**
	 * Constant that defines minimum value of range for which application can
	 * calculate factorial.
	 */
	private static final int MIN_RANGE_VALUE = 3;

	/**
	 * Constant that defines maximum value of range for which application can
	 * calculate factorial.
	 */
	private static final int MAX_RANGE_VALUE = 20;

	/**
	 * Constant that defines minimum value for which we can calculate factorial due
	 * to mathematical definition.
	 */
	private static final int MIN_FACTORIAL_VALUE = 0;

	/**
	 * Constant that defines maximum value for which we can calculate factorial due
	 * to limitation of long type.
	 */
	private static final int MAX_FACTORIAL_VALUE = 20;

	/**
	 * String sequence used to determine end of user input.
	 */
	private static final String END_TOKEN = "kraj";

	/**
	 * Method that starts the program.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String inputToken;

		System.out.print("Unesite broj > ");
		while (sc.hasNext()) {
			inputToken = sc.next();

			try {
				int value = Integer.parseInt(inputToken);

				if (value >= MIN_RANGE_VALUE && value <= MAX_RANGE_VALUE) {
					try {
						System.out.println(value + " != " + getFactorial(value));
					} catch (IllegalArgumentException e) {
						System.out.println("faktorijela od '" + value + "' se ne može pohraniti u long.");
					}

				} else {
					System.out.println("'" + value + "' nije broj u dozvoljenom rasponu.");
				}
			} catch (NumberFormatException e) {
				if (inputToken.equals(END_TOKEN)) {
					System.out.println("Doviđenja.");
					break;
				}
				System.out.println("'" + inputToken + "' nije cijeli broj.");
			}
			System.out.print("Unesite broj > ");
		}
		sc.close();
	}

	/**
	 * calculates factorial of given parameter.
	 * 
	 * @param value value of which we want to calculate factorial
	 * @return factorial of value as long type
	 * @throws IllegalArgumentException if value is negative or it exceeds maximum
	 *                                  value (20) whose factorial can be stored
	 *                                  into long
	 */
	public static long getFactorial(int value) {
		long factorial = 1;

		if (value < MIN_FACTORIAL_VALUE || value > MAX_FACTORIAL_VALUE) {
			throw new IllegalArgumentException();
		}

		while (value > MIN_FACTORIAL_VALUE) {
			factorial = factorial * value--;
		}
		return factorial;
	}
}
