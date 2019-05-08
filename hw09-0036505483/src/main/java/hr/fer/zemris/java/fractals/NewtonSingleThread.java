package hr.fer.zemris.java.fractals;

import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Class that has main method used to demonstrate and visualize Newton-Raphson
 * fractals.
 * 
 * This class holds single thread implementation of problem.
 * @author juren
 *
 */
public class NewtonSingleThread {

	/**
	 * Constant that holds string used at the beggining of program.
	 */
	private static final String WELCOME_MESSAGE = "Welcome to Newton-Raphson iteration-based fractal viewer."
			+ "\r\nPlease enter at least two roots, one root per line. Enter 'done' when done.";

	/**
	 * Main method used to start program which is used to demonstrate and visualize
	 * Newton-Raphson fractals.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		System.out.println(WELCOME_MESSAGE);
		ComplexRootedPolynomial inputPolinomial;
		try {
			inputPolinomial = Util.getInput();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			return;
		}
		FractalViewer.show(new MojProducer(inputPolinomial));
		System.out.println(inputPolinomial);
	}

	/**
	 * private class that holds implementation of {@link IFractalProducer} used for
	 * communication with GUI that is responsible for drawing.
	 * 
	 * This class holds single thread implementation of problem.
	 * @author juren
	 *
	 */
	private static class MojProducer implements IFractalProducer {

		/**
		 * Constant that holds number that represent upper limit of iterations
		 */
		private static final int NUMBER_OF_ITERATIONS = 16 * 16;
		/**
		 * {@link ComplexRootedPolynomial} that holds polynomial that is drawn
		 */
		private ComplexRootedPolynomial polynom;

		/**
		 * Standard constructor for {@link MojProducer}
		 * 
		 * @param polynom that is drawn
		 */
		public MojProducer(ComplexRootedPolynomial polynom) {
			this.polynom = polynom;
		}

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
				long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {

			System.out.println("Starting calculations...");

			int m = NUMBER_OF_ITERATIONS;
			short[] data = new short[width * height];

			NewtonRaphsonLogic.calculate(reMin, reMax, imMin, imMax, width, height, m, 0, height, data, cancel,
					polynom);

			System.out.println("Calculation completed. GUI update!");
			observer.acceptResult(data, (short) (polynom.toComplexPolynom().order() + 1), requestNo);

		}

	}

}
