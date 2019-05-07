package hr.fer.zemris.math;

import java.util.Arrays;

public class ComplexPolynomial {

	private final Complex[] coeficients;

	public ComplexPolynomial(Complex... factors) {
		coeficients = Arrays.copyOf(factors, factors.length);
	}

	public short order() {
		return (short) (coeficients.length - 1);
	}

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

	public ComplexPolynomial derive() {
		Complex[] factors = new Complex[coeficients.length - 1];

		for (int i = 0; i <coeficients.length-1; i++) {
			factors[i] = coeficients[i].multiply(new Complex(coeficients.length-i-1, 0));
		}

		return new ComplexPolynomial(factors);

	}

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
