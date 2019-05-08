package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Class that has main method used to demonstrate and visualize Newton-Raphson
 * fractals.
 * 
 * This class contains multi-threaded implementation of problem.
 * 
 * @author juren
 *
 */
public class Newton {

	/**
	 * Constant that holds string used at the beginning of program.
	 */
	private static final String WELCOME_MESSAGE = "Welcome to Newton-Raphson iteration-based fractal viewer."
			+ "\r\nPlease enter at least two roots, one root per line. Enter 'done' when done.";

	/**
	 * Main method used to start program which is used to demonstrate and visualize
	 * Newton-Raphson fractals. Expects user input through system.in .
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
	}

	/**
	 * Private class that holds implementation of {@link IFractalProducer} used for
	 * communication with GUI that is responsible for drawing.
	 * 
	 * This class holds multi-threaded implementation of problem. Problem is divided
	 * into 8 times the number of cores in system and job is equally divided between
	 * them.
	 * 
	 * @author juren
	 *
	 */
	public static class MojProducer implements IFractalProducer {
		/**
		 * Constant that holds number that represent upper limit of iterations
		 */
		private static final int NUMBER_OF_ITERATIONS = 16 * 16 * 16;
		/**
		 * {@link ComplexRootedPolynomial} that holds polynomial that is drawn
		 */
		private ComplexRootedPolynomial polynom;

		/**
		 * Thread pool used for calculating
		 */
		private ExecutorService pool;

		/**
		 * Standard constructor for {@link MojProducer}
		 * 
		 * @param polynom that is drawn
		 */
		public MojProducer(ComplexRootedPolynomial inputPolinomial) {
			this.polynom = Objects.requireNonNull(inputPolinomial, "Cannot have null reference as a polynome");
			pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), (r) -> {
				Thread t = new Thread(r);
				t.setDaemon(true);
				return t;
			});
		}

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
				long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {

			System.out.println("Starting calculations...");
			int m = NUMBER_OF_ITERATIONS;
			short[] data = new short[width * height];
			final int numberOfParalelTracks = 8 * Runtime.getRuntime().availableProcessors();
			int heightPerTrack = height / numberOfParalelTracks;

			List<Future<Void>> results = new ArrayList<>();

			for (int i = 0; i < numberOfParalelTracks; i++) {
				int yMin = i * heightPerTrack;
				int yMax = (i + 1) * heightPerTrack;
				if (i == numberOfParalelTracks - 1) {
					yMax = height - 1;
				}
				PosaoIzracuna job = new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data,
						cancel, polynom);

				results.add(pool.submit(job));
			}
			for (Future<Void> job : results) {
				try {
					job.get();
				} catch (InterruptedException | ExecutionException e) {
				}
			}
			System.out.println("Calculation completed. GUI update!");
			observer.acceptResult(data, (short) (polynom.toComplexPolynom().order() + 1), requestNo);
		}

	}

	/**
	 * Private class that models a job that each thread has to do.
	 * 
	 * @author juren
	 *
	 */
	private static class PosaoIzracuna implements Callable<Void> {
		/**
		 * Lower limit to real part of complex number used.
		 */
		private double reMin;
		/**
		 * Upper limit to real part of complex number used.
		 */
		private double reMax;
		/**
		 * Lower limit to imaginary part of complex number used.
		 */
		private double imMin;
		/**
		 * Upper limit to imaginary part of complex number used.
		 */
		private double imMax;
		/**
		 * width of GUI in pixels
		 */
		private int width;
		/**
		 * height of GUI in pixels
		 */
		private int height;
		/**
		 * first y coordinate for which this part of job is responsible
		 */
		private int yMin;
		/**
		 * last y coordinate for which this part of job is responsible
		 */
		private int yMax;
		/**
		 * Number of iterations for calculating convergence
		 */
		private int m;
		/**
		 * Array that stores data used by gui to draw picture
		 */
		private short[] data;
		/**
		 * {@link AtomicBoolean} used to stop job is it's results are no longer relevant
		 */
		private AtomicBoolean cancel;
		/**
		 * Polynomial that is represented
		 */
		private ComplexRootedPolynomial polynom;

		/**
		 * Classic constructor.
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
		public PosaoIzracuna(double reMin, double reMax, double imMin, double imMax, int width, int height, int yMin,
				int yMax, int m, short[] data, AtomicBoolean cancel, ComplexRootedPolynomial polynom) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.m = m;
			this.data = data;
			this.cancel = cancel;
			this.polynom = Objects.requireNonNull(polynom, "Cannot represent null polynome.");
		}

		@Override
		public Void call() {

			NewtonRaphsonLogic.calculate(reMin, reMax, imMin, imMax, width, height, m, yMin, yMax, data, cancel,
					polynom);

			return null;
		}
	}

}
