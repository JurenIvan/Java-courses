package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
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

public class Newton2 {

	private static final String WELCOME_MESSAGE = "Welcome to Newton-Raphson iteration-based fractal viewer."
			+ "\r\nPlease enter at least two roots, one root per line. Enter 'done' when done.";

	public static void main(String[] args) {
		System.out.println(WELCOME_MESSAGE);
		ComplexRootedPolynomial inputPolinomial = Util.getInput();
		FractalViewer.show(new MojProducer(inputPolinomial));
	}

	public static class MojProducer implements IFractalProducer {

		private static final int NUMBER_OF_ITERATIONS = 16*16*16;
		private ComplexRootedPolynomial polynom;

		public MojProducer(ComplexRootedPolynomial inputPolinomial) {
			this.polynom = inputPolinomial;
		}

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
				long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {

			System.out.println("Starting calculations...");
			int m = NUMBER_OF_ITERATIONS;
			short[] data = new short[width * height];
			final int numberOfParalelTracks = 8 * Runtime.getRuntime().availableProcessors();
			int heightPerTrack = height / numberOfParalelTracks;

			ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
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
			pool.shutdown();
			System.out.println("Calculation completed. GUI update!");
			observer.acceptResult(data, (short) (polynom.toComplexPolynom().order() + 1), requestNo);
		}

	}

	private static class PosaoIzracuna implements Callable<Void> {
		private double reMin;
		private double reMax;
		private double imMin;
		private double imMax;
		private int width;
		private int height;
		private int yMin;
		private int yMax;
		private int m;
		private short[] data;
		private AtomicBoolean cancel;
		private ComplexRootedPolynomial polynom;

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
			this.polynom = polynom;
		}

		@Override
		public Void call() {

			NewtonRaphsonLogic.calculate(reMin, reMax, imMin, imMax, width, height, m, yMin, yMax, data, cancel,
					polynom);

			return null;
		}
	}

}