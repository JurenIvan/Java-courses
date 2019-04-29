package hr.fer.zemris.java.hw02.demo;

import hr.fer.zemris.java.hw02.ComplexNumber;

/**
 * Class containing main method whose primary goal is to demonstrate operations
 * using complex numbers.
 * 
 * @author juren
 *
 */
public class ComplexDemo {
	/**
	 * Main method used to start the program
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		ComplexNumber c1 = new ComplexNumber(2, 3);
		ComplexNumber c2 = ComplexNumber.parse("2.5-3i");
		ComplexNumber c3 = c1.add(ComplexNumber.fromMagnitudeAndAngle(2, 1.57)).div(c2).power(3).root(2)[1];
		
		System.out.println(c3);
	}

}
