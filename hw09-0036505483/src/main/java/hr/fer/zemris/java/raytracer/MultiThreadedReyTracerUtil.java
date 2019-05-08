package hr.fer.zemris.java.raytracer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.Scene;

/**
 * Class containing methods used by {@link RayCasterParallel} and
 * {@link RayCasterParallel2}.
 * 
 * Contains multi-threaded implementations of methods. Has constructor because
 * scene can be changed.
 * 
 * @author juren
 *
 */
public class MultiThreadedReyTracerUtil extends ReyTracerUtil {

	private Scene scene;

	public MultiThreadedReyTracerUtil(Scene scene) {
		super();
		this.scene = scene;
	}

	/**
	 * Multi-threaded implementation of {@link IRayTracerProducer} that specifies
	 * objects which are capable to create scene snapshots by using ray-tracing
	 * technique.
	 * 
	 * @return {@link IRayTracerProducer}
	 */
	protected IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
					int width, int height, long requestNo, IRayTracerResultObserver observer, AtomicBoolean cancel) {
				System.out.println("Započinjem izračune...");

				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];

				ForkJoinPool pool = new ForkJoinPool();
				pool.invoke(new JobToDo(eye, view, viewUp, horizontal, vertical, width, height, requestNo, observer,
						cancel, red, green, blue, 0, 0, height));
				pool.shutdown();

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}

		};
	}

	/**
	 * Class that models job and is implementation of {@link RecursiveAction}
	 * interface therefore it has compute method. This implementation recursively
	 * divides job until each thread has only (maximum) 32 paralel lines to render.
	 * 
	 * @author juren
	 *
	 */
	private class JobToDo extends RecursiveAction {

		private static final long serialVersionUID = 1L;
		/** point that represents position of eye */
		private Point3D eye;

		/** vector that represents direction of looking */
		private Point3D view;

		/** view up vector determining up in view */
		private Point3D viewUp;

		/** horizontal width of observed space */
		private double horizontal;

		/** vertical width of observed space */
		private double vertical;

		/** number of pixels per screen row */
		private int width;

		/** number of pixels per screen column */
		private int height;

		/** used internally and must be passed on to GUI observer with rendered image */
		private long requestNo;

		/** GUI observer that will accept and display image this producer creates */
		private IRayTracerResultObserver observer;

		/** GUI observer that will accept and display image this producer creates */
		private AtomicBoolean cancel;

		/** array that stores data for red color */
		private short[] red;

		/** array that stores data for green color */
		private short[] green;

		/** array that stores data for blue color */
		private short[] blue;

		/** variable used to calculate offset used by diferent threads */
		private int offset;

		/** first row that is processed by thread */
		private int yMin;

		/** last row that is processed by thread */
		private int yMax;

		/**
		 * Standard constructor for job.
		 * 
		 * @param eye        point that represents position of eye
		 * @param view       vector that represents direction of looking
		 * @param viewUp     view up vector determining up in view
		 * @param horizontal horizontal width of observed space
		 * @param vertical   vertical width of observed space
		 * @param width      number of pixels per screen row
		 * @param height     number of pixels per screen collumns
		 * @param requestNo  used internally and must be passed on to GUI observer with
		 *                   rendered image
		 * @param observer   GUI observer that will accept and display image this
		 *                   producer creates
		 * @param cancel     GUI observer that will accept and display image this
		 *                   producer creates
		 */
		public JobToDo(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical, int width,
				int height, long requestNo, IRayTracerResultObserver observer, AtomicBoolean cancel, short[] red,
				short[] green, short[] blue, int offset, int yMin, int yMax) {
			super();
			this.eye = eye;
			this.view = view;
			this.viewUp = viewUp;
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.width = width;
			this.height = height;
			this.requestNo = requestNo;
			this.observer = observer;
			this.cancel = cancel;
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.offset = offset;
			this.yMax = yMax;
			this.yMin = yMin;
		}

		@Override
		protected void compute() {

			if (yMax - yMin < 32) {
				Point3D OG = view.sub(eye).normalize();
				Point3D yAxis = viewUp.normalize().sub(OG.scalarMultiply(OG.scalarProduct(viewUp.normalize())))
						.normalize();
				Point3D xAxis = OG.vectorProduct(yAxis).normalize();
				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2))
						.add(yAxis.scalarMultiply(vertical / 2));

				short[] rgb = new short[3];
				for (int y = yMin; y < yMax; y++) {
					if (cancel.get())
						break;
					for (int x = 0; x < width; x++) {

						Point3D screenPoint = screenCorner
								.add(xAxis.scalarMultiply((double) x * horizontal / (width - 1))
										.sub(yAxis.scalarMultiply((double) y * vertical / (height - 1))));

						Ray ray = Ray.fromPoints(eye, screenPoint);
						ReyTracerUtil.tracer(scene, ray, rgb);
						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
						offset++;
					}
				}
				return;
			}

			invokeAll(
					new JobToDo(eye, view, viewUp, horizontal, vertical, width, height, requestNo, observer, cancel,
							red, green, blue, offset, yMin, (yMin + yMax) / 2),
					new JobToDo(eye, view, viewUp, horizontal, vertical, width, height, requestNo, observer, cancel,
							red, green, blue, offset + ((yMin + yMax) / 2 - yMin) * width, (yMin + yMax) / 2, yMax));
		}

	}

}
