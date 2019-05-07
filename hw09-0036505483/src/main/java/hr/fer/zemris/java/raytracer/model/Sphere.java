package hr.fer.zemris.java.raytracer.model;

public class Sphere extends GraphicalObject {
	private Point3D center;
	private double radius;
	private double kdr;
	private double kdg;
	private double kdb;
	private double krr;
	private double krg;
	private double krb;
	private double krn;

	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb,
			double krn) {
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		Point3D oc = ray.start.sub(center);

		double a = ray.direction.scalarProduct(ray.direction);
		double b = 2 * ray.direction.scalarProduct(oc);
		double c = oc.scalarProduct(oc) - radius * radius;
		double determinant = b * b - 4 * a * c;

		if (determinant < 0) {
			return null;
		}
		determinant = Math.sqrt(determinant);
		Point3D p;
		double t;
		if (determinant == 0) {
			t = (-b + determinant) / (2 * a);
			p = ray.start.add(ray.direction.scalarMultiply(t));
		} else {
			double t1 = (-b + determinant) / (2 * a);
			double t2 = (-b - determinant) / (2 * a);
			if (t1 < t2) {
				p = ray.start.add(ray.direction.scalarMultiply(t1));
				t = t1;
			} else {
				p = ray.start.add(ray.direction.scalarMultiply(t2));
				t = t2;
			}
		}
		return new RayIntersectionSphere(p, t, isInside(ray.start));
	}

	private boolean isInside(Point3D start) {
		return start.sub(center).norm() < this.radius;
	}

	public class RayIntersectionSphere extends RayIntersection {
		protected RayIntersectionSphere(Point3D point, double distance, boolean outer) {
			super(point, distance, outer);
		}

		@Override
		public Point3D getNormal() {
			return super.getPoint().sub(center).normalize();
		}

		@Override
		public double getKdr() {
			return kdr;
		}

		@Override
		public double getKdg() {
			return kdg;
		}

		@Override
		public double getKdb() {
			return kdb;
		}

		@Override
		public double getKrr() {
			return krr;
		}

		@Override
		public double getKrg() {
			return krg;
		}

		@Override
		public double getKrb() {
			return krb;
		}

		@Override
		public double getKrn() {
			return krn;
		}

	}

}
