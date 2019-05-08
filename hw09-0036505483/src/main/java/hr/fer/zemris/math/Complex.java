package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 
 * Class representing unmodifiable complex number. Every complex number is
 * stored as a pair of its real and imaginary part in two double values. Class
 * contains a handful of methods used to manipulate over operations with Complex
 * numbers.
 * 
 * @author juren
 *
 */
public class Complex {

	/** Complex number representing zero */
	public static final Complex ZERO = new Complex(0, 0);
	/** Complex number representing one */
	public static final Complex ONE = new Complex(1, 0);
	/** Complex number representing minus one */
	public static final Complex ONE_NEG = new Complex(-1, 0);
	/** Complex number representing imaginary one */
	public static final Complex IM = new Complex(0, 1);
	/** Complex number representing imaginary minus one */
	public static final Complex IM_NEG = new Complex(0, -1);

	/**
	 * Real part of complex number
	 */
	private final double re;
	/**
	 * Imaginary part of complex number
	 */
	private final double im;

	/**
	 * Constructor for ComplexNumber that creates an instance with real and
	 * imaginary values set to one.
	 * 
	 */
	public Complex() {
		re = 1;
		im = 1;
	}

	/**
	 * Constructor for ComplexNumber that creates an instance with provided real and
	 * imaginary pair of values.
	 * 
	 * @param re      part of Complex number
	 * @param im part of Complex number
	 */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}

	/**
	 * Method that returns magnitude of ComplexNumber (usually used in polar
	 * notation)
	 * 
	 * @return magnitude of ComplexNumber (usually used in polar notation)
	 */
	public double module() {
		return Math.sqrt(re * re + im * im);
	}

	/**
	 * Method that multiplies two ComplexNumbers and returns ComplexNumber that is
	 * result of multiplication of the two.
	 * 
	 * @param c Complex number
	 * @return ComplexNumber that is result of multiplication.
	 * @throws NullPointerException if referenced ComplexNumber is null
	 */
	public Complex multiply(Complex c) {
		Objects.requireNonNull(c);
		double realPart = this.re * c.re - this.im * c.im;
		double imaginaryPart = this.re * c.im + this.im * c.re;

		return new Complex(realPart, imaginaryPart);
	}

	/**
	 * Method that divides two ComplexNumbers and returns ComplexNumber that is
	 * result of dividing the two.
	 * 
	 * @param c Complex number
	 * @return ComplexNumber that is quotient of the two.
	 * @throws NullPointerException if referenced ComplexNumber is null // * @throws
	 *                              ArithmeticException if other ComplexNumber
	 *                              magnitude is zero
	 */
	public Complex divide(Complex c) {
		if (c == null) {
			throw new NullPointerException();
		}

		double realPart = this.re * c.re + this.im * c.im;
		double imaginaryPart = this.im * c.re - this.re * c.im;
		double denominator = c.re * c.re + c.im * c.im;
		return new Complex(realPart / denominator, imaginaryPart / denominator);
	}

	/**
	 * Method that adds two ComplexNumbers and returns ComplexNumber that is sum of
	 * the two.
	 * 
	 * @param c Complex number
	 * @return ComplexNumber that is sum of two.
	 * @throws NullPointerException if referenced ComplexNumber is null
	 */
	public Complex add(Complex c) {
		if (c == null) {
			throw new NullPointerException();
		}
		return new Complex(this.re + c.re, im + c.im);
	}

	/**
	 * Method that subtracts two ComplexNumbers and returns ComplexNumber that is
	 * difference of the two.
	 * 
	 * @param c Complex number
	 * @return ComplexNumber that is difference of two.
	 * @throws NullPointerException if referenced ComplexNumber is null
	 */
	public Complex sub(Complex c) {
		if (c == null) {
			throw new NullPointerException();
		}
		return new Complex(this.re - c.re, im - c.im);
	}

	/**
	 * Returns new complex number that is this one negated.
	 * 
	 * @return negated complex number
	 */
	public Complex negate() {
		return new Complex(-re, -im);
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
	public Complex power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		}

		double magnitude = Math.pow(module(), n);
		double angle = getAngle() * n;

		return new Complex(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
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
	public List<Complex> root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException();
		}
		List<Complex> solutions = new ArrayList<Complex>();

		double magnitude = Math.pow(module(), 1.0 / n);
		double firstAngle = getAngle();
		double angle;

		for (int i = 0; i < n; i++) {
			angle = (firstAngle + i * 2 * Math.PI) / n;
			solutions.add(new Complex(magnitude * Math.cos(angle), magnitude * Math.sin(angle)));
		}

		return solutions;
	}

	@Override
	public String toString() {
		if (im >= 0)
			return "(" + re + "+i" + Math.abs(im) + ")";
		return "(" + re + "-i" + (-1) * im + ")";
	}

	/**
	 * Method that returns angle of ComplexNumber (usually used in polar notation)
	 * Angle is in radians, from 0 to 2 Pi.
	 * 
	 * @return angle of ComplexNumber in radians, from 0 to 2 Pi.
	 */
	private double getAngle() {
		double angle = Math.atan2(this.im, this.re);
		return angle >= 0 ? angle : angle + 2 * Math.PI;
	}

}
