package hr.fer.zemris.java.raytracer;

import java.util.List;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;

public class Util {

	 protected static void tracer(Scene scene, Ray ray, short[] rgb) {
		rgb[0] = 15;
		rgb[1] = 15;
		rgb[2] = 15;
		RayIntersection closest = findClosestIntersection(scene, ray);
		if (closest == null) {
			rgb[0] = 0;
			rgb[1] = 0;
			rgb[2] = 0;

			return;
		}
		for (LightSource light : scene.getLights()) {
			Ray r = Ray.fromPoints(light.getPoint(), closest.getPoint());

			RayIntersection s = findClosestIntersection(scene, r);

			if(s==null) continue;
			if (s != null && closest.getPoint().sub(light.getPoint()).norm() > s.getDistance() + 0.00001) {
				continue;
			}
			
			double ln = r.direction.negate().scalarProduct(s.getNormal());
			if (ln > 0) {
				rgb[0] = (short) (rgb[0] + light.getR() * s.getKdr() * ln);
				rgb[1] = (short) (rgb[1] + light.getG() * s.getKdg() * ln);
				rgb[2] = (short) (rgb[2] + light.getB() * s.getKdb() * ln);
			}

			Point3D reflVec = r.direction
					.sub(s.getNormal().scalarMultiply(r.direction.scalarProduct(s.getNormal()) * 2));
			Point3D v = ray.direction.negate();

			double rv = reflVec.scalarProduct(v);
			if (rv < 0)
				continue;
			rgb[0] = (short) (rgb[0] + light.getR() * s.getKrr() * Math.pow(rv, s.getKrn()));
			rgb[1] = (short) (rgb[1] + light.getG() * s.getKrg() * Math.pow(rv, s.getKrn()));
			rgb[2] = (short) (rgb[2] + light.getB() * s.getKrb() * Math.pow(rv, s.getKrn()));
		}

	}

	private static RayIntersection findClosestIntersection(Scene scene, Ray ray) {
		List<GraphicalObject> objects = scene.getObjects();
		RayIntersection closestIntersection = null;
		double minDistance = Double.MAX_VALUE;

		for (var elem : objects) {

			RayIntersection ri = elem.findClosestRayIntersection(ray);
			if (ri == null) {
				continue;
			}

			if (ri.getDistance() < minDistance) {
				closestIntersection = ri;
				minDistance = ri.getDistance();
			}
		}
		return closestIntersection;
	}
	
}
