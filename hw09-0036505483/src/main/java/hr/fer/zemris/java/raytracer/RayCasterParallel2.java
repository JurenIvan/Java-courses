package hr.fer.zemris.java.raytracer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.IRayTracerAnimator;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

public class RayCasterParallel2 {

	private static Scene scene;

	public static void main(String[] args) {
		scene = RayTracerViewer.createPredefinedScene2();
		RayTracerViewer.show(getIRayTracerProducer(), getIRayTracerAnimator(), 30, 30);
	}

	private static IRayTracerAnimator getIRayTracerAnimator() {
		return new IRayTracerAnimator() {
			long time;

			@Override
			public void update(long deltaTime) {
				time += deltaTime;
			}

			@Override
			public Point3D getViewUp() { // fixed in time
				return new Point3D(0, 0, 10);
			}

			@Override
			public Point3D getView() { // fixed in time
				return new Point3D(-2, 0, -0.5);
			}

			@Override
			public long getTargetTimeFrameDuration() {
				return 60; // redraw scene each 150 milliseconds
			}

			@Override
			public Point3D getEye() { // changes in time
				double t = (double) time / 10000 * 2 * Math.PI;
				double t2 = (double) time / 5000 * 2 * Math.PI;
				double x = 50 * Math.cos(t);
				double y = 50 * Math.sin(t);
				double z = 30 * Math.sin(t2);
				return new Point3D(x, y, z);
			}
		};
	}

	private static IRayTracerProducer getIRayTracerProducer() {
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

	public static class JobToDo extends RecursiveAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Point3D eye;
		private Point3D view;
		private Point3D viewUp;
		private double horizontal;
		private double vertical;
		private int width;
		private int height;
		private long requestNo;
		private IRayTracerResultObserver observer;
		private AtomicBoolean cancel;
		private short[] red;
		private short[] green;
		private short[] blue;
		private int offset;
		private int yMin;
		private int yMax;

		/**
		 * @param eye
		 * @param view
		 * @param viewUp
		 * @param horizontal
		 * @param vertical
		 * @param width
		 * @param height
		 * @param requestNo
		 * @param observer
		 * @param cancel
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
						Util.tracer(scene, ray, rgb);
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
