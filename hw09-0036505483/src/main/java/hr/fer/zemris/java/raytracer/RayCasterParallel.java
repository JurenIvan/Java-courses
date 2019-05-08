package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Class that has main method and demonstrates render of static predefined scene
 * that was given to us. Point of view is not animated. Picture is produced by
 * using Phongs model for lightning.
 * 
 * This implementation is multi-threaded
 * 
 * @author juren
 *
 */
public class RayCasterParallel {

	/**
	 * Main method used to start the program. See more : {@link RayCasterParallel}
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		
		MultiThreadedReyTracerUtil u=new MultiThreadedReyTracerUtil(RayTracerViewer.createPredefinedScene());
		RayTracerViewer.show(u.getIRayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}
}
