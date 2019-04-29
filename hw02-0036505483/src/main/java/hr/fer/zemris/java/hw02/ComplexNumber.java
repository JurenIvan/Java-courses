package hr.fer.zemris.java.hw02;

/**
 * 
 * Class representing unmodifiable complex number. Every complex number is
 * stored as a pair of its real and imaginary part in two double values. Class
 * contains a handful of methods used to manipulate over operations with Complex
 * numbers
 * 
 * @author juren
 *
 */
public class ComplexNumber {

	/**
	 * sqrt(-1) is often represented differently across different fields of science
	 * so it's a natural thing to define it as a constant so depending on situation
	 * it can be changed fast and easy.
	 */
	private static final char IMAGINARY_SIGN = 'i';

	/**
	 * Used to negate the imprecision caused by mathematical operations and storing
	 * techniques. If difference between both real and imaginary parts of two
	 * numbers is smaller than {@link #TOLERANCE} then they are considered the same.
	 */
	private static final double TOLERANCE = 0.01;

	/**
	 * Real part of complex number
	 */
	private double real;
	
	/**
	 * Imaginary part of complex number
	 */
	private double imaginary;

	/**
	 * Constructor for ComplexNumber that creates an instance with provided real and
	 * imaginary pair of values.
	 * 
	 * @param real      part of Complex number
	 * @param imaginary part of Complex number
	 */
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	/**
	 * Static method that creates ComplexNumber with provided real part. Imaginary
	 * part of it is zero.
	 * 
	 * @param real double representing real part of ComplexNumber
	 * @return ComplexNumber with Re(number)=real , Im(number)=0
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0);
	}

	/**
	 * Static method that creates Complex number with provided imaginary part. Real
	 * part of this number is zero.
	 * 
	 * @param imaginary double representing imaginary part of ComplexNumber
	 * @return ComplexNumber with Re(number)=0 , Im(number)=imaginary
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0, imaginary);
	}

	/**
	 * Static method that creates ComplexNumber out of provided magnitude and
	 * angle(in radians) (polar notation of complex numbers)
	 * 
	 * @param magnitude double that resembles absolute value of imaginary number
	 * @param angle     double that resembles angle that vector resembling imaginary
	 *                  number closes with x axis of imaginary plane
	 * @return ComplexNumber equivalent to one provided by arguments.
	 * 
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		double realPart = magnitude * Math.cos(angle);
		double imaginaryPart = magnitude * Math.sin(angle);

		return new ComplexNumber(realPart, imaginaryPart);
	}

	/**
	 * 
	 * Static method that creates ComplexNumber out of provided string.
	 * 
	 * String has to be formated like ""3.51", "3.51", "-3.17", "-2.71i", "i",
	 * "1","-2.71-3.15i"
	 * 
	 * NOTE: "{@value #IMAGINARY_SIGN}" can be only on the last place in String AND
	 * "+" or "-" mustn't be repeated twice in a row.
	 * 
	 * Not acceptable forms of string are : "i351", "-i317", "-+2.71",
	 * "--2.71","i+2" etc
	 * 
	 * @param s String which is parsed to ComplexNumber
	 * @return ComplexNumber representing given string
	 * @throws NumberFormatException if string form isn't acceptable
	 * 
	 */
	public static ComplexNumber parse(String s) {
		double realPart, imaginaryPart;
		s=s.replaceAll(" ","");

		if (s.equals(IMAGINARY_SIGN + "") || s.equals("+" + IMAGINARY_SIGN)) {
			return new ComplexNumber(0, 1);
		}
		if (s.equals("-" + IMAGINARY_SIGN)) {
			return new ComplexNumber(0, -1);
		}

		char[] entry = s.toCharArray();
		if (entry[s.length() - 1] == IMAGINARY_SIGN) {
			int counter = s.length() - 2;
			while (counter >= 0 && entry[counter] != '+' & entry[counter] != '-') {
				counter--;
			}

			if (counter == s.length() - 2 && counter != -1) {
				if (entry[counter] == '+') {
					imaginaryPart = 1;
				} else if (entry[counter] == '-') {
					imaginaryPart = -1;
				} else {
					throw new NumberFormatException();
				}
				realPart = Double.parseDouble(s.substring(0, counter));
				return new ComplexNumber(realPart, imaginaryPart);
			}
			if (counter == 0 || counter == -1) {
				imaginaryPart = Double.parseDouble(s.substring(0, s.length() - 1));
				return new ComplexNumber(0, imaginaryPart);
			}

			imaginaryPart = Double.parseDouble(s.substring(counter, s.length() - 1));
			realPart = Double.parseDouble(s.substring(0, counter));
			return new ComplexNumber(realPart, imaginaryPart);
		}
		return new ComplexNumber(Double.parseDouble(s), 0);
	}

	/**
	 * Method that returns real part of ComplexNumber
	 * 
	 * @return real part of ComplexNumber
	 */
	public double getReal() {
		return this.real;
	}

	/**
	 * Method that returns imaginary part of ComplexNumber
	 * 
	 * @return imaginary part of ComplexNumber
	 */
	public double getImaginary() {
		return this.imaginary;
	}

	/**
	 * Method that returns magnitude of ComplexNumber (usually used in polar
	 * notation)
	 * 
	 * @return magnitude of ComplexNumber (usually used in polar notation)
	 */
	public double getMagnitude() {
		return Math.sqrt(this.real * this.real + this.imaginary * this.imaginary);
	}

	/**
	 * Method that returns angle of ComplexNumber (usually used in polar notation)
	 * Angle is in radians, from 0 to 2 Pi.
	 * 
	 * @return angle of ComplexNumber in radians, from 0 to 2 Pi.
	 */
	public double getAngle() {
		double angle = Math.atan2(this.imaginary, this.real);
		return angle >= 0 ? angle : angle + 2 * Math.PI;
	}

	/**
	 * Method that adds two ComplexNumbers and returns ComplexNumber that is sum of
	 * the two.
	 * 
	 * @param other Complex number
	 * @return ComplexNumber that is sum of two.
	 * @throws NullPointerException if referenced ComplexNumber is null
	 */
	public ComplexNumber add(ComplexNumber other) {
		if (other == null) {
			throw new NullPointerException();
		}
		return new ComplexNumber(this.real + other.getReal(), imaginary + other.getImaginary());
	}

	/**
	 * Method that subtracts two ComplexNumbers and returns ComplexNumber that is
	 * difference of the two.
	 * 
	 * @param other Complex number
	 * @return ComplexNumber that is difference of two.
	 * @throws NullPointerException if referenced ComplexNumber is null
	 */
	public ComplexNumber sub(ComplexNumber other) {
		if (other == null) {
			throw new NullPointerException();
		}
		return new ComplexNumber(this.real - other.getReal(), this.imaginary - other.getImaginary());
	}

	/**
	 * Method that multiplies two ComplexNumbers and returns ComplexNumber that is
	 * result of multiplication of the two.
	 * 
	 * @param other Complex number
	 * @return ComplexNumber that is result of multiplication.
	 * @throws NullPointerException if referenced ComplexNumber is null
	 */
	public ComplexNumber mul(ComplexNumber other) {
		if (other == null) {
			throw new NullPointerException();
		}
		double realPart = this.real * other.getReal() - this.imaginary * other.getImaginary();
		double imaginaryPart = this.getReal() * other.imaginary + this.imaginary * other.getReal();

		return new ComplexNumber(realPart, imaginaryPart);
	}

	/**
	 * Method that divides two ComplexNumbers and returns ComplexNumber that is
	 * result of dividing the two.
	 * 
	 * @param other Complex number
	 * @return ComplexNumber that is quotient of the two.
	 * @throws NullPointerException if referenced ComplexNumber is null // * @throws
	 *                              ArithmeticException if other ComplexNumber
	 *                              magnitude is zero
	 */

	public ComplexNumber div(ComplexNumber other) {
		if (other == null) {
			throw new NullPointerException();
		}

		double realPart = this.real * other.getReal() + this.imaginary * other.getImaginary();
		double imaginaryPart = this.imaginary * other.getReal() - this.real * other.getImaginary();
		double denominator = other.getReal() * other.getReal() + other.getImaginary() * other.getImaginary();

		// Devision by zero is possible (and enabled by default) because double type is
		// used to store result (supports Inf and NaN) . If user wants an exception to be thrown whenever
		// division by zero occurs, if statement below has to be uncommented.
		
//		if (DEVISION_BY_ZERO_DISABLED && denominator == 0) {
//			throw new ArithmeticException("Division by zero!");
//		}
		
		return new ComplexNumber(realPart / denominator, imaginaryPart / denominator);
	}

	/**
	 * Method that raises ComplexNumber to certain power and returns the result.
	 * Uses Moires formula.
	 * 
	 * Complexity is O(n)
	 * 
	 * @param n -> power to which the ComplexNumber is raised to.
	 * @return result of operation
	 * @throws IllegalArgumentException if power is smaller than zero.
	 */
	public ComplexNumber power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		}

		double magnitude = Math.pow(getMagnitude(), n);
		double angle = getAngle() * n;

		return new ComplexNumber(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}

	/**
	 * Method that returns array of n-th roots of ComplexNumber. Uses Moires formula
	 * to calculate roots.
	 * 
	 * complexity: O(n);
	 * 
	 * @param n int representing root which we want to calculate
	 * @return Array of ComplexNumbers representing all n-th roots of initial
	 *         ComplexNumber;
	 * @throws IllegalArgumentException if n is not positive.
	 */
	public ComplexNumber[] root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException();
		}
		ComplexNumber[] solutions = new ComplexNumber[n];

		double magnitude = Math.pow(getMagnitude(), 1.0 / n);
		double firstAngle = getAngle();
		double angle;

		for (int i = 0; i < n; i++) {
			angle = (firstAngle + i * 2 * Math.PI) / n;
			solutions[i] = new ComplexNumber(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
		}

		return solutions;
	}

	/**
	 * Returns a string representation of the object. In general, the toString
	 * method returns a string that"textually represents" this object. The result
	 * should be a concise but informative representation that is easy for a person
	 * to read.
	 * 
	 * @return String formated like (Re(ComplexNumber)+-Im(ComplexNumber), example "23.24+9.2i")
	 * 
	 */
	@Override
	public String toString() {
		if (imaginary >= 0) 
			return real + " + " + imaginary + "i";
		return real + " - " + (-1) * imaginary + "i";
	}

	// used to evaluate tests
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ComplexNumber)) {
			return false;
		}
		ComplexNumber other = (ComplexNumber) obj;

		if (Math.abs(other.getReal() - real) > TOLERANCE) {
			return false;
		}
		if (Math.abs(other.getImaginary() - imaginary) > TOLERANCE) {
			return false;
		}
		return true;
	}

}
