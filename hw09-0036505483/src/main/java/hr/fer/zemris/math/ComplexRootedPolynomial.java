package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.Objects;

/**
 * Class that models polynomial written in form a*(x-n1)(x-n2). For example
 * (2.0+i0.0)*(z-(1.0+i0.0))*(z-(-1.0+i0.0))*(z-(0.0+i1.0))*(z-(0.0-i1.0)). Class
 * contains bundle of methods used to get information out of polynomial.
 * 
 * @author juren
 *
 */
public class ComplexRootedPolynomial {
	/**
	 * Array that stores coefficient of Complex polynomial
	 */
	private Complex[] coefficient;
	/**
	 * Complex number representing a in polynomial a*(x-n1)(x-2)
	 */
	private Complex factor;

	/**
	 * Constructor that creates new Polynomial
	 * 
	 * @param constant
	 * @param roots
	 * @throws NullPointerException if no roots are provided
	 */
	public ComplexRootedPolynomial(Complex constant, Complex... roots) {
		Objects.requireNonNull(roots, "Cannot create polynomial with no roots");
		factor = constant;
		coefficient = Arrays.copyOf(roots, roots.length);

	}

	/**
	 * Method used to calculate value of polynomial in given complex point
	 * 
	 * @param z f(z) that we evaluate complex number for
	 * @return value of polynomial in point z
	 */
	public Complex apply(Complex z) {
		Complex result = factor;
		for (int i = 0; i < coefficient.length; i++) {
			result = result.multiply((z.sub(coefficient[i])));
		}
		return result;
	}

	/**
	 * Finds index of closest root to a given complex number that is inside a given
	 * threshold.
	 * 
	 * @param z        complex number that is supposed to be close to a root
	 * @param treshold radius of acceptance for finding root
	 * @return index of root
	 */
	public int indexOfClosestRootFor(Complex z, double threshold) {
		double minDistance = Double.MAX_VALUE;
		int indexOfResult = -1;

		for (int i = 0; i < coefficient.length; i++) {

			double distance = coefficient[i].sub(z).module();
			if (distance < minDistance && distance < threshold) {
				minDistance = distance;
				indexOfResult = i;
			}
		}
		return indexOfResult;
	}

	/**
	 * Method that translates this polynomial to another form for example this
	 * :"2.0+i0.0)*(z-(1.0+i0.0))*(z-(-1.0+i0.0))*(z-(0.0+i1.0))*(z-(0.0-i1.0))"
	 * gets translated into this
	 * (2.0+i0.0)*z^4+(0.0+i0.0)*z^3+(0.0+i0.0)*z^2+(0.0+i0.0)*z^1+(-2.0+i0.0)"
	 * 
	 * @return {@link ComplexPolynomial} created out of this one
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial cp = new ComplexPolynomial(factor);
		for (int i = 0; i < coefficient.length; i++) {
			cp = cp.multiply(new ComplexPolynomial(Complex.ONE, coefficient[i].negate()));
		}
		return cp;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(factor + "*");
		for (int i = 0; i < coefficient.length; i++) {
			sb.append("(z-" + coefficient[i].toString() + ")*");
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
}
