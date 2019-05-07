package hr.fer.zemris.java.fractals;

import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.ComplexRootedPolynomial;

public class NewtonSingleThread {

	private static final String WELCOME_MESSAGE = "Welcome to Newton-Raphson iteration-based fractal viewer."
			+ "\r\nPlease enter at least two roots, one root per line. Enter 'done' when done.";

	public static void main(String[] args) {
		System.out.println(WELCOME_MESSAGE);
		ComplexRootedPolynomial inputPolinomial = Util.getInput();
		FractalViewer.show(new MojProducer(inputPolinomial));
	}


	private static class MojProducer implements IFractalProducer {

		private static final int NUMBER_OF_ITERATIONS = 16*16;
		private ComplexRootedPolynomial polynom;

		public MojProducer(ComplexRootedPolynomial polynom) {
			this.polynom = polynom;
		}

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
				long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {

			System.out.println("Starting calculations...");
			
			int m = NUMBER_OF_ITERATIONS;
			short[] data = new short[width * height];
			
			NewtonRaphsonLogic.calculate(reMin, reMax, imMin, imMax, width, height, m, 0, height-1, data, cancel, polynom);
			
			System.out.println("Calculation completed. GUI update!");
			observer.acceptResult(data, (short)(polynom.toComplexPolynom().order()+1), requestNo);
			
		}

	}

}
