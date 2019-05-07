package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexRootedPolynomial;

public class Util {
	
	private static final String ROOT_PROMPT = "Root ";
	private static final char PROMPT_SYMBOL = '>';
	private static final String END_SEQUENCE = "done";
	private static final char IMAGINARY_SIGN = 'i';

	public static ComplexRootedPolynomial getInput() {
		int counter = 1;
		Scanner sc = new Scanner(System.in);
		List<Complex> roots = new ArrayList<Complex>();
		System.out.print(ROOT_PROMPT + counter + PROMPT_SYMBOL + " ");
		while (sc.hasNext()) {
			try {
				Complex parsed = parseComplex(sc.next());
				if (parsed == null) {
					if (roots.size() >= 2) {
						break;
					}
					System.out.println("Not enough roots to make polynome");
					continue;
				}
				roots.add(parsed);
			} catch (NumberFormatException e) {
				System.out.println("Illegal number format.");
				System.out.println(e.getMessage());
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
		if(line.endsWith(IMAGINARY_SIGN+"")) {
			line=line+"1";
		}
		String[] splited = line.split(IMAGINARY_SIGN + "");

		if (splited.length == 0) {
			throw new NumberFormatException("Illegal expression");
		} else if (splited.length == 1) {
			return new Complex(Double.parseDouble(splited[0]), 0);
		} else if (splited.length == 2) {
			if (splited[0].isBlank()) {
				return new Complex(0, Double.parseDouble(splited[1]));
			}
			if (splited[0].charAt(splited[0].length() - 1) == '+') {
				if(splited[0].substring(0, splited[0].length() - 1).isBlank()) 
					return new Complex(0,Double.parseDouble(splited[1]));
				return new Complex(Double.parseDouble(splited[0].substring(0, splited[0].length() - 1)),
						Double.parseDouble(splited[1]));
			}
			if (splited[0].charAt(splited[0].length() - 1) == '-') {
				if(splited[0].substring(0, splited[0].length() - 1).isBlank()) 
					return new Complex(0,(-1)*Double.parseDouble(splited[1]));
				return new Complex(Double.parseDouble(splited[0].substring(0, splited[0].length() - 1)),
						(-1) * Double.parseDouble(splited[1]));
			}
		}
		throw new NumberFormatException();
	}
}
