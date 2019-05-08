package hr.fer.zemris.java.raytracer.model;

/**
 * Class that hold implementation for {@link GraphicalObject} of Sphere.
 * 
 * @author juren
 *
 */
public class Sphere extends GraphicalObject {
	/**
	 * point where a center of sphere is
	 */
	private Point3D center;
	/**
	 * double representing radius of sphere
	 */
	private double radius;
	/**
	 * determine the object parameter for diffuse component of red color
	 */
	private double kdr;
	/**
	 * determine the object parameter for diffuse component of green color
	 */
	private double kdg;
	/**
	 * determine the object parameter for diffuse component of blue color
	 */
	private double kdb;
	/**
	 * determine the object parameter for reflective components for blue color
	 */
	private double krr;
	/**
	 * determine the object parameter for reflective components for red color
	 */
	private double krg;
	/**
	 * determine the object parameter for reflective components for green color
	 */
	private double krb;
	/**
	 * determine the object parameter for shininess factor
	 */
	private double krn;

	/**
	 * Constructor for sphere.
	 * 
	 * @param center point where a center of sphere is
	 * @param radius double representing radius of sphere
	 * @param kdr    determine the object parameter for diffuse component of red
	 *               color
	 * @param kdg    determine the object parameter for diffuse component of green
	 *               color
	 * @param kdb    determine the object parameter for diffuse component of blue
	 *               color
	 * @param krr    determine the object parameter for reflective components for
	 *               red color
	 * @param krg    determine the object parameter for reflective components for
	 *               green color
	 * @param krb    determine the object parameter for reflective components for
	 *               blue color
	 * @param krn    determine the object parameter for shininess factor
	 */
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

	/**
	 * Private method used to determine whether a point is inside or outside a
	 * sphere
	 * 
	 * @param start point that we want to check
	 * @return position of point in relation to sphere (true->it is inside)
	 */
	private boolean isInside(Point3D start) {
		return start.sub(center).norm() < this.radius;
	}

	/**
	 * Class that holds implementation of {@link RayIntersection} for {@link Sphere}
	 * 
	 * @author juren
	 *
	 */
	public class RayIntersectionSphere extends RayIntersection {
		/**
		 * Classic constructor for {@link RayIntersectionSphere}
		 * 
		 * @param point    point of intersection between ray and object
		 * @param distance distance between start of ray and intersection
		 * @param outer    specifies if intersection is outer
		 */
		public RayIntersectionSphere(Point3D point, double distance, boolean outer) {
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
