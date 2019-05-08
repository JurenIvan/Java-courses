package hr.fer.zemris.java.fractals;

import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Util class containing calculate method which fills data array with
 * appropriate numbers resembling.
 * 
 * @author juren
 *
 */
public class NewtonRaphsonLogic {
	/**
	 * constant that determines convergence threshold
	 */
	private static double convergenceThreshold = 0.002;

	/**
	 * Static method that is used to fill data array that GUI uses to draw picture.
	 * 
	 * @param reMin      Lower limit to real part of complex number used.
	 * @param reMax      Upper limit to real part of complex number used.
	 * @param imMin      Lower limit to imaginary part of complex number used.
	 * @param imMaxUpper limit to imaginary part of complex number used.
	 * @param width      width of GUI in pixels
	 * @param height     height of GUI in pixels
	 * @param yMin       first y coordinate for which this part of job is
	 *                   responsible
	 * @param yMax       last y coordinate for which this part of job is responsible
	 * @param m          Number of iterations for calculating convergence
	 * @param data       Array that stores data used by gui to draw picture
	 * @param cancel     @link AtomicBoolean} used to stop job is it's results are
	 *                   no longer relevant
	 * @param polynom    Polynomial that is represented
	 */
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

				} while (module > convergenceThreshold && iter < m);

				short index = (short) polynom.indexOfClosestRootFor(c, convergenceThreshold);
				data[offset++] = (short) (index + 1);
			}
		}
	}
}
