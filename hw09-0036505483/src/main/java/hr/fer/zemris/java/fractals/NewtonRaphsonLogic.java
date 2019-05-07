package hr.fer.zemris.java.fractals;

import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

public class NewtonRaphsonLogic {

	private static double convergenceTreshold = 0.01;

	public static void calculate(double reMin, double reMax, double imMin, double imMax, int width, int height, int m,
			int yMin, int yMax, short[] data, AtomicBoolean cancel, ComplexRootedPolynomial polynom) {

		int offset = yMin * width;
		double module;

		for (int y = yMin; y < yMax; y++) {
			if (cancel.get())
				break;
			for (int x = 0; x < width; x++) {

				double re = reMin + x / (width - 1.0) * (reMax - reMin);
				double im = imMin + (height - 1.0 - y) / (height - 1) * (imMax - imMin);
				Complex c = new Complex(re, im);
				short iter = 0;

				ComplexPolynomial complexPolynomial = polynom.toComplexPolynom();
				ComplexPolynomial derived = complexPolynomial.derive();

				do {
					Complex numerator = polynom.apply(c);
					Complex denominator = derived.apply(c);
					Complex znold = c;
					Complex fraction = numerator.divide(denominator);
					c = c.sub(fraction);
					module = znold.sub(c).module();
					iter++;

				} while (module > convergenceTreshold && iter < m);

				short index = (short) polynom.indexOfClosestRootFor(c, convergenceTreshold);
				data[offset++] = (short) (index + 1);
			}
		}

	}

}
