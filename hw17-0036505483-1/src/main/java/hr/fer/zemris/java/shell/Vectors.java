package hr.fer.zemris.java.shell;

/**
 * Class that holds bundle of methods used to determine likeness
 * 
 * @author juren
 *
 */
public class Vectors {

	/**
	 * method that calculates cosine between two vectors
	 * 
	 * @param first  vector
	 * @param second vector
	 * @return cosine value
	 */
	public static double cosBetweenVectors(double[] first, double[] second) {
		return Vectors.scalarProduct(first, second) / (Vectors.length(first) * Vectors.length(second));
	}

	/**
	 * Method that returns length of vector
	 * 
	 * @param first vector
	 * @return length
	 */
	private static double length(double[] first) {
		double val = 0;
		for (int i = 0; i < first.length; i++) {
			val += first[i] * first[i];
		}
		return Math.sqrt(val);
	}

	/**
	 * Method that returns dot product of two vectors
	 * 
	 * @param first  vector
	 * @param second vector
	 * @return dot product
	 */
	private static double scalarProduct(double[] first, double[] second) {
		double val = 0;
		for (int i = 0; i < first.length; i++) {
			val += first[i] * second[i];
		}
		return val;
	}
}
