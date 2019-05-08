package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Class containing parser for Complex number expressions. Has method that deals
 * with user input and returns {@link ComplexRootedPolynomial}
 * 
 * @author juren
 *
 */
public class Util {

	/**
	 * Constant string used when roots are inputed
	 */
	private static final String ROOT_PROMPT = "Root ";
	/**
	 * Constant char used when roots are inputed
	 */
	private static final char PROMPT_SYMBOL = '>';
	/**
	 * Constant string used when no more roots needs to be inputed
	 */
	private static final String END_SEQUENCE = "done";
	/**
	 * Constant char used for representing imaginary sign
	 */
	private static final char IMAGINARY_SIGN = 'i';

	/**
	 * method that deals with user input thought System IO and returns
	 * {@link ComplexRootedPolynomial}. Requires at last 2 roots to be inserted. In
	 * case of illegal input.prints appropriate message
	 * 
	 * @return ComplexRootedPolynomial created out of user input
	 * @throws IllegalArgumentException if not enough roots is inserted
	 */
	public static ComplexRootedPolynomial getInput() {
		int counter = 1;
		Scanner sc = new Scanner(System.in);
		List<Complex> roots = new ArrayList<Complex>();
		System.out.print(ROOT_PROMPT + counter + PROMPT_SYMBOL + " ");
		while (sc.hasNext()) {
			try {
				Complex parsed = parseComplex(sc.nextLine());
				if (parsed == null) {
					if (roots.size() >= 2) {
						break;
					}
					System.out.println("Not enough roots to make polynomial");
					System.out.print(ROOT_PROMPT + counter + PROMPT_SYMBOL + " ");
					continue;
				}
				roots.add(parsed);
			} catch (NumberFormatException e) {
				System.out.println("Illegal number format.");
				System.out.println(e.getMessage());
				System.out.print(ROOT_PROMPT + counter + PROMPT_SYMBOL + " ");
				continue;
			}
			System.out.print(ROOT_PROMPT + ++counter + PROMPT_SYMBOL + " ");

		}
		Complex[] rootsArray = new Complex[roots.size()];

		for (int i = 0; i < roots.size(); i++) {
			rootsArray[i] = roots.get(i);
		}

		sc.close();
		if (rootsArray.length < 2)
			throw new IllegalArgumentException("Not enough roots.Please insert atleast two roots");
		return new ComplexRootedPolynomial(Complex.ONE, rootsArray);
	}

	/**
	 * Method used to parse Complex numbers out of privided String Complex numbers
	 * can be given in forms like this "2.342" "2.342+i34" "2.342-i23.32" "2.342+i"
	 * "-i" "-i3"
	 * 
	 * @param line string that should be parsed
	 * @return Complex number created out of given string
	 * @throws NumberFormatException is string is improperly formed
	 */
	private static Complex parseComplex(String line) {
		if (line.isBlank())
			throw new NumberFormatException("Empty line is not acceptable.");
		line = line.replaceAll(" ", "");
		if (line.toLowerCase().equals(END_SEQUENCE))
			return null;
		if (line.equals(IMAGINARY_SIGN + "") || line.equals("1" + IMAGINARY_SIGN))
			return new Complex(0, 1);
		if (line.equals("-" + IMAGINARY_SIGN))
			return new Complex(0, -1);
		if (line.endsWith(IMAGINARY_SIGN + "")) {
			line = line + "1";
		}
		String[] splited = line.split(IMAGINARY_SIGN + "");

		if (splited.length == 0)
			throw new NumberFormatException("Illegal expression");

		if (splited.length == 1) {
			return new Complex(Double.parseDouble(splited[0]), 0);
		}
		if (splited.length == 2) {
			if (splited[0].isBlank())
				return new Complex(0, Double.parseDouble(splited[1]));

			if (splited[0].charAt(splited[0].length() - 1) == '+') {
				if (splited[0].substring(0, splited[0].length() - 1).isBlank())
					return new Complex(0, Double.parseDouble(splited[1]));
				return new Complex(Double.parseDouble(splited[0].substring(0, splited[0].length() - 1)),
						Double.parseDouble(splited[1]));
			}

			if (splited[0].charAt(splited[0].length() - 1) == '-') {
				if (splited[0].substring(0, splited[0].length() - 1).isBlank())
					return new Complex(0, (-1) * Double.parseDouble(splited[1]));
				return new Complex(Double.parseDouble(splited[0].substring(0, splited[0].length() - 1)),
						(-1) * Double.parseDouble(splited[1]));
			}
		}
		throw new NumberFormatException();
	}
}
