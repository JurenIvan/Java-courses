package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.IRayTracerAnimator;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Class that has main method and demonstrates render of static predefined scene
 * that was given to us. Point of view is animated so picture is constantly
 * changing. Solution is multi-threaded.
 * 
 * @author juren
 *
 */
public class RayCasterParallel2 {

	/**
	 * Main method used to start the program. See more : {@link RayCasterParallel2}
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		MultiThreadedReyTracerUtil u = new MultiThreadedReyTracerUtil(RayTracerViewer.createPredefinedScene2());
		RayTracerViewer.show(u.getIRayTracerProducer(), getIRayTracerAnimator(), 30, 30);
	}

	/**
	 * implementation for {@link IRayTracerAnimator} interface that is used to
	 * describe animation support for out raytracer. Objects which are of type
	 * IRayTracerAnimator will be informed of the time progress, and for current
	 * time can be asked where the observer is, where he look at, where is "up".
	 * 
	 * @return {@link IRayTracerAnimator} 
	 */
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
				return 30; // redraw scene each 150 milliseconds
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
}
