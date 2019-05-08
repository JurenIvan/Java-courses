package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.Objects;

/**
 * Class that models polynomial written in form a2*x^2+a1*x^1+a0. For example
 * (2.0+i0.0)*z^4+(0.0+i0.0)*z^3+(0.0+i0.0)*z^2+(0.0+i0.0)*z^1+(-2.0+i0.0) .
 * Class contains bundle of methods used to get information out of polynomial.
 * 
 * @author juren
 *
 */
public class ComplexPolynomial {

	/**
	 * Array that stores coefficient of Complex polynomial.
	 */
	private final Complex[] coeficients;

	/**
	 * Constructor that creates new polynomial.
	 * 
	 * @param factors array of factors
	 * @throws NullPointerException if no roots are provided
	 */
	public ComplexPolynomial(Complex... factors) {
		Objects.requireNonNull(factors, "Cannot create polynomial with no roots");
		coeficients = Arrays.copyOf(factors, factors.length);
	}

	/**
	 * Method that returns order of polynomial.
	 * 
	 * @return order of polynomial
	 */
	public short order() {
		return (short) (coeficients.length - 1);
	}

	/**
	 * Method that multiplies this {@link ComplexPolynomial} with other.
	 * 
	 * @param p other {@link ComplexPolynomial}
	 * @return new {@link ComplexPolynomial} created by multiplication
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		int totalLength = coeficients.length + p.coeficients.length - 1;
		Complex[] result = new Complex[totalLength];
		for (int i = 0; i < totalLength; i++) {
			result[i] = new Complex(0, 0);
		}

		for (int i = 0; i < coeficients.length; i++) {
			for (int j = 0; j < p.coeficients.length; j++) {
				result[i + j] = result[i + j].add(coeficients[i].multiply(p.coeficients[j]));
			}
		}
		return new ComplexPolynomial(result);
	}

	/**
	 * Method that derives this polynomial and return new {@link ComplexPolynomial}
	 * representing derived function
	 * 
	 * @return derived {@link ComplexPolynomial}
	 */
	public ComplexPolynomial derive() {
		Complex[] factors = new Complex[coeficients.length - 1];

		for (int i = 0; i < coeficients.length - 1; i++) {
			factors[i] = coeficients[i].multiply(new Complex(coeficients.length - i - 1, 0));
		}
		return new ComplexPolynomial(factors);

	}

	/**
	 * Method used to calculate value of polynomial in given complex point
	 * 
	 * @param z f(z) that we evaluate complex number for
	 * @return value of polynomial in point z
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ZERO;

		for (int i = 0; i < coeficients.length; i++) {
			result = result.add(z.power(i).multiply(coeficients[coeficients.length - i - 1]));
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = coeficients.length - 1; i > 0; i--) {
			sb.append(coeficients[coeficients.length - 1 - i] + "*z^" + i + "+");
		}
		if (coeficients.length > 0)
			sb.append(coeficients[coeficients.length - 1]);
		return sb.toString();
	}

}
