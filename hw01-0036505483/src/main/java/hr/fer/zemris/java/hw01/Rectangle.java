/**
 * 
 */
package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Class that contains main method which reads users input through keyboard,
 * accepts only positive numbers which represent width and height of rectangle
 * and prints area and perimeter of rectangle. In case of invalid input, gives
 * user a message feedback.
 * 
 * Args can be used as input. Parameters should be given as a two numbers
 * separated by space. First argument describes width and the second one height
 * of a rectangle.
 * 
 * @author juren
 *
 */
public class Rectangle {

	/**
	 * Method that starts the program.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		double width, height;

		if (args.length != 0 && args.length != 2) {
			System.out.println("Potrebno je unijeti 2 argumenta");
			return;
		}

		if (args.length == 2) {
			try {
				width = Double.parseDouble(args[0]);
				height = Double.parseDouble(args[1]);

				if (width < 0 || height < 0) {
					throw new NumberFormatException();
				}
				outputMessage(width, height);
				return;

			} catch (NumberFormatException e) {
				System.out.println("Uneseni argumenti moraju biti pozitivni brojevi");
				return;
			}
		}

		Scanner sc = new Scanner(System.in);
		width = inputParameter("sirinu", sc);
		height = inputParameter("visinu", sc);

		outputMessage(width, height);

		sc.close();
	}

	/**
	 * Handles users input, insures proper input form (loops until input is properly
	 * formated (awaits for positive double)). If input string isn't properly
	 * formated prints feedback.
	 * 
	 * @param nameofParameter String that is part of feedback message
	 * @param sc              Scanner instance that allows user input through
	 *                        keyboard
	 * @return positive double
	 */
	public  static double inputParameter(String nameofParameter, Scanner sc) {
		String inputToken;
		double value;

		System.out.print("Unesite " + nameofParameter + " > ");
		while (sc.hasNext()) {
			inputToken = sc.next();
			try {
				value = Double.parseDouble(inputToken);
				if (value >= 0) {
					return value;
				}
				System.out.println("Unijeli ste negativnu vrijednost.");
			} catch (NumberFormatException e) {
				System.out.println("'" + inputToken + "' se ne može protumačiti kao broj." + "");
			}
			System.out.print("Unesite " + nameofParameter + " > ");
		}
		return 0;
	}

	/**
	 * Prints message with width,height,perimeter and area of given rectangle.
	 * 
	 * @param width  must be positive number
	 * @param height must be positive number
	 */
	public static void outputMessage(double width, double height) {
		try {
			System.out.println("Pravokutnik širine " + width + " i visine " + height + " ima površinu "
					+ calcRectangleArea(width, height) + " te opseg " + calcRectanglePerimeter(width, height));
		} catch (IllegalArgumentException e) {
			System.out.println("Opseg i Povrsina nisu definirani za negativne brojeve");
		}
	}

	/**
	 * Calculates perimeter of rectangle with given parameters.
	 * 
	 * @param width  must be positive number
	 * @param height must be positive number
	 * @return value of perimeter as double
	 * @throws IllegalArgumentException if any of arguments is negative
	 */
	public static double calcRectanglePerimeter(double width, double height) {
		if (width < 0 || height < 0)
			throw new IllegalArgumentException();
		return 2 * (width + height);
	}

	/**
	 * Calculates area of rectangle with given parameters.
	 * 
	 * @param width  must be positive number
	 * @param height must be positive number
	 * @return value of area as double
	 * @throws IllegalArgumentException if any of arguments is negative
	 */
	public static double calcRectangleArea(double width, double height) {
		if (width < 0 || height < 0)
			throw new IllegalArgumentException();
		return width * height;
	}

}
