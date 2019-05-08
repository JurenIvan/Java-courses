package hr.fer.zemris.java.raytracer;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

public class RayCaster {

	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
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

				Point3D OG = view.sub(eye).normalize();
				Point3D yAxis = viewUp.normalize().sub(OG.scalarMultiply(OG.scalarProduct(viewUp.normalize())))
						.normalize();
				Point3D xAxis = OG.vectorProduct(yAxis).normalize();
				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2))
						.add(yAxis.scalarMultiply(vertical / 2));

				Scene scene = RayTracerViewer.createPredefinedScene2();
				short[] rgb = new short[3];
				int offset = 0;
				for (int y = 0; y < height; y++) {
					if (cancel.get())
						break;
					for (int x = 0; x < width; x++) {
						Point3D screenPoint = screenCorner
								.add(xAxis.scalarMultiply((double) x * horizontal / (width - 1))
										.sub(yAxis.scalarMultiply((double) y * vertical / (height - 1))));

						Ray ray = Ray.fromPoints(eye, screenPoint);
						tracer(scene, ray, rgb);
						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
						offset++;
					}
				}

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
	}

	private static void tracer(Scene scene, Ray ray, short[] rgb) {
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
			if (s != null && closest.getPoint().sub(light.getPoint()).norm() > s.getDistance() + 0.00001) {
				continue;
			}
			double ln = r.direction.negate().scalarProduct(s.getNormal());
			if (ln > 0) {
				rgb[0] = (short) (rgb[0] + light.getR() * s.getKdr() * ln);
				rgb[1] = (short) (rgb[1] + light.getG() * s.getKdg() * ln);
				rgb[2] = (short) (rgb[2] + light.getB() * s.getKdb() * ln);
			}

			Point3D reflVec = r.direction.negate()
					.sub(s.getNormal().scalarMultiply(r.direction.negate().scalarProduct(s.getNormal())*2));

			Point3D v = s.getPoint().sub(ray.start);
			double rv = reflVec.scalarProduct(v);
//			if (rv < 0)
//				continue;
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
